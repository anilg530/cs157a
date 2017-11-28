<%@ page import="java.util.ArrayList" %>
<%@ page import="filehub.demo.FileModel" %>
<%@ page import="filehub.demo.CommonModel" %>
<%@ page import="filehub.demo.MessagingModel" %>
<% if (session.getAttribute("user_id") != null) {
    String temp_user_id = Integer.toString((Integer) session.getAttribute("user_id"));
    ArrayList<ArrayList<String>> quick_group_members = MessagingModel.getGroupMembersForMessaging(temp_user_id);
%>
<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
<% int message_count = MessagingModel.getNewMessageCount(Integer.toString((int) session.getAttribute("user_id"))); %>
<% if (message_count > 0) { %>
<a href="/messaging"><i class="fa fa-envelope-o"></i> View Messages <span
        class="badge badge-success filehub_new_message"><%
    out.print(message_count); %></span></a>
<% } else { %>
<a href="/messaging"><i class="fa fa-envelope-o"></i> View Messages <span
        class="badge badge-success filehub_new_message"></span></a>
<% } %>
<a href="#" onclick="filehub_send_message(this);"><i class="fa fa-paper-plane-o"></i> Send Message</a>
<% if (!quick_group_members.isEmpty()) { %>
<br>
<h4>Quick Send:</h4>
<% for (ArrayList<String> single_member : quick_group_members) {
    String temp_group_user_id = single_member.get(0);
    String group_member_full_name = single_member.get(1) + " " + single_member.get(2);
    String group_member_email = CommonModel.getEmailByUserID(temp_group_user_id); %>
<a class="sub-a" href="#" data-attr="<% out.print(temp_group_user_id); %>" onclick="filehub_send_message(this);"><%
    out.print(group_member_full_name); %></a>
<p><%
    out.print(group_member_email); %></p>
<% }
}
} %>