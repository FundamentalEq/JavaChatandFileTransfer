import java.io.*;
import java.net.* ;
public class Bob
{
    public static void main(String args[]) //throws Exception
    {
        Socket clientsocket = null ;
        mex flag = new mex() ;
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
        new listner(clientsocket,"bob_listener",flag) ;
        new sender(clientsocket,"bob_sender",flag) ;
        // wait till both threads are alive and then close the clientport

    }
}
