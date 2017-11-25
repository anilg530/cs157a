package filehub.demo;

public class GroupInvite {
    private String inviteCode;
    private int groupId;
    private String groupName;
    private int fromUser;
    private int accessLevel;

    public GroupInvite(String inviteCode, int groupId, String groupName, int fromUser, int accessLevel) {
        this.inviteCode = inviteCode;
        this.groupId = groupId;
        this.groupName = groupName;
        this.fromUser = fromUser;
        this.accessLevel = accessLevel;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
