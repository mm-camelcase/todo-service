package com.mm.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;

import com.mm.todo.config.TestSecurityConfig;


@SpringBootTest
//@ActiveProfiles("test")	// A profile is used where security is disabled
@Import(TestSecurityConfig.class)
class TodoApplicationTests {

	@Test
	void contextLoads() {
	}

}
