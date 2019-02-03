public class PersonTest {

    private Person person;
    private static final Calendar BIRTHDATE =  new GregorianCalendar(1987, 6, 21);

    @Before
    public void setUp() throws Exception {
        person = new Person("Hans", 1234, BIRTHDATE);
    }

    @Test
    public void testPerson() {
        // Aufgabe 3 input-Parameter testen
        assertNotNull(person);

        try {
            new Person("", 1234, BIRTHDATE);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        try {
            new Person(null, 1234, BIRTHDATE);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        try {
            // String of length > 30
            new Person("qwertyuiopasdfghjklzxcvbnmqwerty", 1234, BIRTHDATE);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        try {
            new Person("Hans", 999, BIRTHDATE);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        try {
            new Person("Hans", 10000, BIRTHDATE);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        try {
            new Person("Hans", 1234, null);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }
        Calendar date = Calendar.getInstance();
        date.add(Calendar.YEAR, 5);
        try {
            new Person("Hans", 1234, date);
            fail();
        } catch (IllegalArgumentException e) {
            // just catch, to make sure we got an exception
        }


        // Aufgabe 6
        date =  Calendar.getInstance();
        date.add(Calendar.YEAR, -10);
        person = new Person("Hans", 1234, date);
        date.add(Calendar.YEAR, 5);
        assertFalse(date.equals(person.getBirthdate()));
    }

    @Test
    public void testGetName() {
        assertEquals("Hans", person.getName());
    }

    @Test
    public void testGetZip() {
        assertEquals(1234, person.getZip());
    }

    @Test
    public void testGetBirthdate() {
        Calendar cal = person.getBirthdate();
        assertEquals(BIRTHDATE, cal);
        cal.add(Calendar.YEAR, 5);
        assertFalse(cal.equals(person.getBirthdate()));
    }

    @Test
    public void testGetAge() {
        // Aufgabe 5
        Calendar date =  Calendar.getInstance();
        date.add(Calendar.YEAR, -10);
        date.add(Calendar.DAY_OF_YEAR, -1);
        person = new Person("Hans", 1234, date);
        assertEquals(10, person.getAge());
    }

}
