/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

/**
 *
 * @author Robert
 */
public interface DatabaseConnection {
    boolean open();
    boolean close();
}
