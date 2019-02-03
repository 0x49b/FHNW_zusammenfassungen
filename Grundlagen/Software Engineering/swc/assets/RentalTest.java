package ch.fhnw.swc.mrs.model;

import ch.fhnw.swc.mrs.api.MovieRentalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Rental class.
 */
@DisplayName("Tests for class Rental")
public class RentalTest {

    private User mickey, donald;
    private Movie theKid, goldrush;
    private PriceCategory pc;
    private LocalDate today;

    /**
     * Creates legal User and Movie objects and sets the reference time stamp to now.
     */
    @BeforeEach
    public void setUp() {
        today = LocalDate.now();
        pc = RegularPriceCategory.getInstance();
        mickey = new User("Mouse", "Mickey");
        mickey.setId(4);
        donald = new User("Duck", "Donald");
        donald.setId(7);
        theKid = new Movie("The Kid", today, pc);
        theKid.setId(6);
        goldrush = new Movie("Goldrush", today, pc);
        goldrush.setId(2);
    }

    @DisplayName("Does Rental object get initialized correctly with constructor?")
    @Test
    public void testRentalCtorWithRentalDate() {
        Rental r = new Rental(mickey, theKid, today.minusDays(6));
        doAssertionsForTestRental(6, r);
    }

    private void doAssertionsForTestRental(int expected, Rental r) {
        // is the rental registered with the user?
        assertTrue(mickey.getRentals().contains(r));
        // has the movie's rented state been set to rented?
        assertTrue(theKid.isRented());
        // is the number of rental days set correctly?
        assertEquals(expected, r.getRentalDays());
        // has the rental date been set?
        assertNotNull(r.getRentalDate());
        // do we get the objects that we set?
        assertEquals(mickey, r.getUser());
        assertEquals(theKid, r.getMovie());
    }

    @DisplayName("Throws exception when ctor is called with null User ?")
    @Test
    public void testRentalCtorWithNullUser() {
        Throwable t = assertThrows(NullPointerException.class, () -> new Rental(null, theKid, today));
        assertEquals(Rental.EXC_USER_NULL, t.getMessage());
    }

    @DisplayName("Throws exception when ctor is called with null Movie?")
    @Test
    public void testRentalCtorWithNullMovie() {
        Throwable t = assertThrows(MovieRentalException.class, () -> new Rental(mickey, null, today));
        assertEquals(Rental.EXC_MOVIE_NOT_RENTALBE, t.getMessage());
    }

    @DisplayName("Throws exception when ctor is called with null Date?")
    @Test
    public void testRentalCtorWithNullDate() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> new Rental(mickey, theKid, null));
        assertEquals(Rental.EXC_RENTAL_DATE_IN_FUTURE, t.getMessage());
    }

    @DisplayName("Throws exception when ctor is called with rental date in the future?")
    @Test
    public void testRentalCtorWithFutureDate() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> new Rental(mickey, theKid, today.plusDays(1)));
        assertEquals(Rental.EXC_RENTAL_DATE_IN_FUTURE, t.getMessage());
    }

    @DisplayName("Is rental duration calculated correctly?")
    @Test
    public void testCalcDaysOfRental() {
        LocalDate rentalDate = LocalDate.now().minusDays(6);
        Rental r = new Rental(mickey, theKid, rentalDate);

        int days = r.getRentalDays();
        assertEquals(6, days);
    }

    @DisplayName("Do Id getter and setter work correctly and prevent setting id once it is set?")
    @Test
    public void testSetterGetterId() {
        Rental r = new Rental(mickey, theKid, today);
        r.setId(11);
        assertEquals(11, r.getId());

        // setting id a 2nd time
        assertThrows(IllegalStateException.class, () -> r.setId(47));
        assertEquals(11, r.getId());
    }

    @DisplayName("Do Movie getter and setter work correctly and prevent setting null Movie?")
    @Test
    public void testSetterGetterMovie() {
        Rental r = new Rental(mickey, theKid, today);
        r.setMovie(goldrush);
        assertEquals(goldrush, r.getMovie());

        Throwable t = assertThrows(MovieRentalException.class, () -> r.setMovie(null));
        assertEquals(Rental.EXC_MOVIE_NOT_RENTALBE, t.getMessage());
        assertEquals(goldrush, r.getMovie());
    }

    @DisplayName("Do User getter and setter work correctly and prevent setting null User?")
    @Test
    public void testSetterGetterUser() {
        Rental r = new Rental(mickey, theKid, today);
        r.setUser(donald);
        assertEquals(donald, r.getUser());

        Throwable t = assertThrows(NullPointerException.class, () -> r.setUser(null));
        assertEquals(Rental.EXC_USER_NULL, t.getMessage());
        assertEquals(donald, r.getUser());
    }

    @DisplayName("Does equals work correctly?")
    @Test
    @Disabled
    public void testEquals() {
    }

    @DisplayName("Is hash code calculated correctly?")
    @Test
    public void testHashCode() {
        Rental x = new Rental(mickey, theKid, today);
        theKid.setRented(false);
        Rental y = new Rental(mickey, theKid, today);

        x.setId(42);
        y.setId(42);
        assertEquals(x.hashCode(), y.hashCode());

        x.setMovie(goldrush);
        assertTrue(x.hashCode() != y.hashCode());
        y.setMovie(goldrush);
        assertEquals(x.hashCode(), y.hashCode());

        x.setUser(donald);
        assertTrue(x.hashCode() != y.hashCode());
        y.setUser(donald);
        assertEquals(x.hashCode(), y.hashCode());
    }

}
