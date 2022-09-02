package util;

import com.dzhenetl.util.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggerTest {

    private final Logger logger = Logger.getInstance();

    @Test
    void loggerInstanceShouldBeNotEmpty() {
        Assertions.assertNotNull(logger);
    }
}
