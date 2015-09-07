/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Robert
 */
@XmlRootElement(name = "user")
@Entity
public class User {
    
    @XmlAttribute(name = "id")
    @Column(name = "id")
    private int mID;
    @XmlElement(name = "nickname")
    @Column(name = "nickname")
    private String mNickName;
    @XmlElement(name = "firstname")
    @Column(name = "firstname")
    private String mFirstName;
    @XmlElement(name = "lastname")
    @Column(name = "lastname")
    private String mLastName;
    @XmlElement(name = "birthday")
    @Column(name = "birthday")
    private String mBirthday;
    @XmlElement(name = "about")
    @Column(name = "about")
    private String mAbout;
    @XmlElement(name = "imageUrl")
    @Column(name = "imageUrl")
    private String mImageUrl;
    
    public User(){
    }
    
    //TODO id must not be null
    public User(String nickName, String firstName, String lastName, String birthday, String about){
        mNickName = nickName;
        mFirstName = firstName;
        mLastName = lastName;
        mBirthday = birthday;
        mAbout = about;
    }

    public int getID(){
        return mID;
    }
    /**
     * @return the mNickName
     */
    public String getmNickName() {
        return mNickName;
    }

    /**
     * @return the mFirstName
     */
    public String getmFirstName() {
        return mFirstName;
    }

    /**
     * @return the mLastName
     */
    public String getmLastName() {
        return mLastName;
    }

    /**
     * @return the mBirthday
     */
    public String getmBirthday() {
        return mBirthday;
    }

    /**
     * @return the mAbout
     */
    public String getmAbout() {
        return mAbout;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
