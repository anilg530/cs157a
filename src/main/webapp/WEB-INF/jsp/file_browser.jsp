<%@ page import="filehub.demo.CommonModel" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content="#"/>
    <meta name="author" content="#"/>

    <title>${page_name}</title>

    <jsp:include page="common/top.jsp"/>
</head>
<body>
<div id="mySidenav" class="sidenav">
    <jsp:include page="common/messaging_sidebar.jsp"/>
</div>
<div id="root_html">
    <jsp:include page="common/navigation.jsp"/>
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-2 col-md-8">
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-xs-6">
                                <h4>Group: ${group_name}</h4>
                            </div>
                            <div class="col-xs-6">
                                <div id="includes_files_table_header_html">
                                    <jsp:include page="includes_files_table_header.jsp"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="row">
                            <hr>
                        </div>
                    </div>
                    <div id="includes_files_table_html">
                        <jsp:include page="includes_files_table.jsp"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-2 col-md-8">
                    <div id="includes_drag_drop_file_html">
                        <jsp:include page="includes_drag_drop_file.jsp"/>
                    </div>
                </div>
            </div>
            <%
                int temp_user_id = (int) session.getAttribute("user_id");
                int temp_group_id = (int) session.getAttribute("group_id");
                String temp_user_access = CommonModel.getUserPermissionsString(Integer.toString(temp_user_id), Integer.toString(temp_group_id));
                if (!temp_user_access.isEmpty()) {
            %>
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-2 col-md-8">
                    <div class="col-xs-12">
                        <br>
                        <small class="pull-right">Access Level: <% out.print(temp_user_access); %></small>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

<jsp:include page="common/bottom.jsp"/>
<script src="/assets/js/file.js"></script>
</body>
</html>