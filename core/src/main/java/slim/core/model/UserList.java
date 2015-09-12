package slim.core.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container class to save all users in a list
 * 
 * @author Robert Wolfinger
 */
@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserList {
    
    @XmlElement(name = "user")
    private List<User> mUsers = new ArrayList<>();

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }
    
    public void add(User user){
        mUsers.add(user);
    }
    
    public void remove(User user){
        mUsers.remove(user);
    }
    
    public boolean isEmpty(){
        return mUsers.isEmpty();
    }
}
