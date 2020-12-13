import Allsnake.Buffer;
import org.junit.Test;
import org.junit.Assert;


public class BufferTest {
    Buffer testBuffer = new Buffer();

    /**
     * This test the basic function of our buffer
     * @throws InterruptedException
     */
    @Test
    public void appendTakeBufferTest() throws InterruptedException{
        //the buffer is empty so if take(2) it will return 2;
        Assert.assertEquals(2, testBuffer.take(2));

        testBuffer.append(1);
        //the buffer's input is 1 so if take(2) if will return the 1;
        Assert.assertEquals(1, testBuffer.take(2));
        testBuffer.append(2);
        testBuffer.append(3);
        //the buffer append 2,3 .so if we take it will return 3;
        Assert.assertEquals(2, testBuffer.take(999));
    }


}
