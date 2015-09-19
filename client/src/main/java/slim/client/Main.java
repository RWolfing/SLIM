package slim.client;


/**
 * Main class
 * @author Robert
 */
public class Main {
    
    public static void main(String[] args) {
       SlimClient client = new SlimClient("http://localhost:8080");
       client.start();
    }
}
