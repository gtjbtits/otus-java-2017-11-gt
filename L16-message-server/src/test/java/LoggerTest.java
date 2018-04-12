import com.jbtits.otus.lecture16.ms.ServerMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTest {
    private static final Logger logger = LogManager.getLogger(ServerMain.class.getName());

    @Test
    public void loggerOutputTest() {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        logger.fatal("This is a fatal message");
    }
}
