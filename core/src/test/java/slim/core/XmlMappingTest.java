/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Robert
 */
public class XmlMappingTest extends BaseTest {

    private File mEventTestFileWrite;
    private File mUserTestFileWrite;
    private File mEventTestFileRead;
    private File mUserTestFileRead;

    private Marshaller mMarshaller;
    private Unmarshaller mUnmarshaller;

    @Before
    public void prepareXmlMarshalling() throws PropertyException {
        mEventTestFileRead = new File(System.getProperty("user.dir") + "/src/test/java/resources/eventTestRead.xml");
        mEventTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/java/resources/eventTestWrite.xml");
        mUserTestFileRead = new File(System.getProperty("user.dir") + "/src/test/java/resources/userTestRead.xml");
        mEventTestFileWrite = new File(System.getProperty("user.dir") + "/src/test/java/resources/userTestWrite.xml");
String message = "Test";
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(User.class, Event.class);
             mMarshaller = context.createMarshaller();
        mMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        mUnmarshaller = context.createUnmarshaller();
        } catch (JAXBException ex) {
            message = ex.getMessage();
            Logger.getLogger(XmlMappingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

       
    }
    
    @Test
    public void createValidUserXmlFile() throws IOException, JAXBException{
        if(mUserTestFileWrite.exists()){
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

    // @Test
    public void loadFromXmlFile() throws JAXBException, FileNotFoundException {
       
    }

}
