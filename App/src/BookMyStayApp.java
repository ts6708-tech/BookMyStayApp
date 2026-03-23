import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, -1); // -1 = invalid type
    }

    public void reduceRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

// Validator
class BookingValidator {

    public static void validate(Reservation r, Inventory inventory)
            throws InvalidBookingException {

        // Validate room type
        if (inventory.getAvailability(r.roomType) == -1) {
            throw new InvalidBookingException("Invalid Room Type: " + r.roomType);
        }

        // Validate availability
        if (inventory.getAvailability(r.roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + r.roomType);
        }

        // Validate guest name
        if (r.guestName == null || r.guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }
    }
}

// Booking Service
class BookingService {

    private Inventory inventory;

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(Reservation r) {
        try {
            // Validation (Fail Fast)
            BookingValidator.validate(r, inventory);

            // If valid → proceed
            inventory.reduceRoom(r.roomType);

            System.out.println("Booking SUCCESS for " + r.guestName +
                    " | Room: " + r.roomType);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Suite", 0);

        BookingService service = new BookingService(inventory);

        // Test cases
        service.bookRoom(new Reservation("Alice", "Single"));   // valid
        service.bookRoom(new Reservation("Bob", "Suite"));      // no availability
        service.bookRoom(new Reservation("", "Single"));        // invalid name
        service.bookRoom(new Reservation("Charlie", "Deluxe")); // invalid type
    }
}