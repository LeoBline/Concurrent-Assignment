import Allsnake.Buffer;
import org.junit.Test;
import org.junit.Assert;


public class BufferTest {
    Buffer testbuffer = new Buffer();

    @Test
    public void AppendTakeBufferTest() throws InterruptedException{
        //the buffer is empty so if take(2) it will return 2;
        Assert.assertEquals(2,testbuffer.take(2));

        testbuffer.append(1);
        //the buffer's input is 1 so if take(2) if will return the 1;
        Assert.assertEquals(1,testbuffer.take(2));
        testbuffer.append(2);
        testbuffer.append(3);
        //the buffer append 2,3 .so if we take it will return 3;
        Assert.assertEquals(3,testbuffer.take(999));
    }
}
