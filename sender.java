import java.io.*;
import java.net.* ;
public class sender implements Runnable
{
    private Socket clientsocket ;
    private DataOutputStream ostream ;
    private BufferedReader keyRead ;
    public sender(Socket _clientsocket, String name)
    {
        try
        {
            clientsocket = _clientsocket ;
            ostream = new DataOutputStream(clientsocket.getOutputStream());
            keyRead = new BufferedReader(new InputStreamReader(System.in));
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

    void sendFileTcp(String path)
    {
        System.out.println("Sending file : " + path) ;
        FileInputStream fistream = null ;
        byte[] buffer = new byte[4096];
        try
        {
            fistream = new FileInputStream(path);
            // send the size
            long filesz = fistream.getChannel().size() ;
            ostream.writeLong(filesz) ;
            ostream.flush() ;
            System.out.println("File sz = " + filesz) ;
            // send the file
            while (fistream.read(buffer) > 0)
            {
                ostream.write(buffer) ;
                ostream.flush() ;
            }
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
        try
        {
            fistream.close() ;
            System.out.println("sending of file is now over") ;
            ostream.flush() ;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
    }
    public void run()
    {
        String inputline, path, way ;
        try
        {
            while(true)
            {
                inputline = keyRead.readLine() ;
                if(inputline == "break") break ;
                if(inputline != null && !inputline.isEmpty())
                {
                    ostream.writeUTF(inputline);
                    ostream.flush();
                    if(inputline.indexOf("Sending") != -1)
                    {
                        path = inputline.split(" ")[1] ;
                        way = inputline.split(" ")[2] ;
                        System.out.println(path + " " + way) ;
                        if(way.indexOf("TCP")!=-1) sendFileTcp(path) ;
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