import java.io.*;
import java.net.* ;
public class Bob
{
    public static void main(String args[]) //throws Exception
    {
        Socket clientsocket = null ;
        BufferedReader keyRead = null ;
        DataOutputStream ostream = null ;
        DataInputStream istream = null ;
        try
        {
            clientsocket = new Socket("127.0.0.1", 10000) ;// reading from keyboard (keyRead object)
            keyRead = new BufferedReader(new InputStreamReader(System.in));// sending to client (pwrite object)
            ostream = new DataOutputStream(clientsocket.getOutputStream());
            istream = new DataInputStream(clientsocket.getInputStream());
        } catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname") ;
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: Alice") ;
        }
        try
        {
            String responseLine, inputline ;
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
            //    if((responseLine = istream.readUTF()) != null) //receive from server
            //    {
            //        System.out.println(responseLine);
            //        System.out.flush() ;
            //    }
               System.out.println("here") ;
             }
            clientsocket.close();
            ostream.close() ;
            istream.close() ;
        }
        catch (UnknownHostException e)
        {
            System.err.println("Trying to connect to unknown host: " + e);
        }
         catch (IOException e)
        {
                System.err.println("IOException:  " + e);
        }

    }
}
