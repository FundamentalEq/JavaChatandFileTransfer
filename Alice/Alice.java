import java.io.* ;
import java.net.* ;
public class Alice
{
    public static void main(String args[])
    {
        ServerSocket echoServer = null ;
        Socket clientsocket = null ;
        DatagramSocket udpclientsocket = null ;
        int port = 9876 ;
        mex flag = new mex() ;
        try
        {
           echoServer = new ServerSocket(10000) ;
           udpclientsocket = new DatagramSocket(port) ;
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
        new listner(clientsocket,udpclientsocket,"alice_listener",flag) ;
        new sender(clientsocket,"alice_sender",flag,9875) ;
    }
}
