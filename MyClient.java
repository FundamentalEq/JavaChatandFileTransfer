import java.io.*;
import java.net.*;
public class MyClient
{
    public static void main(void)
    {
        Socket ClientSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try
        {
            ClientSocket = new Socket("127.0.0.1", 10000);
            os = new DataOutputStream(ClientSocket.getOutputStream());
            is = new DataInputStream(ClientSocket.getInputStream());
        } catch (UnknownHostException e)
        {
             System.err.println("Don't know about host: hostname");
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
        if (ClientSocket != null && os != null && is != null)
        {
            System.err.println("Unable to setup Client.\nProgram existing.") ;
        }

        if (ClientSocket != null && os != null && is != null) {
            try {
// The capital string before each colon has a special meaning to SMTP
// you may want to read the SMTP specification, RFC1822/3
                os.writeBytes("HELO\n");
                os.writeBytes("MAIL From: k3is@fundy.csd.unbsj.ca\n");
                os.writeBytes("RCPT To: k3is@fundy.csd.unbsj.ca\n");
                os.writeBytes("DATA\n");
                os.writeBytes("From: k3is@fundy.csd.unbsj.ca\n");
                os.writeBytes("Subject: testing\n");
                os.writeBytes("Hi there\n"); // message body
                os.writeBytes("\n.\n");
        os.writeBytes("QUIT");
// keep on reading from/to the socket till we receive the "Ok" from SMTP,
// once we received that then we want to break.
                String responseLine;
                while ((responseLine = is.readLine()) != null) {
                    System.out.println("Server: " + responseLine);
                    if (responseLine.indexOf("Ok") != -1) {
                      break;
                    }
                }
// clean up:
// close the output stream
// close the input stream
// close the socket
        os.close();
                is.close();
                ClientSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
}
