import java.io.*;
import java.net.* ;
public class listner implements Runnable
{
    private Socket clientsocket ;
    private DataInputStream istream ;
    private mex flag ;
    public listner(Socket _clientsocket, String name, mex _flag)
    {
        try
        {
            clientsocket = _clientsocket ;
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
    void recieveFileTcp(String path)
    {
        flag.book() ;
        // recieve the length of the file
        long remaining = 0 ;
        try
        {
            remaining = istream.readLong() ;
            System.out.println("file of size si " + remaining) ;
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
            while(remaining >0 && (read = istream.read(buffer, 0, (int)Math.min(buffer.length, remaining)))!=-1)
            {
                remaining -= read;
                fostream.write(buffer, 0, read) ;
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
                if((responseLine = istream.readUTF()) != null)
                {
                    // if(responseLine.isEmpty()) continue ;
                    System.out.println(responseLine);
                    System.out.flush() ;
                    if(responseLine == "quit") break ;
                }
                if(responseLine.indexOf("Sending")!=-1)
                {
                    path = responseLine.split(" ")[1] ;
                    way = responseLine.split(" ")[2] ;
                    if(way.indexOf("TCP")!=-1) recieveFileTcp(path) ;
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
