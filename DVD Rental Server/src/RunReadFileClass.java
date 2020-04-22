
/**
 * Write a description of class runSales here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RunReadFileClass
{
    public static void main(String [] args)
    {
        ReadFileClass runRfc = new ReadFileClass();
        runRfc.openFiles();
        runRfc.readAndWrite();
        runRfc.closeFiles();
    }
}