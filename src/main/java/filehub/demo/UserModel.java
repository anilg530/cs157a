package filehub.demo;

/**
 * Created by anilgherra on 9/18/17.
 */
public class UserModel {

    private int id;
    private String username;
    private String password;
    private Groups belongsTo;

    public UserModel(int id, String username, String password, Groups belongsTo ){
        this.id = id;
        this.username = username;
        this.password = password;
        this.belongsTo = belongsTo;
    }
    
    public void setUsername(String username){this.username = username;}

    public String getUsername(){return username;}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public void setPassword(String password){this.password = password;}

    public String getPassword(){return password};

    public void setGroups(Groups belongsTo){this.belongsTo = belongsTo};

    public Groups getGroups(){ return belongsTo;}



}
