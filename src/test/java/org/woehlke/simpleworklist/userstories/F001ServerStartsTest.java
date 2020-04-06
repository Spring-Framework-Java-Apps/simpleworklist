package org.woehlke.simpleworklist.userstories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class F001ServerStartsTest {

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testStartup(){
        log.info("testStartup");
    }
}
