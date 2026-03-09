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
    StringBuffer currentFileName;
    int startSector;
    int fileLength;
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
            reader = new BufferedReader(new FileReader("USER" + id));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".save")) {
                    currentFileName = new StringBuffer(line.substring(6));
                    startSector = nextFreeSector;
                    fileLength = 0;
                    currentlyWriting = true;
                    continue;
                } else if (line.startsWith(".end")) {
                    FileInfo info = new FileInfo();
                    info.diskNumber = 0; // CHANGE WHEN DOING HW9 CAUSE HW1 ONYL HAS 1 DISK!
                    info.startingSector = startSector;
                    info.fileLength = fileLength;
                    directory.enter(currentFileName, info);
                    currentlyWriting = false;
                    continue;
                } else if (line.startsWith(".print")) {
                    String fileToPrint = line.substring(7).trim();
                    PrintJobThread job = new PrintJobThread(fileToPrint);
                    job.start();
                    continue;
                }

                if (currentlyWriting == true) {
                    disk.write(nextFreeSector, new StringBuffer(line));
                    nextFreeSector++;
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
    public static void main(String args[])
    {
        for (int i=0; i<args.length; ++i)
            System.out.println("Args[" + i + "] = " + args[i]);
            
        System.out.println("*** 141 OS Simulation ***");
    }
}

