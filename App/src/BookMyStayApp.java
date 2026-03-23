import java.util.*;

// Reservation (from UC5)
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

// Booking Service (Allocation Logic)
class BookingService {

    private Queue<Reservation> queue;
    private Inventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → assigned IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(Queue<Reservation> queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    // Process booking
    public void processBookings() {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO

            System.out.println("\nProcessing: " + r.guestName);

            // Check availability
            if (inventory.getAvailability(r.roomType) > 0) {

                // Generate unique room ID
                String roomId;
                do {
                    roomId = r.roomType + "-" + UUID.randomUUID().toString().substring(0, 5);
                } while (allocatedRoomIds.contains(roomId));

                // Store ID
                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(r.roomType, k -> new HashSet<>())
                        .add(roomId);

                // Reduce inventory
                inventory.reduceRoom(r.roomType);

                System.out.println("Booking CONFIRMED for " + r.guestName);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + r.guestName + " (No rooms available)");
            }
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Queue (UC5)
        Queue<Reservation> queue = new LinkedList<>();
        queue.add(new Reservation("Alice", "Single"));
        queue.add(new Reservation("Bob", "Single"));
        queue.add(new Reservation("Charlie", "Suite"));

        // Step 2: Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Suite", 1);

        // Step 3: Booking Service
        BookingService service = new BookingService(queue, inventory);

        // Step 4: Process bookings
        service.processBookings();
    }
}