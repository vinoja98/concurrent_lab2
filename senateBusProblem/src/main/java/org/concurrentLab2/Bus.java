package org.concurrentLab2;

import java.util.concurrent.Semaphore;

public class Bus extends Thread {
    private int capacity = 50;
    private int waiting = 0;
    private Semaphore mutex = new Semaphore(1);
    private Semaphore bus = new Semaphore(0);
    private Semaphore boarded = new Semaphore(0);
    public void depart() {
        System.out.println("Bus is departing.");
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(50000);
                mutex.acquire();
                int n = Math.min(waiting, capacity);
                for (int i = 0; i < n; i++) {
                    bus.release();
                    boarded.acquire();
                }
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
            mutex.acquire();
            waiting++;
            System.out.println("Rider "+ rider.getId() + " is waiting for the bus.");
            mutex.release();
            bus.acquire();
            rider.board();
            boarded.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}