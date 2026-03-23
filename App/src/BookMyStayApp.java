import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// Inventory (Serializable)
class Inventory implements Serializable {
    Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public void display() {
        System.out.println("\nInventory State:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " → " + rooms.get(type));
        }
    }
}

// Wrapper class (to save full state)
class SystemState implements Serializable {
    Inventory inventory;
    List<Reservation> history;

    public SystemState(Inventory inventory, List<Reservation> history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state SAVED to file.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("\nSystem state LOADED from file.");
            return state;

        } catch (Exception e) {
            System.out.println("\nNo previous data found. Starting fresh.");
            return null;
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Try loading previous state
        SystemState state = PersistenceService.load();

        Inventory inventory;
        List<Reservation> history;

        if (state != null) {
            // Restore
            inventory = state.inventory;
            history = state.history;
        } else {
            // Fresh start
            inventory = new Inventory();
            history = new ArrayList<>();

            inventory.addRoom("Single", 2);
            inventory.addRoom("Suite", 1);

            history.add(new Reservation("RES101", "Alice", "Single"));
            history.add(new Reservation("RES102", "Bob", "Suite"));
        }

        // Step 2: Display current state
        inventory.display();

        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }

        // Step 3: Save state before exit
        PersistenceService.save(new SystemState(inventory, history));
    }
}