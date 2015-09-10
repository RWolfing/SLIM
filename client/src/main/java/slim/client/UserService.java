/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class UserService extends SlimService {

    private static final String USER_ENDPOINT_PATH = "users";
    private static final String USER_ENDPOINT_CREATE = USER_ENDPOINT_PATH;
    private static final String USER_ENDPOINT_UPDATE = USER_ENDPOINT_PATH + "/id";
    private static final String USER_ENDPOINT_DELETE = USER_ENDPOINT_PATH;
    private static final String USER_ENDPOINT_GET = USER_ENDPOINT_PATH;
    private static final String USER_ENDPOINT_GET_ALL = USER_ENDPOINT_PATH;
    private static final String USER_ENDPOINT_PARTY_WITH = USER_ENDPOINT_PATH;

    public UserService(String serviceUrl, MediaType type) {
        super(serviceUrl + "/users", type);
    }

    public SlimResult<User> createUser(String nickName, String firstName, String lastName, long birthday, String about, String imageUrl) {
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
            User user = (User) unmarshall(post.getResponseBodyAsString(), User.class);
            return new SlimResult<>(status, user);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        }
        return null;
    }

    public SlimResult<User> updateUser(int userId, String nickName, long birthday, String about, String imageUrl) {
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + userId);
        NameValuePair pairNick = new NameValuePair("nickName", nickName);
        NameValuePair pairBDay = new NameValuePair("birthday", String.valueOf(birthday));
        NameValuePair pairAbout = new NameValuePair("about", about);
        NameValuePair pairImageUrl = new NameValuePair("imageurl", imageUrl);
        putMethod.setQueryString(new NameValuePair[]{pairNick, pairBDay, pairAbout, pairImageUrl});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());
        
        try {
            int status = mHttpClient.executeMethod(putMethod);
            User user = (User) unmarshall(putMethod.getResponseBodyAsString(), User.class);
            return new SlimResult<>(status, user);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult deleteUser(int userId) {
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + userId);
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            return new SlimResult(status, null);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SlimResult<User> fetchUserById(int id) {
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + id);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            User user = (User) unmarshall(getMethod.getResponseBodyAsString(), User.class);
            return new SlimResult<>(status, user);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult<List<User>> fetchAllUsers() {
        
        return null;
    }

    public SlimResult<Boolean> doesHePartyWitheMe(int userId1, int userId2) {
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + userId1 + "/" + userId2);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        
        try {
            int status = mHttpClient.executeMethod(getMethod);
            Boolean isTrue = (Boolean) unmarshall(getMethod.getResponseBodyAsString(), User.class);
            return new SlimResult<>(status, isTrue);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }
}
