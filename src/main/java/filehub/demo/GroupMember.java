package filehub.demo;

public class GroupMember {
    private String fullName;
    private int permission;
    private int user_id;

    public GroupMember(String fullName, int permission, int user_id) {
        this.fullName = fullName;
        this.permission = permission;
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

}
