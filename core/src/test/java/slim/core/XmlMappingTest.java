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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
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
public class XmlMappingTest extends BaseTest{
    
    private File mTestFileLoad;
    private File mTestFileSave;
    private Marshaller mMarshaller;
    private Unmarshaller mUnmarshaller;
    
    //@Before
    public void prepareXmlMarshalling() throws PropertyException, JAXBException{
        mTestFileLoad = new File(XmlMappingTest.class.getResource("/robert.xml").getPath().replace("%20", " "));
        mTestFileSave = new File(XmlMappingTest.class.getResource("/randomUser.xml").getPath().replace("%20", " "));
        
        JAXBContext context = JAXBContext.newInstance(User.class, Event.class);
        
       mMarshaller = context.createMarshaller();
       mMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       
       mUnmarshaller = context.createUnmarshaller();
    }
    
    //@Test
    public void createValidXmlFile() throws JAXBException, IOException{
        
        if(mTestFileSave.exists())
        {
            mTestFileSave.delete();
        }
        
        /**
         * Erzeugt zufälligen Nutzer
         */   
        User user = null;
        
        //Serialisieren
        mMarshaller.marshal(user, new FileWriter(mTestFileSave));
        
        assertThat(mTestFileSave.exists(), is(true));
        assertThat(mTestFileSave.isFile(), is(true));
        
        //Deserialisieren
        User desUser = (User) mUnmarshaller.unmarshal(mTestFileSave);
        
        assertThat(desUser, is(not(sameInstance(user))));
        assertThat(desUser.getID(), is(user.getID()));
    }
    
   // @Test
    public void loadFromXmlFile() throws JAXBException, FileNotFoundException{
        User loadedUser = (User) mUnmarshaller.unmarshal(new FileReader(mTestFileLoad));

        assertThat(loadedUser.getmFirstName(), is("Robert"));
        assertThat(loadedUser.getmBirthday(), is("21.3.1993"));
    }
    
}
