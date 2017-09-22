<%@ page import="java.util.ArrayList" %><%--<?php--%>
<%--$root_path = $this->file_model->get_root_dir();--%>
<%--//$get_files = $this->file_model->user_file_upload_get_files($uuid, $current_path);--%>
<%--$folders_list_array = $this->file_model->get_folder_list($current_path);--%>
<%--?>--%>
<div class="col-xs-12">
    <div class="row">
        <div class="col-xs-6">
            <h4>Folder: temp folder name</h4>
        </div>
        <div class="col-xs-6">
            <button class="btn btn-default btn-sm pull-right" type="button"><i class="fa fa-arrow-left"></i> Previous
                Folder
            </button>
        </div>
    </div>
</div>
<div class="col-xs-12">
    <div class="table-responsive">
        <table class="table table-striped mb-none">
            <thead>
            <tr>
                <th>File Name</th>
                <th>Uploaded By</th>
                <th>Uploaded On</th>
                <th>Notes</th>
                <th>Notes By</th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <%
                ArrayList<String> folderList = (ArrayList<String>) request.getAttribute("folderList");

                for (String folderName : folderList) {
            %>
            <tr>
                <td><%=folderName%>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>