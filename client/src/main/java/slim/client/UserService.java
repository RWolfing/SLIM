/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import java.util.List;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class UserService extends SlimService{
    
    public static SlimResult<User> createUser(String nickName, String firstName, String lastName, long birthday, String about, String imageUrl){
        
        return null;
    }
    
    public static SlimResult<User> updateUser(int userId, String nickName, long birthday, String about, String imageUrl){
        return null;
    }
    
    public static SlimResult deleteUser(int userId){
        return null;
    }
    
    public static SlimResult<User> fetchUserById(int id){
        return null;
    }
    
    public static SlimResult<List<User>> fetchAllUsers(){
        return null;
    }
    
    public static SlimResult<Boolean> doesHePartyWitheMe(int userId1, int userId2){
        return null;
    }
}
