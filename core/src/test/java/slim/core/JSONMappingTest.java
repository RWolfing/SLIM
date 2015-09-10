/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
        User desUser = (User) mUnmarshaller.unmarshal(mUserTestFileWrite);

        //Check if everything worked
        assertThat(desUser, is(not(sameInstance(user))));
        assertThat(desUser.getmID(), is(user.getmID()));
        assertThat(desUser.getmNickName(), equalTo(user.getmNickName()));
        assertThat(desUser.getmFirstName(), equalTo(user.getmFirstName()));
        assertThat(desUser.getmLastName(), equalTo(user.getmLastName()));
        assertThat(desUser.getmAbout(), equalTo(user.getmAbout()));
        assertThat(desUser.getmBirthday(), is(user.getmBirthday()));
    }
}
