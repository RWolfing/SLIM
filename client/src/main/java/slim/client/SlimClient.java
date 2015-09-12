package slim.client;

import java.io.BufferedReader;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
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

    private static final String serviceBaseURI = "http://localhost:8080";

    private UserService mUserService = new UserService(serviceBaseURI, SlimService.MediaType.JSON);
    private EventService mEventService = new EventService(serviceBaseURI, SlimService.MediaType.JSON);
    private LocationService mLocationService = new LocationService(serviceBaseURI, SlimService.MediaType.JSON);

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
    private BufferedReader mReader;

    private SlimService.MediaType mMediaType = SlimService.MediaType.JSON;

    public SlimClient() {
        mScanner = new Scanner(System.in);
    }

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
                    setLoggingEnabled(true);
                    testUserSimpleLifeCycle();
                    break;
                case 3:
                    setLoggingEnabled(true);
                    testUserFetchAll();
                    break;
                case 4:
                    cleanupDatabase();
                    break;
                case 5:
                    setLoggingEnabled(true);
                    testEventSimpleLifeCycle();
                    break;
                case 6:
                    setLoggingEnabled(true);
                    testLocationSimpleLifeCycle();
                    break;
                case 7:
                    setLoggingEnabled(true);
                    testGuestLifeCycle();
                    break;
                case 8:
                    switchMediaType();
                    break;
                default:
                    System.out.println("No valid option!");
            }
        }

    }

    public void cleanupDatabase() {
        File coreModul = new File(System.getProperty("user.dir"));
        File dbFile = new File(coreModul.getParent() + "/slim.db");
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Deleted database! " + dbFile.getAbsolutePath());
            } else {
                System.err.println("Could not delete database! " + dbFile.getAbsolutePath());
            }
        }
    }

    public void showAvailableOptions() {
        System.out.println("|------- Available options --------|");
        System.out.println("| 0: To Exit programm.. |");
        System.out.println("| 1: Setup Datapool |");
        System.out.println("| 4: Cleanup Datapool |");
        System.out.println("| 8: Switch MediaType, Active: " + mMediaType);
        System.out.println("| 2: Test User Lifecylce |");
        System.out.println("| 5: Test Event Lifecylce |");
        System.out.println("| 6: Test Location Lifecycle |");
        System.out.println("| 7: Test Guest Lifecycle |");
        System.out.println("| 3: Fetch all users |");
    }

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

    public void testLocationSimpleLifeCycle() {
        printMessage("creating location");
        Location location = mLocationService.createLocation("Max Location", Long.MAX_VALUE, Long.MAX_VALUE).getResultContent();
        printMessage("updating location");
        mLocationService.updateLocation(location.getID(), "Updated Max Location");
        printMessage("retrieving location by id");
        mLocationService.fetchLocationById(location.getID());
        printMessage("retrieving location by lat/long (SUCCESS)");
        mLocationService.fetchLocationLongLat(location.getLattitude(), location.getLongitude());
        printMessage("retrieving location by lat/long (NOT FOUND)");
        mLocationService.fetchLocationLongLat(Long.MIN_VALUE, Long.MIN_VALUE);
        printMessage("deleting location");
        mLocationService.deleteLocation(location.getID());
        printMessage("retrieving location");
        mLocationService.fetchLocationById(location.getID());
    }

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

    private void printMessage(String message) {
        System.out.println("----" + message.toUpperCase() + "----");
    }

    public void testUserFetchAll() {
        mUserService.fetchAllUsers();
    }

    public void setLoggingEnabled(boolean enabled) {
        mUserService.setLoggingEnabled(enabled);
        mEventService.setLoggingEnabled(enabled);
        mLocationService.setLoggingEnabled(enabled);
    }

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
