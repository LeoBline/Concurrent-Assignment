import Allsnake.ServerDB;
import org.junit.Test;
import org.junit.Assert;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * All the test about concurrent login and basic function of login system.
 */
public class LoginTest {
    ServerDB testServerDB = new ServerDB("001","123456");
    DB db = DBMaker.newFileDB(new File("PlayerInformation"))
            .closeOnJvmShutdown()
            .encryptionEnable("password")
            .make();
    //Put the account information to the playerMap
    ConcurrentNavigableMap<String, String> playerMap = db.getTreeMap("playerinformation");

    /**
     * Test whether the "001" successful add into testServerDB;
     * @throws Exception
     */
    @Test
    public void updatePlayerMapTest() throws Exception {
        testServerDB.update(playerMap,db);
        Assert.assertEquals("001",testServerDB.call());;
    }
}
