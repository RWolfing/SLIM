/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client.services;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import static slim.client.services.SlimService.ERROR_JAXB;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class UserService extends SlimService {
    
    private static final String ERROR_JAXB_USER = ERROR_JAXB + User.class.getName();
    
    public UserService(String serviceUrl, MediaType type) {
        super(serviceUrl + "/users", type);
    }
    
    public SlimResult<User> createUser(String nickName, String firstName, String lastName, long birthday, String about, String imageUrl) {
        SlimResult result = new SlimResult(null);
        PostMethod post = new PostMethod(mServiceBaseURI);
        post.addParameter("nickname", nickName);
        post.addParameter("firstname", firstName);
        post.addParameter("lastname", lastName);
        post.addParameter("birthday", String.valueOf(birthday));
        post.addParameter("about", about);
        post.addParameter("imageurl", imageUrl);
        post.setRequestHeader("Accept", mMediaType.getValue());
        
        try {
            int status = mHttpClient.executeMethod(post);
            result.setStatus(status);
            if (status == HttpStatus.SC_CREATED) {
                User user = (User) unmarshall(post.getResponseBodyAsString(), User.class);
                result.setResultContent(user);
            }
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_USER, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "create user", ex);
        }
        return result;
    }
    
    public SlimResult<User> updateUser(int userId, String nickName, long birthday, String about, String imageUrl) {
        SlimResult result = new SlimResult(null);
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + userId);
        NameValuePair pairNick = new NameValuePair("nickName", nickName);
        NameValuePair pairBDay = new NameValuePair("birthday", String.valueOf(birthday));
        NameValuePair pairAbout = new NameValuePair("about", about);
        NameValuePair pairImageUrl = new NameValuePair("imageurl", imageUrl);
        putMethod.setQueryString(new NameValuePair[]{pairNick, pairBDay, pairAbout, pairImageUrl});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());
        
        try {
            int status = mHttpClient.executeMethod(putMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                User user = (User) unmarshall(putMethod.getResponseBodyAsString(), User.class);
                result.setResultContent(user);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "udpate user", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_USER, ex);
        }
        return result;
    }
    
    public SlimResult deleteUser(int userId) {
        SlimResult result = new SlimResult(null);
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + userId);
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            result.setStatus(status);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public SlimResult<User> fetchUserById(int id) {
        SlimResult result = new SlimResult(null);
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + id);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                User user = (User) unmarshall(getMethod.getResponseBodyAsString(), User.class);
                result.setResultContent(user);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "fetch user by id", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_USER, ex);
        }
        return result;
    }
    
    public SlimResult<List<User>> fetchAllUsers() {
        SlimResult result = new SlimResult(null);
        return null;
    }
    
    public SlimResult<Boolean> doesHePartyWitheMe(int userId1, int userId2) {
        SlimResult result = new SlimResult(null);
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + userId1 + "/" + userId2);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        
        try {
            int status = mHttpClient.executeMethod(getMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Boolean isTrue = (Boolean) unmarshall(getMethod.getResponseBodyAsString(), User.class);
                result.setResultContent(isTrue);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "does he party with me", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_USER, ex);
        }
        return result;
    }
}
