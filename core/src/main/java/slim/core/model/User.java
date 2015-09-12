package slim.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for a user
 * 
 * @author Robert Wolfinger
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
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
    
    public void setmID(int id){
        mID = id;
    }

    public int getmID() {
        return mID;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public long getmBirthday() {
        return mBirthday;
    }

    public void setmBirthday(long mBirthday) {
        this.mBirthday = mBirthday;
    }

    public String getmAbout() {
        return mAbout;
    }

    public void setmAbout(String mAbout) {
        this.mAbout = mAbout;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
    
    @Override
    public int hashCode() {
        return mID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return ((User) obj).getmID() == mID;
    }

    @Override
    public String toString() {
        return "User{" + "mID=" + mID + ", mNickName=" + mNickName + ", mFirstName=" + mFirstName + ", mLastName=" + mLastName + ", mBirthday=" + mBirthday + ", mAbout=" + mAbout + ", mImageUrl=" + mImageUrl + '}';
    }
}
