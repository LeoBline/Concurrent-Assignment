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
        System.out.println(" "+testServer.getMap().getMapInfo(testSnake.getSnakeInfo(0,0), testSnake.getSnakeInfo(0,1)-1));
        testServer.updateSnake();
        //wait the thread pool finish job;
        Thread.sleep(100);
        //To determine whether the snake in the direction of the chang
        Assert.assertEquals(0,testSnake.getDirection());
        //To determine whether the snake y position change or not
        Assert.assertEquals(snakeY-1,testSnake.getSnakeInfo(0,1));
//         testServer = new ServerUIControl("test");
        //change all grid note to bonus

        Thread.sleep(100);
//        System.out.println(" the food is"+testMap.getMapInfo(testSnake.getSnakeInfo(0,0), testSnake.getSnakeInfo(0,1)-1));
//        System.out.println("food: "+testSnake.getSnakeInfo(0,0)+" "+(snakeY-2));
//        System.out.println("Snake: "+testSnake.getSnakeInfo(0,0)+" "+testSnake.getSnakeInfo(0,1));

        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-1);
        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-2);
        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-3);

        testServer.updateSnake();
        Thread.sleep(100);

//        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(100);
//        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(100);
//        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(100);
        System.out.println(testServer.getPlayerList()[0].getSnake().setSnakearray()[1][1]);

        Assert.assertEquals(4,testServer.getPlayerList()[0].getSnake().getRealLength());
//

//
//        Assert.assertEquals(true,testSnake.getGameover());

    }
}
