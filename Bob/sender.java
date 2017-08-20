import java.io.*;
import java.net.* ;
public class sender implements Runnable
{
    private Socket clientsocket ;
    private DataOutputStream ostream ;
    private BufferedReader keyRead ;
    private mex flag ;
    private InetAddress host ;
    private int port ;
    public sender(Socket _clientsocket, String name,mex _flag,int _port)
    {
        try
        {
            clientsocket = _clientsocket ;
            flag = _flag ;
            ostream = new DataOutputStream(clientsocket.getOutputStream());
            keyRead = new BufferedReader(new InputStreamReader(System.in));
            host = InetAddress.getByName("localhost") ;
            port = _port ;
        }
        catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection") ;
        }
        new Thread(this,name).start() ;
    }
    String bar(String filename, long total, long done)
    {
        int p = (int)((done * 10) / total) ;
        String ans = "Transferring : " + filename + " [" ;
        for(int i = 1; i <= p ; ++i) ans = ans + "=" ;
        ans += ">" ;
        for(int i = p+1 ; i <= 10 ; ++i) ans += "_" ;
        ans += "]" + (10*p) + "%" ;
        ans += "\r" ;
        return ans ;
    }
    void sendFileUdp(String path)
    {
        flag.book() ;
        // System.out.println("Sending file : " + path) ;
        FileInputStream fistream = null ;
        byte[] buffer = new byte[1024] ;
        try
        {
            fistream = new FileInputStream(path);
            DatagramSocket osock = new DatagramSocket();
            // send the size
            long filesz = fistream.getChannel().size() ;
            ostream.writeLong(filesz) ;
            ostream.flush() ;
            // System.out.println("File sz = " + filesz) ;
            // send the file
            long done = 0 ;
            while (fistream.read(buffer) > 0)
            {
                DatagramPacket dp = new DatagramPacket(buffer,buffer.length,host,port) ;
                osock.send(dp) ;
                done += buffer.length ;
                System.out.write(bar(path,filesz,Math.min(done,filesz)).getBytes()) ;
            }
            System.out.println("") ;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path + e) ;
        }
        try
        {
            fistream.close() ;
            // System.out.println("sending of file is now over") ;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
        flag.unbook() ;
    }
    void sendFileTcp(String path)
    {
        flag.book() ;
        // System.out.println("Sending file : " + path) ;
        FileInputStream fistream = null ;
        byte[] buffer = new byte[1024];
        try
        {
            fistream = new FileInputStream(path);
            // send the size
            long filesz = fistream.getChannel().size() ;
            ostream.writeLong(filesz) ;
            ostream.flush() ;
            // System.out.println("File sz = " + filesz) ;
            // send the file
            long done = 0 ;
            while (fistream.read(buffer) > 0)
            {
                ostream.write(buffer) ;
                ostream.flush() ;
                done += buffer.length ;
                System.out.write(bar(path,filesz,Math.min(done,filesz)).getBytes()) ;
            }
            System.out.println("") ;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
        try
        {
            fistream.close() ;
            // System.out.println("sending of file is now over") ;
            // ostream.flush() ;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
        flag.unbook() ;
    }
    public void run()
    {
        String inputline, path, way ;
        try
        {
            while(true)
            {
                System.out.write("msg >> ".getBytes()) ;
                inputline = keyRead.readLine() ;
                if(inputline == "break") break ;
                if(inputline != null && !inputline.isEmpty())
                {
                    ostream.writeUTF(inputline);
                    ostream.flush();
                    // System.out.println("just wrote " + inputline) ;
                    if(inputline.indexOf("Sending") != -1)
                    {
                        path = inputline.split(" ")[1] ;
                        way = inputline.split(" ")[2] ;
                        // System.out.println(path + " " + way) ;
                        if(way.indexOf("TCP")!=-1) sendFileTcp(path) ;
                        if(way.indexOf("UDP")!=-1) sendFileUdp(path) ;
                    }
                }
            }
            ostream.close() ;
        }
        catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
    }
}
