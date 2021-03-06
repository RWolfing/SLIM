package slim.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import slim.client.services.EventService;
import slim.client.services.LocationService;
import slim.client.services.SlimService;
import slim.client.services.UserService;
import slim.core.model.Event;
import slim.core.model.Location;
import slim.core.model.User;

/**
 * Client to test the restful slim webservice
 *
 * @author Robert Wolfinger
 */
public class SlimClient {

    private final String mServiceBaseURI;

    private UserService mUserService;
    private EventService mEventService;
    private LocationService mLocationService;

    private Calendar mCalendar = Calendar.getInstance();
    private User mUserMaxMuster;
    private User mUserABranco;
    private User mUserLKopfer;
    private User mUserMMueller;
    private User mUserGSchulze;

    private Event mEventStandard;
    private Event mEventWithGuests;

    private Location mLocationSingle;
    private Location mLocationWithEvents;

    private Scanner mScanner;

    private SlimService.MediaType mMediaType = SlimService.MediaType.XML;

    /**
     * Constructor to create a SlimClient with the given baseURI
     * @param baseURI the base uri of the webservice
     */
    public SlimClient(String baseURI) {
        mServiceBaseURI = baseURI;
        mScanner = new Scanner(System.in);
        mUserService = new UserService(mServiceBaseURI, mMediaType);
        mEventService = new EventService(mServiceBaseURI, mMediaType);
        mLocationService = new LocationService(mServiceBaseURI, mMediaType);
    }

    /**
     * Starts the client
     */
    public void start() {
        boolean nextRound = true;
        while (nextRound) {
            showAvailableOptions();
            int input = mScanner.nextInt();
            switch (input) {
                case 0:
                    nextRound = false;
                    break;
                case 1:
                    setLoggingEnabled(false);
                    setupDataPool();
                    break;
                case 2:
                    switchMediaType();
                    break;
                case 3:
                    setLoggingEnabled(true);
                    testUserSimpleLifeCycle();
                    break;
                case 4:
                    testLocationSimpleLifeCycle();
                    break;
                case 5:
                    setLoggingEnabled(true);
                    testLocationSimpleLifeCycle();
                    break;
                case 6:
                    setLoggingEnabled(true);
                    testGuestLifeCycle();
                    break;
                case 7:
                    setLoggingEnabled(true);
                    testUserFetchAll();
                    break;
                case 8:
                    testFetchAllEvents();
                    break;
                case 9:
                    setLoggingEnabled(true);
                    testLocationInvalidDelete();
                    break;
                case 10:
                    setLoggingEnabled(true);
                    testDoesHePary();
                    break;
                case 11:
                    setLoggingEnabled(true);
                    testFetchEventsWithOrganizer();
                    break;
                case 12:
                    setLoggingEnabled(true);
                    testFetchEventsWithinBounds();
                    break;
                case 13:
                    setLoggingEnabled(true);
                    testFetchEventsWithGuestrange();
                    break;
                case 14:
                    setLoggingEnabled(false);
                    cleanupDatabase();
                    break;
                default:
                    System.out.println("No valid option!");
            }
        }
    }

