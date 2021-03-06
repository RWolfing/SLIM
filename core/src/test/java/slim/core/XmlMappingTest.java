package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import slim.core.model.GuestEntry;
import slim.core.model.Location;

/**
 * Class to test the xml mapping of all models
 *
 * @author Robert Wolfinger
 */
public class XmlMappingTest extends BaseTest {

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
    private String mReadLocName = "auto-generated";
    private long mReadLattitude = 1000;
    private long mReadLongitude = 1000;
    private long mReadEventBegin = 1200;
    private long mReadEventEnd = 2200;
    private String mReadDescription = "description";

    @Before
    public void prepareXmlMarshalling() throws PropertyException, JAXBException {
        mEventTestFileRead = new File(System.getProperty("user.dir") + "/src/test/resources/eventTestRead.xml");
        mEventTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/resources/eventTestWrite.xml");
        mUserTestFileRead = new File(System.getProperty("user.dir") + "/src/test/resources/userTestRead.xml");
        mUserTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/resources/userTestWrite.xml");

        JAXBContext context = JAXBContext.newInstance(User.class, Event.class, Location.class, GuestEntry.class);
        mMarshaller = context.createMarshaller();
        mMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        mUnmarshaller = context.createUnmarshaller();
    }

    @Test
    public void createValidUserXmlFile() throws IOException, JAXBException {
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
        User desUser = (User) mUnmarshaller.unmarshal(mUserTestFileWrite);

        //Check if everything worked
        assertThat(desUser, is(not(sameInstance(user))));
        assertThat(desUser.getID(), is(user.getID()));
        assertThat(desUser.getNickName(), equalTo(user.getNickName()));
        assertThat(desUser.getFirstName(), equalTo(user.getFirstName()));
        assertThat(desUser.getLastName(), equalTo(user.getLastName()));
        assertThat(desUser.getAbout(), equalTo(user.getAbout()));
        assertThat(desUser.getBirthday(), is(user.getBirthday()));
    }

    @Test
    public void createValidEventXmlFile() throws IOException, JAXBException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
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
        Field guestId = User.class
                .getDeclaredField("mID");
        guestId.setAccessible(
                true);
        guestId.set(guest1,
                1);
        guestId.set(guest2,
                2);

        Location location = new Location("auto-generated", 500, 500);
        Event event = new Event("name", location, 0, 10000, "description", organizer);

        event.addGuest(guest1);

        event.addGuest(guest2);

        mMarshaller.marshal(event,
                new FileWriter(mEventTestFileWrite));

        //Check if file was created
        assertThat(mEventTestFileWrite.exists(), is(true));
        assertThat(mEventTestFileWrite.isFile(), is(true));

        //deserialize
        Event desEvent = (Event) mUnmarshaller.unmarshal(mEventTestFileWrite);

        //Check if everything worked
        assertThat(desEvent, is(not(sameInstance(event))));
        assertThat(desEvent.getID(), is(event.getID()));
        assertThat(desEvent.getName(), equalTo(event.getName()));
        assertThat(desEvent.getLocation(), notNullValue());
        assertThat(desEvent.getLocation().getID(), is(event.getLocation().getID()));
        assertThat(desEvent.getLocation().getName(), is(event.getLocation().getName()));
        assertThat(desEvent.getLocation().getLattitude(), is(event.getLocation().getLattitude()));
        assertThat(desEvent.getLocation().getLongitude(), is(event.getLocation().getLongitude()));
        assertThat(desEvent.getEventBegin(), is(event.getEventBegin()));
        assertThat(desEvent.getEventEnd(), is(event.getEventEnd()));
        assertThat(desEvent.getDescription(), is(event.getDescription()));
        assertThat(desEvent.getGuests(), notNullValue());
        assertThat(desEvent.getGuests().size(), is(2));
        assertThat(desEvent.getOrganizer(), notNullValue());
        assertThat(desEvent.getOrganizer().getID(), is(event.getOrganizer().getID()));
    }

    @Test
    public void loadUserFromXmlFile() throws JAXBException, FileNotFoundException {
        User user = (User) mUnmarshaller.unmarshal(new FileReader(mUserTestFileRead));

        assertThat(user.getID(), is(mReadUserId));
        assertThat(user.getNickName(), equalTo(mReadNickName));
        assertThat(user.getFirstName(), equalTo(mReadFirstName));
        assertThat(user.getBirthday(), is(mReadBirthday));
        assertThat(user.getAbout(), equalTo(mReadAbout));
        assertThat(user.getImageUrl(), equalTo(mReadImageUrl));
    }

    //TODO Guestlist
    @Test
    public void loadEventFromXmlFile() throws JAXBException, FileNotFoundException {
        Event event = (Event) mUnmarshaller.unmarshal(new FileReader(mEventTestFileRead));

        assertThat(event.getID(), is(mReadEventID));
        assertThat(event.getName(), equalTo(mReadName));
        assertThat(event.getLocation(), notNullValue());
        assertThat(event.getLocation().getID(), is(mReadLocID));
        assertThat(event.getLocation().getName(), equalTo(mReadLocName));
        assertThat(event.getLocation().getLattitude(), is(mReadLattitude));
        assertThat(event.getLocation().getLongitude(), is(mReadLongitude));
        assertThat(event.getEventBegin(), is(mReadEventBegin));
        assertThat(event.getEventEnd(), is(mReadEventEnd));
        assertThat(event.getOrganizer(), notNullValue());
        assertThat(event.getOrganizer().getID(), is(mReadUserId));
        assertThat(event.getOrganizer().getNickName(), equalTo(mReadNickName));
        assertThat(event.getOrganizer().getFirstName(), equalTo(mReadFirstName));
        assertThat(event.getOrganizer().getLastName(), equalTo(mReadLastName));
        assertThat(event.getOrganizer().getBirthday(), is(mReadBirthday));
        assertThat(event.getOrganizer().getAbout(), equalTo(mReadAbout));
        assertThat(event.getOrganizer().getImageUrl(), equalTo(mReadImageUrl));
        assertThat(event.getGuests(), notNullValue());
        assertThat(event.getGuests().size(), is(2));
    }
}
