package com.romanskochko.taxi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class TaxiApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
	}

}
