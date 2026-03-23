import java.util.*;

// Reservation
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

// Inventory
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public void increaseRoom(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.reservationId, r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }
}

// Cancellation Service
class CancellationService {

    private Inventory inventory;
    private BookingHistory history;

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public CancellationService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        Reservation r = history.getReservation(reservationId);

        // Validation
        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation not found");
            return;
        }

        // Step 1: Push room ID to stack
        releasedRoomIds.push(r.roomId);

        // Step 2: Restore inventory
        inventory.increaseRoom(r.roomType);

        // Step 3: Remove booking
        history.removeReservation(reservationId);

        System.out.println("Cancellation SUCCESS for " + r.guestName);
        System.out.println("Released Room ID: " + r.roomId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + releasedRoomIds);
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 0);

        BookingHistory history = new BookingHistory();

        // Simulate confirmed booking (UC6)
        Reservation r1 = new Reservation("RES101", "Alice", "Single", "Single-123");
        history.addReservation(r1);

        CancellationService service = new CancellationService(inventory, history);

        // Valid cancellation
        service.cancelBooking("RES101");

        // Invalid cancellation
        service.cancelBooking("RES101");

        service.showRollbackStack();
    }
}