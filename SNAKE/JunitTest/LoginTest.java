import Allsnake.ServerDB;
import Allsnake.ServerUIControl;
import org.junit.Test;
import org.junit.Assert;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
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
    public void updatePlayerMapTest() throws InterruptedException {
        testServerDB.update();

        //Should be 4 player in player map
        Assert.assertEquals(4,testServerDB.getMap().size());
        //Should return "001" when testServerDB calls
        Assert.assertEquals("001",testServerDB.call());
    }

    /**
     * Test what will happen if we login 4 account at same time
     */
    @Test
    public void concurrentLoginTest() throws InterruptedException {
        ServerUIControl testServerUi= new ServerUIControl("test");
        testServerUi.playerLogin("001","123456");
        testServerUi.playerLogin("002","123456");
        testServerUi.playerLogin("003","123456");
        testServerUi.playerLogin("004","123456");
        Assert.assertEquals(4,testServerUi.getPlayerList().length);
    }

}