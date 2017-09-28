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

<jsp:include page="common/navigation.jsp"/>

<div class="row">
    <div class="col-xs-12">
        <div id="root_html">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
                    <div id="includes_files_table_html">
                        <jsp:include page="includes_files_table.jsp"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
                    <div id="includes_drag_drop_file_html">
                        <jsp:include page="includes_drag_drop_file.jsp"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/bottom.jsp" />
<script src="/assets/js/file.js"></script>
</body>
</html>