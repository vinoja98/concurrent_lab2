import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class BusSimulation {
    // Maximum number of riders that can board the bus
    private static final int MAX_BOARDING = 50;
    // Semaphore to control access to the bus, allowing only one thread to access the bus at a time
    private static Semaphore bus = new Semaphore(1);
    // Semaphore to control the number of riders waiting for the bus
    private static Semaphore waitingRiders = new Semaphore(0);
    // Semaphore to block the bus until all riders have boarded
    private static Semaphore allAboard = new Semaphore(MAX_BOARDING);
    // Random number generator for inter-arrival times
    private static Random rand = new Random();
    // Mean inter-arrival time of busses (in ms)
    private static final int BUS_INTERARRIVAL_TIME = 2000;
    // Mean inter-arrival time of riders (in ms)
    private static final int RIDER_INTERARRIVAL_TIME = 1;
    private static AtomicInteger availableSpots = new AtomicInteger(MAX_BOARDING);


    public static void main(String[] args) {
        // Start the bus and rider threads
        new Thread(new Bus()).start();
       
        new Thread(new Rider()).start();    
    }
    private static class Bus implements Runnable {
        public void run() {
            while (true) {
                try {
                    // Wait for the bus to be available
                    bus.acquire();
                    // Wait for riders to arrive
                    int ridersWaiting = waitingRiders.drainPermits();
                    if (ridersWaiting == 0) {
                        // If there are no riders, the bus departs
                        depart();
                    } else {
                        // Allow the maximum number of riders to board
                        int boarding = Math.min(ridersWaiting, availableSpots.get());
                        allAboard.release(boarding);
                        availableSpots.getAndAdd(-boarding);

                        // Wait for all riders to board
                        allAboard.acquire(boarding);
                        // Release the remaining waiting riders
                        if (ridersWaiting > MAX_BOARDING) {
                            waitingRiders.release(ridersWaiting - MAX_BOARDING);
                        }
                        // Depart
                        depart();
                    }
                } catch (InterruptedException e) {
                    // Handle exception
                } finally {
                    // Release the bus for the next trip
                    bus.release();
                }
                // Wait for the next bus to arrive
                try {
                    int interArrivalTime = (int) (-BUS_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                    Thread.sleep(interArrivalTime);
                } catch (InterruptedException e) {
                    // Handle exception
                }
            }
        }
    }
    
    private static class Rider implements Runnable {
        public void run() {
            while (true) {
                try {
                    // Wait for the next rider to arrive
                    int interArrivalTime = (int) (-RIDER_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                    Thread.sleep(interArrivalTime);
                    // Rider has arrived
                    waitingRiders.release();
                    // Wait for the bus to arrive
                    allAboard.acquire();
                    availableSpots.incrementAndGet();

                    // Board the bus
                    board();
                } catch (InterruptedException e) {
                    // Handle exception
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
