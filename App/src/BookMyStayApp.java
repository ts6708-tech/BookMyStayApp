import java.util.*;

// Reservation
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Inventory (Thread-Safe)
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String type) {
        int available = rooms.getOrDefault(type, 0);

        if (available > 0) {
            rooms.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }
}

// Booking Processor (Runnable)
class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private Inventory inventory;

    public BookingProcessor(Queue<Reservation> queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation r;

            // synchronized access to queue
            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                r = queue.poll();
            }

            // critical section: allocation
            boolean success = inventory.allocateRoom(r.roomType);

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " booked room for " + r.guestName);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED for " + r.guestName);
            }
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        // Shared queue
        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single"));
        queue.add(new Reservation("Bob", "Single"));
        queue.add(new Reservation("Charlie", "Single"));

        // Shared inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2); // only 2 rooms

        // Create threads
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        // Start threads
        t1.start();
        t2.start();
    }
}