    /**
     * Deletes the database
     */
    public void cleanupDatabase() {
        DeleteMethod dropTables = new DeleteMethod(mServiceBaseURI + "/debug/dropall/");
        try {
            HttpClient client = new HttpClient();
            int status = client.executeMethod(dropTables);
            if(status == 200){
                System.out.println("Database successfully cleared!");
            } else {
                System.err.println("Database could not be cleared");
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Could not clear database, IO Exception", ex);
        }
    }

    /**
     * Prints the available options
     */
    public void showAvailableOptions() {
        System.out.println("|----------------INFO------------|");
        System.out.println("| Some methods need a filled databool, so if you run into 404s -> Press 1");
        System.out.println("|------- Available options --------|");
        System.out.println("| 0: To Exit programm.. |");
        System.out.println("| 1: Setup Datapool |");
        System.out.println("| 2: Switch MediaType, Active: " + mMediaType);
        System.out.println("| 3: Test User Lifecylce |");
        System.out.println("| 4: Test Event Lifecylce |");
        System.out.println("| 5: Test Location Lifecycle |");
        System.out.println("| 6: Test Guest Lifecycle |");
        System.out.println("| 7: Fetch all users |");
        System.out.println("| 8: Fetch all events |");
        System.out.println("| 9: Delete a event location (should fail) |");
        System.out.println("| 10: Does he party with me (should be true)|");
        System.out.println("| 11: Get Events with User as Organizer |");
        System.out.println("| 12: Fetch Events in Bounds |");
        System.out.println("| 13: Fetch Events with Guestrange |");
        System.out.println("| 14: Cleanup Datapool |");
    }

    /**
     * Creates a datapool on which some functionality can be tested
     *
     * @return success
     */
    public boolean setupDataPool() {
        cleanupDatabase();
        System.out.println("Setting up data pool.....");
        //Setup users
        mCalendar.set(2000, 3, 20);
        mUserMaxMuster = mUserService.createUser("mmuster", "Max", "Mustermann", mCalendar.getTimeInMillis(), "Ich bin der Max!", "www.meine-bilder.de").getResultContent();
        mCalendar.set(1955, 5, 10);
        mUserABranco = mUserService.createUser("abranco", "Anne", "Branco", mCalendar.getTimeInMillis(), "Ich bin die Anne!", "www.meine-bilder.de").getResultContent();
        mCalendar.set(1970, 7, 1);
        mUserLKopfer = mUserService.createUser("lkopfer", "Lasse", "kopfer", mCalendar.getTimeInMillis(), "Ich bin der Lasse!", "www.meine-bilder.de").getResultContent();
        mCalendar.set(1988, 2, 15);
        mUserMMueller = mUserService.createUser("mmueller", "Marvin", "Mueller", mCalendar.getTimeInMillis(), "Ich bin der Marvin!", "www.meine-bilder.de").getResultContent();
        mCalendar.set(1995, 9, 25);
        mUserGSchulze = mUserService.createUser("gschulze", "Guenther", "Schulze", mCalendar.getTimeInMillis(), "Ich bin der Guenther!", "www.meine-bilder.de").getResultContent();

        mLocationSingle = mLocationService.createLocation("Single Location", 1591050909l, 4091509090l).getResultContent();
        mLocationWithEvents = mLocationService.createLocation("Event Location", 4091509090l, -490815090l).getResultContent();

        //Setup events
        mEventStandard = mEventService.createEvent("Standard Event", 4091509090l, -490815090l, 98795161l, 121059019100l, "Das ist ein Standard event", mUserMaxMuster.getID()).getResultContent();
        mEventWithGuests = mEventService.createEvent("Guest Event", 4091509090l, -490815090l, 98795161l, 121059019100l, "Das ist ein Event mit Gaesten", mUserMaxMuster.getID()).getResultContent();
        mEventService.addGuestToEvent(mEventWithGuests.getID(), mUserABranco.getID());
        mEventService.addGuestToEvent(mEventWithGuests.getID(), mUserLKopfer.getID());
        mEventService.addGuestToEvent(mEventWithGuests.getID(), mUserMMueller.getID());

        if (mUserABranco != null && mUserGSchulze != null && mUserLKopfer != null && mUserMMueller != null && mUserMaxMuster != null && mEventStandard != null && mEventWithGuests != null && mLocationSingle != null && mLocationWithEvents != null) {
            System.out.println("Setting up data pool succeeded!");
            return true;
        } else {
            System.err.println("Setting up data pool did NOT succeed!");
            System.err.println("The service can not be tested...");
            System.err.println("Please start the server & delete the database file!");
            return false;
        }
    }

    /**
     * Tests the user "lifecycle" by testing the methods create, update, fetch &
     * delete
     */
    public void testUserSimpleLifeCycle() {
        printMessage("creating user");
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 3, 20);
        User userMax = mUserService.createUser("mschmitz", "Mario ", "Schmitz", cal.getTimeInMillis(), "Super laeufts", "www.mario.de").getResultContent();
        printMessage("updating user");
        mUserService.updateUser(userMax.getID(), "maxi", userMax.getBirthday() + 10000000l, "Laeuft immer besser", "www.mario-update.de");
        printMessage("retrieving user");
        mUserService.fetchUserById(userMax.getID());
        printMessage("deleting user");
        mUserService.deleteUser(userMax.getID());
        printMessage("retrieving user");
        mUserService.fetchUserById(userMax.getID());
    }

    /**
     * Tests the event "lifecycle" by testing the methods create, update, fetch
     * & delete
     */
    public void testEventSimpleLifeCycle() {
        if (mUserGSchulze == null) {
            System.err.println("Needed User is missing, pls setup datapool first!");
            return;
        }
        printMessage("creating event");
        Event event = mEventService.createEvent("CreatedEvent", 100, 500, 4560540l, 987985109510l, "Ein neu erzeugter Event", mUserGSchulze.getID()).getResultContent();
        printMessage("updating event");
        mEventService.updateEvent(event.getID(), "UpdatedEvent", 10, 100, "Ein geupdateter Event").getResultContent();
        printMessage("retrieving event");
        mEventService.fetchEventById(event.getID());
        printMessage("deleting event");
        mEventService.deleteEvent(event.getID());
        printMessage("retrieving event");
        mEventService.fetchEventById(event.getID());
    }

