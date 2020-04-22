public class RunReadRentalFileClass
{
    public static void main(String[] args)
    {
        ReadRentalFileClass runRrfc = new ReadRentalFileClass();
        runRrfc.openFiles();
        runRrfc.readAndWrite();
        runRrfc.closeFiles();
    }
}
