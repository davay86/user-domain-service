package com.babcock.user.api.payload;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDetailsTest {

    UserDetails userDetails;

    @Before
    public void setup(){
        userDetails = new UserDetails();
    }

    @Test
    public void getUsername() throws Exception {
        userDetails.setUsername("testUsername");
        Assert.assertEquals("testUsername",userDetails.getUsername());
    }

    @Test
    public void getFirstname() throws Exception {
        userDetails.setFirstname("testFirstname");
        Assert.assertEquals("testFirstname",userDetails.getFirstname());
    }

    @Test
    public void getLastname() throws Exception {
        userDetails.setLastname("testLastname");
        Assert.assertEquals("testLastname", userDetails.getLastname());
    }

    @Test
    public void getJsonString() throws Exception {
        userDetails.setUsername("testUsername");
        userDetails.setFirstname("testFirstname");
        userDetails.setLastname("testLastname");

        String expectedString = "{ \"username\" : \"testUsername\"," +
                "\"firstname\" : \"testFirstname\"," +
                "\"lastname\" : \"testLastname\"" +
                "}";

        Assert.assertEquals(expectedString, userDetails.getJsonString());
    }

}