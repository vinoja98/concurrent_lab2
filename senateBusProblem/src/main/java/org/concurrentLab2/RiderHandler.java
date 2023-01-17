package org.concurrentLab2;

public class RiderHandler extends Thread {

    Rider[] riders;
    Bus bus;
    int limit;

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
