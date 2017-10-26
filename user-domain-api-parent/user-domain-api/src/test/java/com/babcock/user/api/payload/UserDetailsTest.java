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
    public void getId(){
        userDetails.setId(123L);
        Assert.assertEquals(123,userDetails.getId());
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
    public void isActive(){
        userDetails.setActive(true);
        Assert.assertTrue(userDetails.isActive());
    }

    @Test
    public void getJsonString() throws Exception {
        userDetails = new UserDetails("testUsername","testFirstname","testLastname");

        String expectedString = "{ \"username\" : \"testUsername\"," +
                "\"firstname\" : \"testFirstname\"," +
                "\"lastname\" : \"testLastname\"" +
                "}";

        Assert.assertEquals(expectedString, userDetails.getJsonString());
    }

}