<%@ page import="java.util.ArrayList" %>
<%@ page import="filehub.demo.GroupModel" %>
<%@ page import="filehub.demo.CommonModel" %>
<%@ page import="filehub.demo.Groups" %>


<div class="col-xs-12">
    <div class="table-responsive no-wrap-xs">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Group Name</th>
                <th>Owner</th>
                <th>Created On</th>
                <th>Permission</th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <%
                ArrayList<Groups> groups = GroupModel.getAllGroups((int) session.getAttribute("user_id"));
                if (groups != null && groups.size() > 0) {
                for(Groups group: groups){
                    int groupID = group.getId();
                    String groupName = group.getGroup_name();
                    String owner = CommonModel.getFullName(Integer.toString(group.getGroup_owner()));
                    String createdOn = group.getCreated_on();
                    int permission = group.getUser_permission();
            %>

            <tr>

                    <td class="vertical-align-middle">
                        <a href="/file/view/<% out.print(groupID); %>" class="icon-black"
                           data-toggle="tooltip"
                           data-original-title="Open" target="_blank">
                        <% out.print(groupName); %>
                        </a>

                    </td>
                    <td class="vertical-align-middle"><% out.print(owner); %></td>
                    <td class="vertical-align-middle"><% out.print(createdOn); %></td>
                    <td class="vertical-align-middle"><% out.print(Integer.toString(permission)); %></td>


            </tr>

            <% }}%>
            </tbody>
        </table>
    </div>
</div>