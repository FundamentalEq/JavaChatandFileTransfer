import java.io.* ;
import java.net.* ;
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
        new listner(clientsocket,"alice_listener") ;
        new sender(clientsocket,"alice_sender") ;
    }
}
