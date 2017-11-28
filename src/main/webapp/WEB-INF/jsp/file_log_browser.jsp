<%@ page import="java.util.ArrayList" %>
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
                                <h4>${page_name}</h4>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="row">
                            <hr>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="table-responsive no-wrap-xs">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Action By</th>
                                    <th>Action Info</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% ArrayList<ArrayList<String>> getFileUploadLog = (ArrayList<ArrayList<String>>) request.getAttribute("getFileUploadLog");
                                    if (!getFileUploadLog.isEmpty()) {
                                        for (ArrayList<String> single_log : getFileUploadLog) {
                                            String log_user_id = single_log.get(1);
                                            String log_entry = single_log.get(2);
                                            String sent_on = CommonModel.timeStampToFormalDate(single_log.get(3));
                                            String send_from_full_name = single_log.get(4) + " " + single_log.get(5);
                                %>
                                <tr>
                                    <td class="vertical-align-middle">
                                        <% out.print(send_from_full_name); %>
                                    </td>
                                    <td class="vertical-align-middle">
                                        <% out.print(log_entry); %>
                                    </td>
                                    <td class="vertical-align-middle">
                                        <% out.print(sent_on); %>
                                    </td>
                                </tr>
                                <% } %>
                                <% } else { %>
                                <tr>
                                    <td class="vertical-align-middle" colspan="3">
                                        No File Upload Activity Yet.
                                    </td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/bottom.jsp"/>
</body>
</html>