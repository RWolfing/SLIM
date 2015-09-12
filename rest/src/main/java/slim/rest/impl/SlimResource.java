package slim.rest.impl;

import javax.ws.rs.core.Context;
import slim.core.SlimService;

/**
 * Abstract class of a resource that provides the {@link SlimService}
 * 
 * @author Robert Wolfinger
 */
public abstract class SlimResource {
    @Context
    protected SlimService mSlimService;

    public SlimService getSlimService() {
        return mSlimService;
    }

    public void setSlimService(SlimService slimService) {
        mSlimService = slimService;
    }
}
