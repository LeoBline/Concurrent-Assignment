import org.junit.Test;
import org.junit.Assert;
import Allsnake.ServerUIControl;
public class Add100RobotTest {
ServerUIControl test = new ServerUIControl("test");


@Test
    public void test100Robot(){

    for(int i =0 ;i<100;i++){
        test.addRobot();
    }
    Assert.assertEquals(100,test.getPlayerList().length);

}

}
