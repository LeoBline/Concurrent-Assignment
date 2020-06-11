import Allsnake.ServerDB;
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
//    ServerDB testServerDB1 = new ServerDB("002","123456");
//    ServerDB testServerDB2= new ServerDB("003","123456");
//    ServerDB testServerDB3 = new ServerDB("004","123456");
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
        Assert.assertEquals("001",testServerDB.call());;
    }

    /**
     * Test what will happen if we login 4 account at same time
     */
    @Test
    public void concurrentLoginTest() throws InterruptedException {

    }

    /**
     * Give a random integer between specif range
     * @param min
     * @param max
     * @return
     */
    private static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

}