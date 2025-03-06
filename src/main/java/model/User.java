package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private int role;

    public User() {
    }

    public User(int id, String username, String password, String name, String lastname, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {return this.name;}

    public void setName(String name) {this.name = name;}

    public String getLastName() {return this.lastname;}

    public void setLastName(String lastname) {this.lastname = lastname;}

    public int getRole() {return this.role;}

    public void setRole(int role) {this.role = role;}

    public String toString() {
        return "User{id=" + this.id + ", username='" + this.username + "', password='" + this.password
                + "', name='" + this.name + "', lastname='" + this.lastname + "', role=" + this.role +"'}";
    }
}
