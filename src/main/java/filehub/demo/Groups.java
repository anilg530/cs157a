package filehub.demo;

public class Groups {
    private int id;
    private String group_name;
    private int group_owner;
    private String group_password;
    private String group_status;
    private String created_on;
    private int user_permission;


    public Groups(int id, String group_name, int group_owner, String group_password, String group_status, String created_on) {
        this.id = id;
        this.group_name = group_name;
        this.group_owner = group_owner;
        this.group_password = group_password;
        this.group_status = group_status;
        this.created_on = created_on;
    }

    public Groups(int id, int user_permission,  String group_name, int group_owner,  String created_on) {
        this.id = id;
        this.group_name = group_name;
        this.group_owner = group_owner;
        this.user_permission = user_permission;
        this.created_on = created_on;
    }

    @Override
    public String toString() {
        return "Group[id:"+id+",group name:"+group_name+",group owner:"+group_owner+
                ",group password:"+group_password+",group status:"+group_status+",created on:"+created_on+"]";
    }

    public int getUser_permission(){
        return this.user_permission;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_owner() {
        return group_owner;
    }

    public void setGroup_owner(int group_owner) {
        this.group_owner = group_owner;
    }

    public String getGroup_password() {
        return group_password;
    }

    public void setGroup_password(String group_password) {
        this.group_password = group_password;
    }

    public String getGroup_status() {
        return group_status;
    }

    public void setGroup_status(String group_status) {
        this.group_status = group_status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }


}
