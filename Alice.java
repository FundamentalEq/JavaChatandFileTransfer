import java.io.* ;
import java.net.* ;
public class Alice
{
    public static void main(String args[])
    {
        ServerSocket echoServer = null ;
        Socket clientsocket = null ;
        BufferedReader keyRead = null ;
        DataInputStream istream = null ;
        try
        {
           echoServer = new ServerSocket(10000);
           keyRead = new BufferedReader(new InputStreamReader(System.in));// sending to client (pwrite object)
        }
        catch (IOException e)
        {
           System.out.println(e);
        }
        try
        {
           clientsocket = echoServer.accept() ;
           istream = new DataInputStream(clientsocket.getInputStream()) ;

           String responseLine, inputline ;
           while(true)
           {
            //   inputline = keyRead.readLine() ;  // keyboard reading
            //   if(inputline == "break") break ;
            //   if(inputline != null && !inputline.isEmpty())
            //   {
                  //   pwrite.println(inputline);
                //   pwrite.flush();
            //   }
            // responseLine = istream.readUTF() ;
              if((responseLine = istream.readUTF()) != null)
              {
                  System.out.println(responseLine);
                  System.out.flush() ;
              }
              if(responseLine == "aak") break ;
            }
           clientsocket.close();
           istream.close() ;

        }catch (IOException e)
        {
           System.out.println(e);
        }
    }
}
