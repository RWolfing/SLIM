/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.GuestEntry;
import slim.core.model.Location;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class JSONMappingTest {

    private File mEventTestFileWrite;
    private File mUserTestFileWrite;
    private File mEventTestFileRead;
    private File mUserTestFileRead;

    private Marshaller mMarshaller;
    private Unmarshaller mUnmarshaller;

    /**
     * ********** READ FIELDS ***************
     */
    //User
    private int mReadUserId = 5;
    private String mReadNickName = "nickname";
    private String mReadFirstName = "firstname";
    private String mReadLastName = "lastname";
    private long mReadBirthday = 1000;
    private String mReadAbout = "about";
    private String mReadImageUrl = "www.test.de";

    //Event
    private int mReadEventID = 5;
    private String mReadName = "name";
    private int mReadLocID = 7;
    private long mReadLattitude = 1000;
    private long mReadLongitude = 1000;
    private long mReadEventBegin = 1200;
    private long mReadEventEnd = 2200;
    private String mReadDescription = "description";

    @Before
    public void prepareJsonMarshalling() throws PropertyException, JAXBException {
        mEventTestFileRead = new File(System.getProperty("user.dir") + "/src/test/resources/eventTestRead.json");
        mEventTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/resources/eventTestWrite.json");
        mUserTestFileRead = new File(System.getProperty("user.dir") + "/src/test/resources/userTestRead.json");
        mUserTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/resources/userTestWrite.json");

        JAXBContext context = JAXBContext.newInstance(User.class, Event.class, Location.class, GuestEntry.class);
        mMarshaller = context.createMarshaller();
        mMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        mMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        mMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        mUnmarshaller = context.createUnmarshaller();
        mUnmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        mUnmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
    }

    @Test
    public void createValidUserJsonFile() throws IOException, JAXBException {
        if (mUserTestFileWrite.exists()) {
            mUserTestFileWrite.delete();
        }

        //Create xml file for user
        User user = new User("nickName", "firstName", "lastName", 0, "about", "imageurl");
        mMarshaller.marshal(user, new FileWriter(mUserTestFileWrite));

        //Check if file was created
        assertThat(mUserTestFileWrite.exists(), is(true));
        assertThat(mUserTestFileWrite.isFile(), is(true));

        //deserialize
        User desUser = (User) mUnmarshaller.unmarshal(new StreamSource(mUserTestFileWrite), User.class).getValue();

        //Check if everything worked
        assertThat(desUser, is(not(sameInstance(user))));
        assertThat(desUser.getmID(), is(user.getmID()));
        assertThat(desUser.getmNickName(), equalTo(user.getmNickName()));
        assertThat(desUser.getmFirstName(), equalTo(user.getmFirstName()));
        assertThat(desUser.getmLastName(), equalTo(user.getmLastName()));
        assertThat(desUser.getmAbout(), equalTo(user.getmAbout()));
        assertThat(desUser.getmBirthday(), is(user.getmBirthday()));
    }

    @Test
    public void createValidEventJsonFile() throws IOException, JAXBException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (mEventTestFileWrite.exists()) {
            mEventTestFileWrite.delete();
        }

        User organizer = new User("organizer", "organizer", "organizer", 0, "about", "imageurl");
        User guest1 = new User("guest", "guest", "guest", 0, "about", "imageurl");
        User guest2 = new User("guest2", "guest2", "guest2", 0, "about", "imageurl");

        /**
         * IDs will be created through the underlying database. But here we wont
         * be saving any users to the database, so the ids will be missing.
         * Therefore we will smart and set them through reflection ;).
         */
        Field guestId = User.class.getDeclaredField("mID");
        guestId.setAccessible(true);
        guestId.set(guest1, 1);
        guestId.set(guest2, 2);

        Location location = new Location(500, 500);
        Event event = new Event("name", location, 0, 10000, "description", organizer);
        event.addGuest(guest1);
        event.addGuest(guest2);
        mMarshaller.marshal(event, new FileWriter(mEventTestFileWrite));

        //Check if file was created
        assertThat(mEventTestFileWrite.exists(), is(true));
        assertThat(mEventTestFileWrite.isFile(), is(true));

        //deserialize
        Event desEvent = (Event) mUnmarshaller.unmarshal(new StreamSource(mEventTestFileWrite), Event.class).getValue();

        //Check if everything worked
        assertThat(desEvent, is(not(sameInstance(event))));
        assertThat(desEvent.getmID(), is(event.getmID()));
        assertThat(desEvent.getmName(), equalTo(event.getmName()));
        assertThat(desEvent.getmLocation(), notNullValue());
        assertThat(desEvent.getmLocation().getmID(), is(event.getmLocation().getmID()));
        assertThat(desEvent.getmLocation().getmLattitude(), is(event.getmLocation().getmLattitude()));
        assertThat(desEvent.getmLocation().getmLongitude(), is(event.getmLocation().getmLongitude()));
        assertThat(desEvent.getmEventBegin(), is(event.getmEventBegin()));
        assertThat(desEvent.getmEventEnd(), is(event.getmEventEnd()));
        assertThat(desEvent.getmDescription(), is(event.getmDescription()));
        assertThat(desEvent.getGuests(), notNullValue());
        assertThat(desEvent.getGuests().size(), is(2));
        assertThat(desEvent.getmOrganizer(), notNullValue());
        assertThat(desEvent.getmOrganizer().getmID(), is(event.getmOrganizer().getmID()));
    }

    @Test
    public void loadUserFromJsonFile() throws JAXBException, FileNotFoundException {
        User user = (User) mUnmarshaller.unmarshal(new StreamSource(mUserTestFileRead), User.class).getValue();

        assertThat(user.getmID(), is(mReadUserId));
        assertThat(user.getmNickName(), equalTo(mReadNickName));
        assertThat(user.getmFirstName(), equalTo(mReadFirstName));
        assertThat(user.getmBirthday(), is(mReadBirthday));
        assertThat(user.getmAbout(), equalTo(mReadAbout));
        assertThat(user.getmImageUrl(), equalTo(mReadImageUrl));
    }

    @Test
    public void loadEventFromJsonFile() throws JAXBException, FileNotFoundException {
        Event event = (Event) mUnmarshaller.unmarshal(new StreamSource(mEventTestFileRead), Event.class).getValue();

        assertThat(event.getmID(), is(mReadEventID));
        assertThat(event.getmName(), equalTo(mReadName));
        assertThat(event.getmLocation(), notNullValue());
        assertThat(event.getmLocation().getmID(), is(mReadLocID));
        assertThat(event.getmLocation().getmLattitude(), is(mReadLattitude));
        assertThat(event.getmLocation().getmLongitude(), is(mReadLongitude));
        assertThat(event.getmEventBegin(), is(mReadEventBegin));
        assertThat(event.getmEventEnd(), is(mReadEventEnd));
        assertThat(event.getmOrganizer(), notNullValue());
        assertThat(event.getmOrganizer().getmID(), is(mReadUserId));
        assertThat(event.getmOrganizer().getmNickName(), equalTo(mReadNickName));
        assertThat(event.getmOrganizer().getmFirstName(), equalTo(mReadFirstName));
        assertThat(event.getmOrganizer().getmLastName(), equalTo(mReadLastName));
        assertThat(event.getmOrganizer().getmBirthday(), is(mReadBirthday));
        assertThat(event.getmOrganizer().getmAbout(), equalTo(mReadAbout));
        assertThat(event.getmOrganizer().getmImageUrl(), equalTo(mReadImageUrl));
        assertThat(event.getGuests(), notNullValue());
        assertThat(event.getGuests().size(), is(2));
    }
}