import java.io.*;
import java.net.* ;
class listner implements Runnable
{
    private Socket clientsocket ;
    private DataInputStream istream ;
    public listner(Socket _clientsocket)
    {
        try
        {
            clientsocket = _clientsocket ;
            istream = new DataInputStream(clientsocket.getInputStream());
        }
        catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
        new Thread(this,"bob_listener").start() ;
    }
    void recieveFileTcp(String path,int remaining)
    {
        FileOutputStream fostream = null ;
        byte[] buffer = new byte[4096];
        try
        {
            fostream = new FileOutputStream(path);
            int read = 0 ;
            while((read = istream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
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
    }
    public void run()
    {
        String responseLine ;
        try
        {
            while(true)
            {
                if((responseLine = istream.readUTF()) != null)
                {
                    System.out.println(responseLine);
                    System.out.flush() ;
                    if(responseLine == "quit") break ;
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
class sender implements Runnable
{
    private Socket clientsocket ;
    private DataOutputStream ostream ;
    private BufferedReader keyRead ;
    public sender(Socket _clientsocket)
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
            System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
        new Thread(this,"bob_sender").start() ;
    }
    void sendFileTcp(String path)
    {
        FileInputStream fistream = null ;
        byte[] buffer = new byte[4096];
        try
        {
            fistream = new FileInputStream(path);
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
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open the file" + path) ;
        }
    }
    public void run()
    {
        String inputline ;
        try
        {
            while(true)
            {
                inputline = keyRead.readLine() ;
                if(inputline == "break") break ;
                if(inputline != null && !inputline.isEmpty())
                {
                    // if(inputline.indexOf("Sending") != -1)
                    // {
                    //
                    // }
                    ostream.writeUTF(inputline);
                    ostream.flush();
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
public class Bob
{
    public static void main(String args[]) //throws Exception
    {
        Socket clientsocket = null ;
        try
        {
            clientsocket = new Socket("127.0.0.1", 10000) ;
        } catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        }catch (IOException e)
        {
           System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
        new listner(clientsocket) ;
        new sender(clientsocket) ;
        // wait till both threads are alive and then close the clientport

    }
}
