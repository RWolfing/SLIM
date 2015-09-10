/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import slim.core.SlimService;
import slim.core.impl.SlimServiceInMemory;
import slim.rest.impl.EventResourceImpl;
import slim.rest.impl.UserResourceImpl;

/**
 *
 * @author Robert
 */
public class SlimApplication extends ResourceConfig{
    
   
    public SlimApplication(){
   
        register(UserResourceImpl.class);
        register(EventResourceImpl.class);
        
        register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(new SlimServiceInMemory()).to(SlimService.class);
            }
        });
    }
}
