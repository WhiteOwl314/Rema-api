package seongju.remaapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seongju.remaapi.config.jwt.JWTHolder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTHolderTest {
    @Autowired
    private JWTHolder jwtHolder;

    @Test
    void yamlFileTest(){
        String secret = jwtHolder.getSecret();

        System.out.println("Secret :" + secret);
        assertAll(
                () -> assertEquals(secret, "1234567890")
        );
    }

}