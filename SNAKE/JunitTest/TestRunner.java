import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BufferTest.class,
        GameServerTest.class,
        LogicalTest.class,
        LoginTest.class
})

public class TestRunner {
}
