import java.util.concurrent.Semaphore;

public class SenateBus {
    private Semaphore mutex;//protect the shared variables waitingRiders and bus from concurrent access
    private Semaphore bus;//block riders from boarding the bus until it arrives
    private Semaphore boarded;//block the bus from departing until all riders have boarded
    private int waitingRiders;

    public SenateBus() {
        mutex = new Semaphore(1);
        bus = new Semaphore(0);
        boarded = new Semaphore(0);
        waitingRiders = 0;
    }

    public void boardBus() throws InterruptedException {
        mutex.acquire();
        waitingRiders++;
        if (waitingRiders > 50) {
            waitingRiders--;
            mutex.release();
            return;
        }
        mutex.release();

        bus.acquire();
        boarded.release();
    }

    public void depart() throws InterruptedException {
        mutex.acquire();
        if (waitingRiders == 0) {
            mutex.release();
            return;
        }
        mutex.release();

        for (int i = 0; i < 50; i++) {
            bus.release();
            boarded.acquire();
        }

        mutex.acquire();
        waitingRiders -= 50;
        mutex.release();
    }
    public static void main(String[] args) {
        SenateBus bus = new SenateBus();
    
        // Create rider threads
        for (int i = 0; i < 100; i++) {
            Thread rider = new Thread(() -> {
                try {
                    bus.boardBus();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            rider.start();
        }
    
        // Create bus thread
        Thread busThread = new Thread(() -> {
            while (true) {
                try {
                    bus.depart();
                    Thread.sleep(10000); // wait for 10 seconds before departing again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        busThread.start();
    }
}

