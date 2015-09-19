package slim.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import slim.core.SlimService;
import slim.core.impl.SlimServiceInMemory;
import slim.rest.impl.EventResourceImpl;
import slim.rest.impl.LocationResourceImpl;
import slim.rest.impl.UserResourceImpl;

/**
 * Implementation of the slim application for the service
 *
 * @author Robert Wolfinger
 */
public class SlimApplication extends ResourceConfig {

    public SlimApplication() {

        //Register rest resources
        register(UserResourceImpl.class);
        register(EventResourceImpl.class);
        register(LocationResourceImpl.class);
        register(DebugResource.class);

        register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(new SlimServiceInMemory()).to(SlimService.class);
            }
        });
    }
}
