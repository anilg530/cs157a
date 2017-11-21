<%@ page import="filehub.demo.MessagingModel" %>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#menu-collapse"
                    aria-expanded="false">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/"><i class="fa fa-file-o"></i> <strong>FileHub</strong></a>
        </div>
        <div id="menu-collapse" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="/group"><i class="fa fa-user"></i> Group Management</a></li>
                <% if (session.getAttribute("username") != null) { %>
                <li><a href="#"><i class="fa fa-cog"></i> My Profile (${sessionScope.username})</a></li>
                <% } %>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <% if (session.getAttribute("username") == null) { %>
                <li><a href="/login"><i class="fa fa-key"></i> Login</a></li>
                <% } else {
                    int message_count = MessagingModel.getNewMessageCount(Integer.toString((int) session.getAttribute("user_id")));
                %>
                <% if (message_count > 0) { %>
                <li><a id="messaging_button" class="messaging_button" href="javascript:;"><i
                        class="fa fa-envelope-o messaging_button"></i> Messaging <span
                        class="badge badge-success filehub_new_message"><% out.print(message_count); %></span></a>
                </li>
                <% } else { %>
                <li><a id="messaging_button" class="messaging_button" href="javascript:;"><i
                        class="fa fa-envelope-o messaging_button"></i> Messaging <span
                        class="badge badge-success filehub_new_message"></span></a>
                </li>
                <% } %>
                <li><a href="/logout"><i class="fa fa-sign-out"></i> Logout</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
<script src="/assets/js/messaging.js"></script>
<jsp:include page="modal.jsp"/>