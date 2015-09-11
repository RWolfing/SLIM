/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import java.util.Calendar;
import java.util.List;
import slim.client.services.EventService;
import slim.client.services.LocationService;
import slim.client.services.SlimService;
import slim.client.services.UserService;
import slim.core.model.Event;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class SlimClient {

    private static final String serviceBaseURI = "http://localhost:8080";

    private static UserService mUserService = new UserService(serviceBaseURI, SlimService.MediaType.JSON);
    private static EventService mEventService = new EventService(serviceBaseURI, SlimService.MediaType.JSON);
    private static LocationService mLocationService = new LocationService(serviceBaseURI, SlimService.MediaType.JSON);

    public static void main(String[] args) {
        testUserFunctionality();
        testEventFunctionality();
    }

    private static void testUserFunctionality() {
        System.out.println();
        System.out.println("---- CREATING USER ----");

        Calendar cal = Calendar.getInstance();
        cal.set(2000, 3, 20);
        User userMax = mUserService.createUser("mmuster", "Max", "Mustermann", cal.getTimeInMillis(), "Super läufts", "www.geilesaeue.de").getmResultContent();

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
        
        userMax = mUserService.createUser("mmuster", "Max", "Mustermann", cal.getTimeInMillis(), "Super läufts", "www.geilesaeue.de").getmResultContent();
        userMax = mUserService.createUser("mmuster", "Max1", "Mustermann", cal.getTimeInMillis(), "Super läufts", "www.geilesaeue.de").getmResultContent();
        userMax = mUserService.createUser("mmuster", "Max2", "Mustermann", cal.getTimeInMillis(), "Super läufts", "www.geilesaeue.de").getmResultContent();
        
        mUserService.fetchAllUsers();
    }
    
    private static void testEventFunctionality(){
        User userMax = mUserService.createUser("mmuster", "Max", "Mustermann", 5910591000l, "Super läufts", "www.geilesaeue.de").getmResultContent();
        Event event = mEventService.createEvent("test", 5000, 5000, 500, 5000, "description", userMax.getmID()).getmResultContent();
        
        List<Event> events = mEventService.fetchAllEvents().getmResultContent();
    }
}
