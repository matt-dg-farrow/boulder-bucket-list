package com.bae.tests.domain;

import org.junit.Assert;
import org.junit.Test;

import com.bae.persistence.domain.User;

public class UserTest {

	private User testUser = new User("testName","testPassword");

	@Test
	public void getUsernameTest() {
		Assert.assertEquals("testName", testUser.getUsername());
	}

	@Test
	public void getHashedPasswordTest() {
		Assert.assertEquals("testPassword", testUser.getHashedPassword());
	}

}
