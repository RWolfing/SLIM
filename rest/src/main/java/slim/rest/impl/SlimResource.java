/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest.impl;

import javax.ws.rs.core.Context;
import slim.core.SlimService;

/**
 *
 * @author Robert
 */
public abstract class SlimResource {
    @Context
    protected SlimService mSlimService;

    public SlimService getmSlimService() {
        return mSlimService;
    }

    public void setmSlimService(SlimService mSlimService) {
        this.mSlimService = mSlimService;
    }
}
