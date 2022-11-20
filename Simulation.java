
/**
 * Natthawee Koengfak 6213125
 * Nicharee Chalermsuksri 6213198
 */
import java.io.*;
import static java.lang.Integer.*;
import java.util.*;
import java.util.concurrent.*;

class group {

    String name, destination;
    int seat, transaction;

    group(String n, int s) {
        name = n;
        seat = s;
    }

    group(String n, int s, String d, int t) {
        name = n;
        seat = s;
        destination = d;
        transaction = t;
    }

    int getBound() {
        return destination.equals("A") ? 0 : 1;
    }
}

class Bus {

    String name;
    int available;
    private final ArrayList<group> groups = new ArrayList<>();

    Bus(String n, int ms) {
        //       ++count;
        name = n;
        available = ms;
    }

    void add(String n, int s) {
        groups.add(new group(n, s));
    }

    void print() {
        System.out.printf("\n%s >> %-3s :", Thread.currentThread().getName(), name);
        for (int i = 0; i < groups.size(); i++) {

            System.out.printf(" %-20s (%2d seats)%s ",
                    groups.get(i).name,
                    groups.get(i).seat,
                    i < groups.size() - 1 ? "," : " ");
        }
    }

}

class BusLine {

    String destination;
    int maxSeat;
    ArrayList<Bus> busses;
    Bus currentBus;

    BusLine(String d, int ms) {
        maxSeat = ms;
        destination = d;
        currentBus = new Bus(destination + "0", maxSeat);
        busses = new ArrayList<>();
    }

    int count() {
        return busses.size() + (currentBus.available == maxSeat ? 0 : 1);
    }

    synchronized public void allocateBus(group currentGroup) {
        //   synchronized (this) {
        // System.out.printf("--%s %d %d--\n", Thread.currentThread().getName(), currentBus.available, currentGroup.seat);
        int fit = min(currentBus.available, currentGroup.seat);
        currentBus.add(currentGroup.name, fit);
        // currentBus.available = Group.seat -= fit;
        currentBus.available -= fit;
        currentGroup.seat -= fit;
        System.out.printf("%s >> Transaction %2d : %-20s (%2d seats) bus %s\n",
                Thread.currentThread().getName(),
                currentGroup.transaction,
                currentGroup.name,
                fit,
                currentBus.name);
        if (currentBus.available == 0) {

            busses.add(currentBus);
            currentBus = new Bus(destination + busses.size(), maxSeat);
        }
        //   }

    }

    void cleanUp() {
        if (currentBus.available != maxSeat) {
            busses.add(currentBus);
            currentBus.available = 0;
        }
        System.out.printf("\n%s >> ===== %s Bound =====", Thread.currentThread().getName(), destination.equals("A") ? "Airport" : "City");
        for (Bus i : busses) {
            i.print();
        }
        System.out.println();
    }
}

class TicketCounter extends Thread implements Runnable {

    static ArrayList<BusLine> BusLines = new ArrayList<>();
    private static int maxSeats, checkpoint;
    private static CyclicBarrier barrier = new CyclicBarrier(3);
    static int printedCount = 0;

    static void eiei(int ms, int cp) {

        maxSeats = ms;
        checkpoint = cp;
        BusLines.add(new BusLine("A", maxSeats));
        BusLines.add(new BusLine("C", maxSeats));
    }
    private Scanner scan, key = new Scanner(System.in);
    private int count;

    TicketCounter(String name) {

        super(name);
        String fileName = name + ".txt";
        count = 0;
        boolean founded = false;
        do {
            founded = true;
            try {
                scan = new Scanner(new File(fileName));
            } catch (FileNotFoundException e) {
                System.err.println(e);
                System.out.printf("%s >> Enter new file's name of %s.txt :\n", Thread.currentThread().getName(), name);
                fileName = key.nextLine();
                founded = false;
            }
        } while (!founded);

    }

    @Override
    public void run() {
        while (scan.hasNext()) {
            //System.out.println(Thread.currentThread().getName() + " " + scan.nextLine());
            String[] buf = scan.nextLine().split(",");
            group currentGroup = new group(buf[1].trim(), Integer.parseInt(buf[2].trim()), buf[3].trim(), Integer.parseInt(buf[0].trim()));
            //   while (currentGroup.seat > 0) 
            {
                Do(currentGroup);
            }
        }
    }

    private void await() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            System.err.println(Thread.currentThread().getName() + ex);
        }
    }

    private void Do(group currentGroup) {

        if (++count == checkpoint) {
            // System.out.println(Thread.currentThread().getName());
            await();
            printBarrier();
        }

        while (currentGroup.seat > 0) //  synchronized (BusLines) 
        {
            BusLines.get(currentGroup.getBound()).allocateBus(currentGroup);
        }
    }

    synchronized private void printBarrier() {
        if (printedCount++ == 0) {
            System.out.printf("\n%s >> %2d airport-bound have been allocated",
                    Thread.currentThread().getName(), BusLines.get(0).count());
            System.out.printf("\n%s >> %2d city-bound have been allocated\n\n",
                    Thread.currentThread().getName(), BusLines.get(1).count());
        }
        await();
    }
}

class Simulation {

    static void input() {
        ////////////////////////////////// from static //////////////////////////////////
        {

            System.out.println();
            Scanner key = new Scanner(System.in);
            System.out.println(Thread.currentThread().getName()
                    + " >> Enter max seats :");
            int maxSeats = key.nextInt();
            System.out.println(Thread.currentThread().getName()
                    + " >> Enter checkpoint :");
            int checkpoint = key.nextInt();
            TicketCounter.eiei(maxSeats, checkpoint);
            System.out.println();
        }
        //////////////////////////////////////////
    }

    public static void main(String[] args) {
        ArrayList<TicketCounter> tcs = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            tcs.add(new TicketCounter("T" + i));
        }
        input();
        for (TicketCounter i : tcs) {
            i.start();
        }
        for (TicketCounter i : tcs) {
            try {
                i.join();
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        for (BusLine i : TicketCounter.BusLines) {
            i.cleanUp();
        }

    }
}
