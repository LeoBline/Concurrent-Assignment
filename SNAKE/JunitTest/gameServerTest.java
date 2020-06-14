import org.junit.Test;
import org.junit.Assert;
import Allsnake.ServerUIControl;

import java.awt.*;
import java.awt.event.KeyEvent;

public class gameServerTest {
ServerUIControl testServer = new ServerUIControl("test");
@Test
public void testServerThread() throws InterruptedException, AWTException {
    testServer.playerLogin("001","123456");
    testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);

    //UP is 0, DOWN = 1; LEFT = 2; RIGHT = 3;
    Assert.assertEquals(0,testServer.getPlayerList()[0].getPlayerBuffer().take(10));
    testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
    int snakedateY = testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1);
    testServer.updateSnake();
    //wait the thread pool finish job;
    Thread.sleep(100);
//    //To determine whether the snake in the direction of the chang
    Assert.assertEquals(0,testServer.getPlayerList()[0].getSnake().getDirection());
    //To determine whether the snake y position of the chang
    Assert.assertEquals(snakedateY-1,testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1));
}

}
