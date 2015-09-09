/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Robert
 */
public class SlimServiceTest extends BaseTest {

    @Before
    public void setupTest() throws SQLException {
        mSlimDatabase.clearAllTables();
    }

    @Test
    public void getEventsWithUserTest() throws SQLException {
        mSlimDatabase.clearAllTables();
        User searchedUser = createRandomUser();
        Collection<User> guestListWithUser = new ArrayList<>();
        guestListWithUser.add(searchedUser);
        guestListWithUser.add(createRandomUser());
        guestListWithUser.add(createRandomUser());

        Collection<User> guestListWithoutUser = new ArrayList();
        guestListWithoutUser.add(createRandomUser());
        guestListWithoutUser.add(createRandomUser());

        Event event1 = createRandomEvent(guestListWithUser, null);
        Event event2 = createRandomEvent(guestListWithoutUser, null);
        Event event3 = createRandomEvent(guestListWithUser, null);
        Event event4 = createRandomEvent(guestListWithoutUser, null);

        List<Event> result = mSlimService.getEventsWithUser(searchedUser.getmID());
        assertThat(result.contains(event1), is(true));
        assertThat(result.contains(event2), is(false));
        assertThat(result.contains(event3), is(true));
        assertThat(result.contains(event4), is(false));
    }
}
