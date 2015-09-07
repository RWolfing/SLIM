/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Robert
 */
@XmlRootElement(name = "user")
@DatabaseTable(tableName = "users")
public class User {

    @XmlAttribute(name = "id")
    @DatabaseField(generatedId = true)
    private int mID;
    @XmlElement(name = "nickname")
    @DatabaseField
    private String mNickName;
    @XmlElement(name = "firstname")
    @DatabaseField
    private String mFirstName;
    @XmlElement(name = "lastname")
    @DatabaseField
    private String mLastName;
    @XmlElement(name = "birthday")
    @DatabaseField
    private long mBirthday;
    @XmlElement(name = "about")
    @DatabaseField
    private String mAbout;
    @XmlElement(name = "imageUrl")
    @DatabaseField
    private String mImageUrl;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "guestlist_id")
    private GuestList mGuestList;

    public User() {
    }

    public User(String nickName, String firstName, String lastName, long birthday, String about, String imageUrl) {
        mNickName = nickName;
        mFirstName = firstName;
        mLastName = lastName;
        mBirthday = birthday;
        mAbout = about;
        mImageUrl = imageUrl;
    }

    public int getID() {
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
    public long getmBirthday() {
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
