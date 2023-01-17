package org.concurrentLab2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Bus extends Thread {
    private int capacity = 50;
    //keeps track of how many riders are waiting to board the bus
    private int waiting = 0;
    //used to ensure that only one thread can access the waiting variable at a time
    private Semaphore mutex = new Semaphore(1);
    //semaphore object that the rider threads use to wait for the bus to arrive
    private Semaphore bus = new Semaphore(0);
    //semaphore object that the rider threads use to signal to the bus thread that they have boarded
    private Semaphore boarded = new Semaphore(0);

    //use to generate inter arrival time
    private static Random rand = new Random();
    private static final int BUS_INTERARRIVAL_TIME = 12000;

    public void depart() {
        System.out.println("Bus is departing.");
    }

    public void run() {
        while(true) {
            try {
                //generate a random number that follows an exponential distribution
                int interArrivalTime = (int) (-BUS_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                Thread.sleep(interArrivalTime);

                //Wait for the mutex semaphore to be available
                mutex.acquire();

                // n represents the number of riders that will be allowed to board the bus
                int n = Math.min(waiting, capacity);

                // Iterate n number of times to board riders to the bus
                for (int i = 0; i < n; i++) {
                    bus.release();
                    boarded.acquire();
                }
                // Decrement the waiting variable by the number of riders who boarded
                waiting = Math.max(waiting-capacity, 0);

                mutex.release();
                depart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitForRider(Rider rider) {
        try {
            // Wait for the mutex semaphore to be available
            mutex.acquire();
            waiting++;
            System.out.println("Rider "+ rider.getId() + " is waiting for the bus.");
            mutex.release();

            //Wait for the bus semaphore to be signaled by the bus thread
            bus.acquire();
            rider.board();
            //Signal the boarded semaphore to indicate to the bus thread that the rider has boarded
            boarded.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}