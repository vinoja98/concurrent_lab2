package org.concurrentLab2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bus bus = new Bus();
        Rider[] riders = new Rider[52];
        RiderHandler riderHandler = new RiderHandler(riders, bus, 52);
        riderHandler.start();
        bus.start();
    }
}