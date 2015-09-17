/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;


/**
 *
 * @author Robert
 */
public class Main {
    
    public static void main(String[] args) {
       SlimClient client = new SlimClient("http://localhost:8080");
       client.start();
    }
}
