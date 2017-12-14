<%@ page import="java.util.ArrayList" %>
<%@ page import="filehub.demo.GroupModel" %>
<%@ page import="filehub.demo.CommonModel" %>
<%@ page import="filehub.demo.Groups" %>
<%@ page import="filehub.demo.GroupMember" %>
<%@ page import="java.util.HashMap" %>


<div class="col-xs-12">
    <div class="table-responsive no-wrap-xs">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Member</th>
                <th>Permission</th>
                <th>Action</th>
            </tr>
            </thead>

            <tbody>
            <%
                int user_id = (int) session.getAttribute("user_id");
                int group_id = (int) request.getAttribute("group_id");
                System.out.println("group id" + group_id);
                ArrayList<GroupMember> members = GroupModel.getAllGroupMembers(group_id);
                System.out.println("size: " + members.size());
                if (members != null && members.size() > 0) {
                    for (GroupMember member : members) {
                        int userId = member.getUser_id();
                        String fullName = member.getFullName();
                        int userPermission = member.getPermission();
                        String userPermissionString = GroupModel.getPermissionString(userPermission);

                        System.out.println("full name" + fullName);
                        System.out.println("userPermissionString" + userPermissionString);
            %>

            <tr>

                <td class="vertical-align-middle"><% out.print(fullName); %></td>

                <td class="vertical-align-middle">
                    <% if (userPermission != 4 && userId != user_id) { %>
                    <select name="invite_access_level" id="invite_access_level"
                            class="selectpicker show-tick form-control" data-attr="<% out.print(userId); %>"
                            data-attr1="<% out.print(fullName); %>" data-attr2="<% out.print(group_id); %>"
                            autocomplete="off" data-size="10" required onchange="dataOnchange(this);">
                        <%
                            HashMap<String, String> getUserPermissionsType = CommonModel.getUserPermissionsType();
                            for (HashMap.Entry<String, String> entry : getUserPermissionsType.entrySet()) {
                                String user_permission_id = entry.getKey();
                                String user_permission_formal = entry.getValue(); %>
                        <option value="<% out.print(user_permission_id); %> "
                                <% if (userPermission == Integer.valueOf(user_permission_id)) { %>
                                SELECTED<%}%> ><%out.print(user_permission_formal); %></option>
                        <% } %>

                    </select>
                    <%} else { %>
                    <% out.print(userPermissionString); %>
                    <%}%>
                </td>
                <td class="vertical-align-middle">
                    <%if (!GroupModel.isOwner(userPermission)) {%>

                    <a class="btn no-padding" href="javascript:;"
                       data-attr="<% out.print(userId); %>" data-attr2="<% out.print(fullName); %>"
                       data-attr3="<% out.print(group_id); %>"
                       data-attr4="<% out.print(GroupModel.getGroupName(group_id)); %>"
                       onclick="userDelete(this);" data-toggle="tooltip" data-original-title="Delete"><i
                            class="fa fa-trash-o"></i></a>

                    <%}%>
                </td>
            </tr>
            <% }
            } else {%>

            <tr>
                <td colspan="6">No member found.</td>
            </tr>
            <% }%>
            </tbody>
        </table>
    </div>
</div>

<script>
    function userDelete(object) {
        $(object).tooltip('hide');

        var userId = $(object).attr('data-attr');
        var fullName = $(object).attr('data-attr2');
        var groupId = $(object).attr('data-attr3');
        var groupName = $(object).attr('data-attr4');
        setTimeout(function () {
            swal({
                    html: true,
                    title: 'User Deletion',
                    text: 'User <b>' + fullName + '</b> will be deleted from group <b>' + groupName + '</b>. Are you sure ?',
                    type: 'warning',
                    allowOutsideClick: true,
                    showCancelButton: true,
                    confirmButtonColor: '#dd2420',
                    confirmButtonText: 'Confirm',
                    cancelButtonText: 'Cancel',
                    closeOnConfirm: true,
                    closeOnCancel: true,

                },
                function (is_confirm) {
                    if (is_confirm) {
                        $.ajax({
                            type: 'POST',
                            url: '/group/members/delete',
                            dataType: 'json',
                            data: {
                                groupId: groupId,
                                userId: userId,
                                fullName: fullName
                            },
                            beforeSend: function () {
                            },
                            success: function (response) {
                                if (response.status == 'success') {
                                    successToast(response.title, response.content);
                                    window.location.reload();
                                } else {
                                    errorToast(response.title, response.content);
                                }
                            },
                            error: function (xhr, status, error) {

                            }
                        });


                    }
                });
        }, 200);

    }
</script>



