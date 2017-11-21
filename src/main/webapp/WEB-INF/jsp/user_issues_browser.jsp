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
                                    <th>Issues By</th>
                                    <th>Issue Message</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% ArrayList<ArrayList<String>> getUserIssues = (ArrayList<ArrayList<String>>) request.getAttribute("getUserIssues");
                                    if (!getUserIssues.isEmpty()) {
                                        for (ArrayList<String> single_issue : getUserIssues) {
                                            String issue_user_id = single_issue.get(1);
                                            String issue_message = single_issue.get(2);
                                            String sent_on = CommonModel.timeStampToFormalDate(single_issue.get(3));
                                            String issue_from_full_name = single_issue.get(4) + " " + single_issue.get(5);
                                %>
                                <tr>
                                    <td class="vertical-align-middle">
                                        <% out.print(issue_from_full_name); %>
                                    </td>
                                    <td class="vertical-align-middle">
                                        <% out.print(issue_message); %>
                                    </td>
                                    <td class="vertical-align-middle">
                                        <% out.print(sent_on); %>
                                    </td>
                                </tr>
                                <% } %>
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