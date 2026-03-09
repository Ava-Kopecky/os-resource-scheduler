class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 2048;
    static final int DISK_DELAY = 80;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];
    Disk() 
    {
        for (int i = 0; i < NUM_SECTORS; i++) {
            sectors[i] = new StringBuffer();
        }
    }
    void write(int sector, StringBuffer data)  // call sleep
    {
        try {
            Thread.sleep(DISK_DELAY); // sleeps for 80 ms for gradescoep auto grader
        } catch (InterruptedException e) {
            println(e);
        }
        sectors[sector].setLength(0);
        sectors[sector].append(data);
    }
    void read(int sector, StringBuffer data)   // call sleep
    {
        try {
            Thread.sleep(DISK_DELAY); // sleeps for 80 ms for gradescope auto grader
        } catch (InterruptedException e) {
            println(e);
        }
        data.setLength(0);
        data.append(sectors[sector]);
    }
}

class Printer
    // extends Thread
{
    static final int PRINT_DELAY = 275;
    int id;
    BufferedWriter writer;
    Printer(int id)
    {
        this.id = id;
        try {
            writer = new BufferedWriter(new FileWriter("PRINTER" + id, true));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void print(StringBuffer data)  // call sleep
    {
        try {
            Thread.sleep(PRINT_DELAY);
        } catch (InterruptedException e) {
            println(e);
        }

        try {
            writer.write(data.toString());
            writer.newLine();
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

