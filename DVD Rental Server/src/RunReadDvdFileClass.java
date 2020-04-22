
/**
 * Write a description of class runSales here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RunReadDvdFileClass
{
    public static void main(String [] args)
    {
        ReadDvdFileClass runRdfc = new ReadDvdFileClass();
        runRdfc.openFiles();
        runRdfc.readAndWrite();
        runRdfc.closeFiles();
    }
}