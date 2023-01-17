package org.concurrentLab2;

import java.util.Random;

public class RiderHandler extends Thread {

    Rider[] riders;
    Bus bus;
    int limit;
    private static Random rand = new Random();
    private static final int RIDER_INTERARRIVAL_TIME = 30;

    public RiderHandler(Rider[] riders, Bus bus, int limit) {
        this.riders = riders;
        this.bus = bus;
        this.limit = limit;
    }

    public void run() {
        for (int i = 0; i < limit; i++) {
            riders[i] = new Rider(i+1, bus);
            riders[i].start();
            try {
                //generate a random number that follows an exponential distribution
                int interArrivalTime = (int) (-RIDER_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
                Thread.sleep(interArrivalTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
