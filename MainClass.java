class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 2048;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];
    Disk()
    {
    }
    void write(int sector, StringBuffer data)  // call sleep
    {
    }
    void read(int sector, StringBuffer data)   // call sleep
    {
    }
}

class Printer
    // extends Thread
{
    Printer(int id)
    {
    }

    void print(StringBuffer data)  // call sleep
    {
    }
}

class PrintJobThread
    extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer

    PrintJobThread(String fileToPrint)
    {
    }

    public void run()
    {
    }
}

class FileInfo
{
    int diskNumber;
    int startingSector;
    int fileLength;
}

class DirectoryManager
{
    // private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
    }

    FileInfo lookup(StringBuffer fileName)
    {
        return null;
    }
}

class ResourceManager
{
}

class DiskManager
{
}

class PrinterManager
{
}

class UserThread
    extends Thread
{
    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
    }

    public void run()
    {
    }
}


public class MainClass
{
    public static void main(String args[])
    {
        for (int i=0; i<args.length; ++i)
            System.out.println("Args[" + i + "] = " + args[i]);
            
        System.out.println("*** 141 OS Simulation ***");
    }
}

