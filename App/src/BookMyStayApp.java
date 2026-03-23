import java.util.*;

// Reservation class (confirmed booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("Added to history: " + r.getReservationId());
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    public void showAllBookings(List<Reservation> history) {
        System.out.println("\n--- Booking History ---");

        for (Reservation r : history) {
            r.display();
        }
    }

    public void generateSummary(List<Reservation> history) {
        System.out.println("\n--- Summary Report ---");

        Map<String, Integer> countByRoom = new HashMap<>();

        for (Reservation r : history) {
            countByRoom.put(
                    r.getRoomType(),
                    countByRoom.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (String type : countByRoom.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByRoom.get(type));
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Step 1: Simulate confirmed bookings
        history.addReservation(new Reservation("RES101", "Alice", "Single"));
        history.addReservation(new Reservation("RES102", "Bob", "Suite"));
        history.addReservation(new Reservation("RES103", "Charlie", "Single"));

        // Step 2: Admin views all bookings
        reportService.showAllBookings(history.getAllReservations());

        // Step 3: Generate summary report
        reportService.generateSummary(history.getAllReservations());
    }
}