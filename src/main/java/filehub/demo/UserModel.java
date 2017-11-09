package filehub.demo;

/**
 * Created by anilgherra on 9/18/17.
 */

public class UserModel {


    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;

    public UserModel(String username, String password, String firstname, String lastname, String phone, String email){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
    }
    /
    public void setUsername(String username){this.username = username;}

    public String getUsername(){return username;}



    public void setPassword(String password){this.password = password;}

    public String getPassword(){return password;}

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
