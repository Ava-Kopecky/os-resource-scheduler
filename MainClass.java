import java.io.*;
import java.util.Hashtable;

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
            System.out.println(e);
        }
        sectors[sector].setLength(0);
        sectors[sector].append(data);
    }
    void read(int sector, StringBuffer data)   // call sleep
    {
        try {
            Thread.sleep(DISK_DELAY); // sleeps for 80 ms for gradescope auto grader
        } catch (InterruptedException e) {
            System.out.println(e);
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
            writer = new BufferedWriter(new FileWriter("PRINTER" + id));
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
            System.out.println(e);
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
    String fileToPrint;

    PrintJobThread(String fileToPrint)
    {
        this.fileToPrint = fileToPrint;
    }

    public void run()
{
    StringBuffer stringFile = new StringBuffer(fileToPrint);
    FileInfo data = MainClass.directory.lookup(stringFile);

    if (data != null) {
        int dN = data.diskNumber;
        int sS = data.startingSector;
        int fL = data.fileLength;

        int printerIndex = MainClass.printerManager.request();

        for (int i = sS; i < sS + fL; i++) {
            MainClass.disks[dN].read(i, line);
            MainClass.printers[printerIndex].print(line);
        }
        MainClass.printerManager.release(printerIndex);
    }

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
    private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
        String validFileName = fileName.toString();
        T.put(validFileName, file);
    }

    FileInfo lookup(StringBuffer fileName)
    {
        String validFileName = fileName.toString();
        FileInfo value = T.get(validFileName);
        return value;
    }
}

class ResourceManager
{
    boolean[] isFree;

    ResourceManager(int numberOfItems) {
        isFree = new boolean[numberOfItems];
        for (int i = 0; i < isFree.length; ++i)
            isFree[i] = true;
    }

    synchronized int request() {
        while (true) {
            for (int i = 0; i < isFree.length; ++i) {
                if (isFree[i]) {
                    isFree[i] = false;
                    return i;
                }
            }

            try {
                wait();   // block until someone releases resource
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void release(int index) {
        isFree[index] = true;
        notify();  // let a blocked thread run
    }
}

class DiskManager extends ResourceManager {
    int[] nextFreeSector;
    DiskManager(int numberOfDisks) {
        super(numberOfDisks);
        nextFreeSector = new int[numberOfDisks];
    }
}

class PrinterManager extends ResourceManager {
    PrinterManager(int numberOfPrinters) {
        super(numberOfPrinters);
    }
}

class UserThread
    extends Thread
{
    StringBuffer currentFileName;
    int startSector;
    int fileLength;
    int currentDisk;
    int id;
    boolean currentlyWriting = false;
    BufferedReader reader;
    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
        this.id = id;
    }

    public void run()
    {
        try {
            try {
                reader = new BufferedReader(new FileReader("users/USER" + id));
            } catch (IOException e) {
                reader = new BufferedReader(new FileReader("USER" + id));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".save")) {
                    currentFileName = new StringBuffer(line.substring(6).trim());
                    currentDisk = MainClass.diskManager.request();
                    startSector = MainClass.diskManager.nextFreeSector[currentDisk];
                    fileLength = 0;
                    currentlyWriting = true;
                    continue;
                } else if (line.startsWith(".end")) {
                    FileInfo info = new FileInfo();
                    info.diskNumber = currentDisk;
                    info.startingSector = startSector;
                    info.fileLength = fileLength;
                    MainClass.directory.enter(currentFileName, info);
                    MainClass.diskManager.release(currentDisk); 
                    currentlyWriting = false;
                    continue;
                } else if (line.startsWith(".print")) {
                    String fileToPrint = line.substring(7).trim();

                    PrintJobThread job = new PrintJobThread(fileToPrint);
                    job.start();
                    try {
                        job.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                if (currentlyWriting == true) {
                    MainClass.disks[currentDisk].write(MainClass.diskManager.nextFreeSector[currentDisk], new StringBuffer(line));
                    MainClass.diskManager.nextFreeSector[currentDisk]++;
                    fileLength++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class MainClass
{
    static Disk[] disks;
    static Printer[] printers;
    static DirectoryManager directory;
    static DiskManager diskManager;
    static PrinterManager printerManager;

    public static void main(String args[])
{
    int numUsers = Integer.parseInt(args[0].substring(1));
    int numDisks = Integer.parseInt(args[1].substring(1));
    int numPrinters = Integer.parseInt(args[2].substring(1));
    directory = new DirectoryManager();
    diskManager = new DiskManager(numDisks);
    printerManager = new PrinterManager(numPrinters);

    disks = new Disk[numDisks];
    for (int i = 0; i < numDisks; i++) {
        disks[i] = new Disk();
    }

    printers = new Printer[numPrinters];
    for (int i = 0; i < numPrinters; i++) {
        printers[i] = new Printer(i);
    }

    UserThread[] users = new UserThread[numUsers];
    for (int i = 0; i < numUsers; i++) {
        users[i] = new UserThread(i);
        users[i].start();
    }

    for (int i = 0; i < numUsers; i++) {
        try {
            users[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    System.out.println("*** 141 OS Simulation ***");
}
}

