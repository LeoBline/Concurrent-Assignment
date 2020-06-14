import Allsnake.Map;
import Allsnake.Snake;
import org.junit.Test;
import org.junit.Assert;
import Allsnake.ServerUIControl;

import java.awt.*;
import java.awt.event.KeyEvent;

public class gameServerTest {
ServerUIControl testServer = new ServerUIControl("test");

    /**
     * Integration Testing
     * Append player keypress event to buffer and see whether the snake move correct on the grid
     * @throws InterruptedException
     * @throws AWTException
     */
    @Test
    public void testServerThread() throws InterruptedException {
        testServer.playerLogin("001","123456");
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        Snake testSnake = testServer.getPlayerList()[0].getSnake();
        Map testMap = testServer.getMap();

        //UP is 0, DOWN = 1; LEFT = 2; RIGHT = 3;
        Assert.assertEquals(0,testServer.getPlayerList()[0].getPlayerBuffer().take(10));
        //Add a snake's movement to buffer
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        int snakeY = testSnake.getSnakeInfo(0,1);
        testServer.updateSnake();
        //wait the thread pool finish job;
        Thread.sleep(100);
        //To determine whether the snake in the direction of the chang
        Assert.assertEquals(0,testSnake.getDirection());
        //To determine whether the snake y position change or not
        Assert.assertEquals(snakeY-1,testSnake.getSnakeInfo(0,1));
        //change all grid note to bonus
        for (int x = 0; x < testMap.getGameSize(); x++) {
            for (int y = 0; y < testMap.getGameSize(); y++) {
                testMap.setMapInfo(x, y, 1);
            }
        }
        testServer.setGridMap(testMap.getGrid());
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        System.out.println(testServer.getPlayerList()[0].getSnake().getRealLength());

        Assert.assertEquals(4,testServer.getPlayerList()[0].getSnake().getLength());
//

//
//        Assert.assertEquals(true,testSnake.getGameover());

    }
}
