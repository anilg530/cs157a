package filehub.demo;

public class GroupMember {
    private String fullName;
    private int permission;

    public GroupMember(String fullName, int permission) {
        this.fullName = fullName;
        this.permission = permission;
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
