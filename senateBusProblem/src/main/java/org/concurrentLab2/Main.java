package org.concurrentLab2;

public class Main {
//    private static Random rand = new Random();
//    // Mean inter-arrival time of busses (in ms)
//    private static final int BUS_INTERARRIVAL_TIME = 1200000;
//    // Mean inter-arrival time of riders (in ms)
//    private static final int RIDER_INTERARRIVAL_TIME = 5;

    public static void main(String[] args) throws InterruptedException {
        Bus bus = new Bus();
        Rider[] riders = new Rider[52];

//        for (int i = 0; i < 52; i++) {
//            int interArrivalTime = (int) (-RIDER_INTERARRIVAL_TIME * Math.log(rand.nextDouble()));
//            TimeUnit.SECONDS.sleep(interArrivalTime);
//            riders[i] = new Rider(i+1, bus);
//            riders[i].start();
//        }
        RiderHandler riderHandler = new RiderHandler(riders, bus, 52);
        riderHandler.start();
        bus.start();
    }
}