import java.io.* ;
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
        new Thread(this,"alice_listener").start() ;
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
        new Thread(this,"alice_sender").start() ;
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
                    System.out.println(inputline) ;
                    ostream.writeUTF(inputline);
                    ostream.flush();
                }
                System.out.println("here") ;
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

public class Alice
{
    public static void main(String args[])
    {
        ServerSocket echoServer = null ;
        Socket clientsocket = null ;
        try
        {
           echoServer = new ServerSocket(10000);
        }
        catch (IOException e)
        {
           System.out.println(e);
        }
        try
        {
            clientsocket = echoServer.accept() ;
        }
        catch (IOException e)
        {
           System.out.println(e);
        }
        new listner(clientsocket) ;
        new sender(clientsocket) ;
    }
}