    /**
     * Tests the location "lifecycle" by testing the methods create, update,
     * fetchByID, fetchByLatLong & delete
     */
    public void testLocationSimpleLifeCycle() {
        printMessage("creating location");
        Location location = mLocationService.createLocation("Max Location", Long.MAX_VALUE, Long.MAX_VALUE).getResultContent();
        printMessage("updating location");
        mLocationService.updateLocation(location.getID(), "Updated Max Location");
        printMessage("retrieving location by id");
        mLocationService.fetchLocationById(location.getID());
        printMessage("retrieving location by lat/long (SUCCESS)");
        mLocationService.fetchLocationLatLong(location.getLattitude(), location.getLongitude());
        printMessage("retrieving location by lat/long (NOT FOUND)");
        mLocationService.fetchLocationLatLong(Long.MIN_VALUE, Long.MIN_VALUE);
        printMessage("deleting location");
        mLocationService.deleteLocation(location.getID());
        printMessage("retrieving location");
        mLocationService.fetchLocationById(location.getID());
    }

    /**
     * Tests if a location referenced in a event can be deleted
     */
    private void testLocationInvalidDelete() {
        if (mLocationWithEvents == null) {
            System.err.println("Needed event is missing, pls setup datapool first!");
            return;
        }
        printMessage("Trying to delete a event location");
        mLocationService.deleteLocation(mLocationWithEvents.getID());
    }

    /**
     * Tests the guest "lifecycle" by testing add guest, retrieve event, remove
     * guest, retrieve event
     */
    public void testGuestLifeCycle() {
        if (mUserABranco == null) {
            System.err.println("Needed Users & Events are missing, pls setup datapool first!");
            return;
        }

        printMessage("adding guest " + mUserABranco.getID() + " to event " + mEventStandard.getID());
        mEventService.addGuestToEvent(mEventStandard.getID(), mUserABranco.getID());

        printMessage("adding guest " + mUserLKopfer.getID() + " to event " + mEventStandard.getID());
        mEventService.addGuestToEvent(mEventStandard.getID(), mUserLKopfer.getID());

        printMessage("adding invalid user " + Integer.MAX_VALUE + " to event " + mEventStandard.getID());
        mEventService.addGuestToEvent(mEventStandard.getID(), Integer.MAX_VALUE);

        printMessage("retrieving event " + mEventStandard.getID());
        mEventService.fetchEventById(mEventStandard.getID());

        printMessage("removing guest " + mUserABranco.getID() + " from event " + mEventStandard.getID());
        mEventService.removeGuestFromEvent(mEventStandard.getID(), mUserABranco.getID());

        printMessage("removing guest " + mUserLKopfer.getID() + " from event " + mEventStandard.getID());
        mEventService.removeGuestFromEvent(mEventStandard.getID(), mUserLKopfer.getID());

        printMessage("retrieving event " + mEventStandard.getID());
        mEventService.fetchEventById(mEventStandard.getID());
    }

    public void testUserFetchAll() {
        mUserService.fetchAllUsers();
    }

    /**
     * Tests if abranco parties with lkopfer
     */
    public void testDoesHePary() {
        boolean doesHePartyWithMe = mUserService.doesHePartyWitheMe(mUserABranco.getID(), mUserLKopfer.getID()).getResultContent();
    }

    /**
     * Retrieves all existing events
     */
    public void testFetchAllEvents() {
        printMessage("Fetching all existing events");
        mEventService.fetchAllEvents();
    }

    /**
     * Retrieves all events where max muster is the organizer
     */
    public void testFetchEventsWithOrganizer() {
        if (mUserMaxMuster == null) {
            System.err.println("Missing Organizer, pls setup Datapool!");
            return;
        }
        printMessage("fetching all events with " + mUserMaxMuster.getID() + " as Organizer");
        mEventService.getEventsFromUser(mUserMaxMuster.getID());
    }

    /**
     * Retrievies all events with maximum bounds
     */
    public void testFetchEventsWithinBounds() {
        printMessage("Fetching all events in maximum bound");
        mEventService.getEventsWithinLocation(Long.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Retrieves all events in the guest range 1 - 10
     */
    public void testFetchEventsWithGuestrange() {
        printMessage("Fetching all events with guestrange 1 - 10");
        mEventService.getEventsWithinGuestRange(1, 10);
    }

    /**
     * Helper method to print a message
     *
     * @param message the message
     */
    private void printMessage(String message) {
        System.out.println("----" + message.toUpperCase() + "----");
    }

    /**
     * Sets the logging enabled
     *
     * @param enabled is logging enabled
     */
    public void setLoggingEnabled(boolean enabled) {
        mUserService.setLoggingEnabled(enabled);
        mEventService.setLoggingEnabled(enabled);
        mLocationService.setLoggingEnabled(enabled);
    }

    /**
     * Switches the mediatype
     */
    public void switchMediaType() {
        switch (mMediaType) {
            case JSON:
                mMediaType = SlimService.MediaType.XML;
                break;
            case XML:
                mMediaType = SlimService.MediaType.JSON;
                break;
            default:
                break;
        }
        mUserService.setMediaType(mMediaType);
        mEventService.setMediaType(mMediaType);
        mLocationService.setMediaType(mMediaType);
    }
}
