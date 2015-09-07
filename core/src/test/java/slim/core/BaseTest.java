/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.io.File;
import slim.core.impl.SlimServiceInMemory;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import slim.core.impl.SlimDBServiceImpl;
import slim.core.utils.Constants;

/**
 *
 * @author Robert
 */
public abstract class BaseTest {

    private static final String CLASS_NAME = BaseTest.class.getName();
    SlimService mSlimService;
    SlimDBServiceImpl mSlimDatabase;

    /**
     * Prepare the initial setup before testing
     */
    @Before
    public void buildService() {
        mSlimService = new SlimServiceInMemory();
        mSlimDatabase = new SlimDBServiceImpl(Constants.MOCK_DB_NAME);
        mSlimService.setDatabase(mSlimDatabase);
    }

    /**
     * Tear down the created setup
     */
    @After
    public void tearDownService() {
        // Cleanup the mock database
        mSlimDatabase.close();
        File file = new File(System.getProperty("user.dir") + "/" + Constants.MOCK_DB_NAME);
        if (file.exists()) {
            file.delete();
        } else {
            System.err.println(CLASS_NAME + " could not delete mockup database. File does not exist!");
        }
    }

    protected String getRandomName() {
        return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
    }

    protected String getRandomUUIDString() {
        return String.valueOf(UUID.randomUUID());
    }

    protected String getRandomDateString() {
        Date date = new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
        return date.toString();
    }
}
