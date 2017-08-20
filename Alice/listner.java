import java.io.*;
import java.net.* ;
public class listner implements Runnable
{
    private Socket clientsocket ;
    private DataInputStream istream ;
    private DatagramSocket isock ;
    private mex flag ;
    public listner(Socket _clientsocket, DatagramSocket _isock, String name, mex _flag)
    {
        try
        {
            clientsocket = _clientsocket ;
            isock = _isock ;
            flag = _flag ;
            istream = new DataInputStream(clientsocket.getInputStream());
        }
        catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
        new Thread(this,name).start() ;
    }
    String bar(long total, long done)
    {
        int p = (int)((done * 10) / total) ;
        String ans = "[" ;
        for(int i = 1; i <= p ; ++i) ans = ans + "=" ;
        ans += ">" ;
        for(int i = p+1 ; i <= 10 ; ++i) ans += "_" ;
        ans += "]" + (10*p) + "%" ;
        ans += "\r" ;
        return ans ;
    }
    void recieveFileUdp(String path)
    {
        flag.book() ;
        // recieve the length of the file
        long remaining = 0 ;
        try
        {
            remaining = istream.readLong() ;
            // System.out.println("file of size si " + remaining) ;
        }
        catch(IOException e)
        {
            System.err.println("Unable to recieve the length of the file " + path) ;
        }

        // recieve the actual file
        FileOutputStream fostream = null ;
        byte[] buffer = new byte[1024] ;
        try
        {
            fostream = new FileOutputStream(path);
            DatagramPacket incoming = new DatagramPacket(buffer,buffer.length) ;
            long filesz = remaining ;
            while(remaining >0)
            {
                isock.receive(incoming) ;
                remaining -= incoming.getLength() ;
                fostream.write(incoming.getData(), 0, incoming.getLength()) ;
                System.out.write(bar(filesz,filesz-remaining).getBytes()) ;
            }
        }
        catch (IOException e)
       {
           System.err.println("Couldn't write to the file " + path) ;
       }
       try
       {
           fostream.close() ;
       }
       catch(IOException e)
       {
           System.err.println("Couldn't write to the file " + path) ;
       }
    //    System.out.println("recieve of file is now complete") ;
       flag.unbook() ;
    }
    void recieveFileTcp(String path)
    {
        flag.book() ;
        // recieve the length of the file
        long remaining = 0 ;
        try
        {
            remaining = istream.readLong() ;
            // System.out.println("file of size si " + remaining) ;
        }
        catch(IOException e)
        {
            System.err.println("Unable to recieve the length of the file " + path) ;
        }

        // recieve the actual file
        FileOutputStream fostream = null ;
        byte[] buffer = new byte[1024];
        try
        {
            fostream = new FileOutputStream(path);
            int read = 0 ;
            long filesz = remaining ;
            while(remaining >0)
            {
                read = istream.read(buffer) ;
                read = (int)Math.min((long)buffer.length,remaining) ;
                if(read <= 0) break ;
                remaining -= read;
                fostream.write(buffer, 0, read) ;
                System.out.write(bar(filesz,filesz-remaining).getBytes()) ;
            }
            System.out.println("") ;
            System.out.println("remaining = " + remaining) ;
        }
        catch (IOException e)
       {
           System.err.println("Couldn't write to the file " + path) ;
       }
       try
       {
           fostream.close() ;
       }
       catch(IOException e)
       {
           System.err.println("Couldn't write to the file " + path) ;
       }
       System.out.println("recieve of file is now complete") ;
       flag.unbook() ;
    }
    public void run()
    {
        String responseLine, path, way  ;
        try
        {
            while(true)
            {
                System.out.println("Waiting for input") ;
                responseLine = istream.readUTF() ;
                // if((responseLine = istream.readUTF()) )
                // {
                    // if(responseLine.isEmpty()) continue ;
                    System.out.println("msg >> " + responseLine);
                    System.out.flush() ;
                    if(responseLine == "quit") break ;
                // }
                if(responseLine.indexOf("Sending")!=-1)
                {
                    path = responseLine.split(" ")[1] ;
                    way = responseLine.split(" ")[2] ;
                    if(way.indexOf("TCP")!=-1) recieveFileTcp(path) ;
                    if(way.indexOf("UDP")!=-1) recieveFileUdp(path) ;
                }

            }
            istream.close() ;
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
