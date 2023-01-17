import java.util.Random;
import java.util.concurrent.Semaphore;

public class BusSimulation {
    // Maximum number of riders that can board the bus
    private static int waiting = 0;
    // Semaphore to control access to the bus, allowing only one thread to access the bus at a time
    private static Semaphore mutex = new Semaphore(1);
    // Semaphore to control the number of riders waiting for the bus
    private static Semaphore bus = new Semaphore(0);
    // Semaphore to block the bus until all riders have boarded
    private static Semaphore boarded = new Semaphore(0);
    // Random number generator for inter-arrival times
    private static Random rand = new Random();
    // Mean inter-arrival time of busses (in ms)
    private static final int BUS_INTERARRIVAL_TIME = 1200000;
    // Mean inter-arrival time of riders (in ms)
    private static final int RIDER_INTERARRIVAL_TIME = 30000;

    public static void main(String[] args) {
        // Start the bus and rider threads
        new Thread(new Bus()).start();
       
        new Thread(new Rider()).start();    
    }
    private static class Bus implements Runnable {
        public void run() {
            while (true) {
                try {
                    mutex.acquire();
                    int n = Math.min(waiting, 50);
                    for(int i=0;i<n;i++){
                        bus.release();
                        boarded.acquire();
                    }
                                        
                    waiting = Math.max(waiting-50, 0);
                    mutex.release();                  
                    depart();
                } catch (InterruptedException e) {
                    // Handle exception
                // Wait for the next bus to arrive
                try {
                    int interArrivalTime = (int) (-RIDER_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                    Thread.sleep(interArrivalTime);
                } catch (InterruptedException e1) {
                    // Handle exception
                }
            }
        }
    }
    }
    
    private static class Rider implements Runnable {
        public void run() {
            while (true) {
                try {
                    mutex.acquire();
                        waiting += 1;
                    mutex.release();                 
                    bus.acquire();
                    board();
                    boarded.release();
                } catch (InterruptedException e) {
                    // Handle exception
                
                try {
                    int interArrivalTime = (int) (-BUS_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                    Thread.sleep(interArrivalTime);
                } catch (InterruptedException e1) {
                    // Handle exception
                }
             }
            }
        }
    }
    private static void depart() {
        System.out.println("The bus is departing.");
    }
    
    private static void board() {
        System.out.println("A rider has boarded the bus.");
    }

}
  
