import org.junit.Test;
import org.junit.Assert;
import Allsnake.ServerUIControl;
public class Add100RobotTest {
ServerUIControl test = new ServerUIControl("test");


@Test
    public void test100Robot(){

    //add 100 rebot
    for(int i =0 ;i<10;i++){
        //every time it will add 10 robot
        test.addRobot();
    }
    Assert.assertEquals(100,test.getPlayerList().length);

}

}
