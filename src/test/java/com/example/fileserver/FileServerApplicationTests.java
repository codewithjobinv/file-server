package com.example.fileserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"app.security.user.name=testuser",
		"app.security.user.password=testpass",
		"file.storage.location=/tmp/uploads-test"
})
class FileServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
