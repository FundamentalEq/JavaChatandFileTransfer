
public class mex
{
    private boolean flag = false ;
    public synchronized void book()
    {
        if(flag)
        {
            try
            {
                wait() ;
            }
            catch(InterruptedException e)
            {
                e.printStackTrace() ;
            }
        }
        flag = true ;
    }
    public synchronized void unbook()
    {
        flag = false ;
        notify() ;
    }
}
