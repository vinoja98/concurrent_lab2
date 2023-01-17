package senateBusProblem;

public class Rider extends Thread {
    private int id;
    private Bus bus;
    public Rider(int id, Bus bus) {
        this.id = id;
        this.bus = bus;
    }

    @Override
    public long getId() {
        return id;
    }

    public void run() {
        bus.waitForRider(this);

    }
    public void board() {
        System.out.println("Rider " + this.getId() + " is boarding the bus.");
    }
}
