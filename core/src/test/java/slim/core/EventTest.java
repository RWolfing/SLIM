/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.Event;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author Robert
 */
public class EventTest extends BaseTest{
    
    @Test
    public void createValidEvent(){
        
        /**
         * Event erzeugen
         */
        
        Event event = createRandomEvent(mSlimService);
        
        List<Event> eventList = mSlimService.getEvents();
        assertThat(eventList, hasItem(event));
    }
}
