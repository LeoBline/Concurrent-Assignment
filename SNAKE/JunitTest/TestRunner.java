import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BufferTest.class,
        GameServerTest.class,
        LogicalTest.class,
        LoginTest.class,
        Add100RobotTest.class
})

public class TestRunner {
}
