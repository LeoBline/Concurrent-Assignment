import Allsnake.Map;
import Allsnake.ServerUIControl;
import org.junit.Test;
import org.junit.Assert;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LogicalTest {
    Map testMap = new Map();
    ServerUIControl testServerUI = new ServerUIControl("Test");
    //Create to robot to act as a real player
    Robot robot = new Robot();

    public LogicalTest() throws AWTException {
    }

    /**
     * Test if the eatFood method do change the grid information to 0 and return the false when empty
     */
    @Test
    public void eatFoodTest(){
        //Make sure there will be food to eat
        for (int x = 0; x < testMap.getGameSize(); x++){
            for(int y = 0; y < testMap.getGameSize(); y++){
                testMap.getGrid()[x][y] = 1;
            }
        }
        testMap.eatFood(1,1);
        //The position should be 0 when it has a bonus and been eat by snake
        Assert.assertEquals(0,testMap.getGrid()[1][1]);
        Assert.assertEquals(false,testMap.eatFood(1,1));
    }

    /**
     * Random place a bonus in map see whether the bonus will be in map
     */
    @Test
    public void randomPlaceBonusTest(){
        //Make sure the map is empty which means no bonus
        for (int x = 0; x < testMap.getGameSize(); x++){
            for(int y = 0; y < testMap.getGameSize(); y++){
                testMap.getGrid()[x][y] = 0;
            }
        }
        testServerUI.setGridMap(testMap.getGrid());
        testServerUI.placeBonus(1);

        //The flag to show Whether the map have bonus
        Boolean existFlag = false;
        //Find out the bonus and if there is a bonus in map change the exitFlag to true
        for (int x = 0; x < testMap.getGameSize(); x++){
            for(int y = 0; y < testMap.getGameSize(); y++){
                if(testMap.getGrid()[x][y] == 1){
                    existFlag = true;
                }
            }
        }
        Assert.assertTrue(existFlag);
    }

    /**
     *Press space should stop the game and esc should exit the game
     */
    @Test
    public void exitAndPauseTest(){
        System.out.println(testServerUI.isPaused());
        robot.mouseMove(25,20);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        System.out.println(testServerUI.isPaused());
    }

}
