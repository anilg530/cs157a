<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ page import="filehub.demo.CommonModel" %>
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
        <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-2 col-md-8">
            <div class="row">
                <div class="col-xs-12">
                    <div class="row">
                        <div class="col-xs-6">
                            <% if (session.getAttribute("username") != null) {
                                String fullName = CommonModel.getFullName(Integer.toString((int) session.getAttribute("user_id")));%>
                            <h4><%out.print(fullName);%>'s Group</h4>
                            <% } %>
                        </div>
                        <div class="col-xs-6">
                            <div class="btn-group pull-right">
                                <a class="btn btn-default" href="/group/create_group">Create a Group</a>
                                <a class="btn btn-default" href="#">Join a Group</a>
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
                    <jsp:include page="includes_group_table.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<jsp:include page="common/bottom.jsp"/>
<script src="/assets/js/group.js"></script>
<script src="/assets/third_party/bootbox/bootbox.js"></script>

</body>
</html>



