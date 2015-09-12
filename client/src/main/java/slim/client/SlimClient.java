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

    public SlimClient() {
        mScanner = new Scanner(System.in);
    }

    public void start() {
        boolean nextRound = true;
        while (nextRound) {
            showAvailableOptions();
            int input = mScanner.nextInt();
            switch (input) {
                case 1:
                    testUserFunctionality();
                    break;
                default:
                    System.out.println();
                    System.out.println("No valid option!");
                    nextRound = false;
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
        System.out.println("| 1: Show User create->update->retrieve->delete->retrieve |");
    }

    public boolean setupDataPool() {
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
        mEventStandard = mEventService.createEvent("Standard Event", 4091509090l, -490815090l, 98795161l, 121059019100l, "Das ist ein Standard event", mUserMaxMuster.getmID()).getResultContent();
        mEventWithGuests = mEventService.createEvent("Guest Event", 4091509090l, -490815090l, 98795161l, 121059019100l, "Das ist ein Event mit Gaesten", mUserMaxMuster.getmID()).getResultContent();
        mEventService.addGuestToEvent(mEventWithGuests.getmID(), mUserABranco.getmID());
        mEventService.addGuestToEvent(mEventWithGuests.getmID(), mUserLKopfer.getmID());
        mEventService.addGuestToEvent(mEventWithGuests.getmID(), mUserMMueller.getmID());

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

    public void testUserFunctionality() {
        cleanupDatabase();
        mUserService.setLoggingEnabled(true);
        System.out.println();
        System.out.println("---- CREATING USER ----");

        Calendar cal = Calendar.getInstance();
        cal.set(2000, 3, 20);
        User userMax = mUserService.createUser("mmuster", "Max", "Mustermann", cal.getTimeInMillis(), "Super läufts", "www.geilesaeue.de").getResultContent();

        System.out.println();
        System.out.println("---- UPDATING USER ----");
        mUserService.updateUser(userMax.getmID(), "maxi", userMax.getmBirthday() + 10000000l, "Läuft immer besser", "www.nettebilder.de");

        System.out.println();
        System.out.println("---- RETRIEVING USER ----");
        mUserService.fetchUserById(userMax.getmID());

        System.out.println();
        System.out.println("---- DELETING USER ----");
        mUserService.deleteUser(userMax.getmID());

        System.out.println();
        System.out.println("---- RETRIEVING USER ----");
        mUserService.fetchUserById(userMax.getmID());

        mUserService.fetchAllUsers();
    }

    public void testEventFunctionality() {
        User userMax = mUserService.createUser("mmuster", "Max", "Mustermann", 5910591000l, "Super läufts", "www.geilesaeue.de").getResultContent();
        Event event = mEventService.createEvent("test", 5000, 5000, 500, 5000, "description", userMax.getmID()).getResultContent();

        List<Event> events = mEventService.fetchAllEvents().getResultContent();
    }
}
