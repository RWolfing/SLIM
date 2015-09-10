/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class SlimClient {
     private static final String serviceBaseURI = "http://localhost:8080";
     
    public static void main(String[] args){
        UserService userService = new UserService(serviceBaseURI, SlimService.MediaType.XML);
        EventService eventService = new EventService(serviceBaseURI, SlimService.MediaType.XML);
        
        User user1 = userService.createUser("Hansi", "Hansi", "Hinterseher", 5000000, "Super läufts", "www.geilesaeue.de").getmResultContent();
        System.out.println("Created user: " + user1);
        
        User user2 = userService.createUser("Franzi", "Schmanzi", "Mustermann", 5000000, "Super läufts", "www.geileresaeue.de").getmResultContent();
        System.out.println("Created user: " + user2);
        
        User user3 = userService.createUser("Wilder", "Hugo", "Haudrauf", 549814900, "Super läufts", "www.geilesaeue.de").getmResultContent();
        System.out.println("Created user: " + user3);
        
        User user4 = userService.createUser("Schnegge", "Jessica", "Joil", 16505651, "Super läufts", "www.geilesaeue.de").getmResultContent();
        System.out.println("Created user: " + user4);
        
        User user5 = userService.createUser("KeineFreunde", "Kevin", "Knoll", 987941, "Super läufts", "www.geilesaeue.de").getmResultContent();
        System.out.println("Created user: " + user5);
        
        User user6 = userService.createUser("EndeGelaende", "Klaus", "Kick", 498191951, "Super läufts", "www.geilesaeue.de").getmResultContent();
        System.out.println("Created user: " + user6);
    }
}
