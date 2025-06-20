package io.openleap.mrs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"logger"})
public class MessageReceiverServiceApplicationTest {
    @Test
    void contextLoads() {
    }
}
