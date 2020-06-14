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

        //UP is 0, DOWN = 1; LEFT = 2; RIGHT = 3;
        Assert.assertEquals(0,testServer.getPlayerList()[0].getPlayerBuffer().take(10));
        //Add a snake's movement to buffer
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        int snakeY = testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1);
        testServer.updateSnake();
        //wait the thread pool finish job;
        Thread.sleep(10);
        //To determine whether the snake in the direction of the chang
        Assert.assertEquals(0,testServer.getPlayerList()[0].getSnake().getDirection());
        //To determine whether the snake y position change or not
        Assert.assertEquals(snakeY-1,testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1));
//         testServer = new ServerUIControl("test");
        //change all grid note to bonus

        Thread.sleep(10);

        //Place 3 1 point bonuses in front of the first snake
        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),
                testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-1);
        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),
                testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-2);
        testServer.getPlayerList()[0].getSnake().placeBonus(testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,0),
                testServer.getPlayerList()[0].getSnake().getSnakeInfo(0,1)-3);


        //Three steps forward.
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(10);
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(10);
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_UP);
        testServer.updateSnake();
        Thread.sleep(10);

        //Test whether snake do get the bonus
        Assert.assertEquals(4,testServer.getPlayerList()[0].getSnake().getRealLength());

        //Login second player
        testServer.playerLogin("002","123456");
        testServer.getPlayerList()[1].judgeInput(KeyEvent.VK_W);

        //See whether the player's action added into buffer
        Assert.assertEquals(0,testServer.getPlayerList()[1].getPlayerBuffer().take(10));

        //Place 2 1 point bonuses in front of the second snake
        testServer.getPlayerList()[1].getSnake().placeBonus(testServer.getPlayerList()[1].getSnake().getSnakeInfo(0,0),
                testServer.getPlayerList()[1].getSnake().getSnakeInfo(0,1)-1);
        testServer.getPlayerList()[1].getSnake().placeBonus(testServer.getPlayerList()[1].getSnake().getSnakeInfo(0,0),
                testServer.getPlayerList()[1].getSnake().getSnakeInfo(0,1)-2);

        //Two steps forward.
        testServer.getPlayerList()[1].judgeInput(KeyEvent.VK_W);
        testServer.updateSnake();
        Thread.sleep(10);
        testServer.getPlayerList()[1].judgeInput(KeyEvent.VK_W);
        testServer.updateSnake();
        Thread.sleep(10);

        //Whether snake get the points
        Assert.assertEquals(3,testServer.getPlayerList()[1].getSnake().getRealLength());


        //Simulate a snake walking in a circle,which will cause the dead of the snake
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_RIGHT);
        testServer.updateSnake();
        Thread.sleep(10);
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_DOWN);
        testServer.updateSnake();
        Thread.sleep(10);
        testServer.getPlayerList()[0].judgeInput(KeyEvent.VK_LEFT);
        testServer.updateSnake();
        Thread.sleep(10);

        //Whether the snake is dead
        Assert.assertEquals(true,testServer.getPlayerList()[0].getSnake().getGameover());
    }
}
