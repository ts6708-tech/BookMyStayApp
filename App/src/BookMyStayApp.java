
import java.util.*;

// Room class (Domain Model)
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory class (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-Only Logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Validation: only show available rooms
            if (available > 0 && roomData.containsKey(type)) {
                Room room = roomData.get(type);

                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println("===========================");
            }
        }
    }
}

// ONLY public class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Step 2: Create Room Data
        Map<String, Room> roomData = new HashMap<>();

        roomData.put("Single",
                new Room("Single", 2000,
                        Arrays.asList("WiFi", "TV")));

        roomData.put("Double",
                new Room("Double", 3500,
                        Arrays.asList("WiFi", "TV", "AC")));

        roomData.put("Suite",
                new Room("Suite", 6000,
                        Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 3: Search Service
        SearchService searchService = new SearchService(inventory, roomData);

        // Step 4: Perform Search (Read-Only)
        searchService.searchAvailableRooms();
=======
import java.util.HashMap;
import java.util.Map;
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with initial availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found.");
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v3.1");
        System.out.println("=======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        System.out.println("\nUpdating Double Room availability...\n");

        // Update availability
        inventory.updateAvailability("Double Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully.");




    }
}