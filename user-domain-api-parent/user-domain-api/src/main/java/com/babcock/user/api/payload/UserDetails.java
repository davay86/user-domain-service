package com.babcock.user.api.payload;

public class UserDetails {

    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private boolean active;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserDetails() {
    }

    public UserDetails(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getJsonString(){
         return "{ \"username\" : \""+ username +"\"," +
                "\"firstname\" : \""+ firstname +"\"," +
                "\"lastname\" : \"" + lastname + "\"" +
                "}";
    }

}
