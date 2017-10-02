<%@ page import="java.util.ArrayList" %>
<%@ page import="filehub.demo.FileModel" %>
<%@ page import="filehub.demo.CommonModel" %>
<%
    String current_path = (String) session.getAttribute("current_path");
    if (!FileModel.isInRootDIR(session, current_path)) {
        String folder_name = FileModel.getTopLevelFolder(current_path);
%>
<div class="col-xs-12">
    <div class="row">
        <div class="col-xs-6">
            <h4>Folder: <% out.print(folder_name); %></h4>
        </div>
        <div class="col-xs-6">
            <button class="btn btn-default btn-sm pull-right" type="button" onclick="filehub_group_file_previous_folder();"><i class="fa fa-arrow-left"></i> Previous
                Folder
            </button>
        </div>
    </div>
</div>
<% } %>
<div class="col-xs-12">
    <div class="table-responsive">
        <table class="table table-striped">
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
                ArrayList<String> folder_directory = (ArrayList<String>) request.getAttribute("folder_directory");
                if (folder_directory != null && folder_directory.size() > 0) {
                    for (String folder : folder_directory) {
                        ArrayList<String> tempFolderInfo = FileModel.getFolderInfo(session, folder);
                        if (tempFolderInfo != null && tempFolderInfo.size() > 0) {
                            String temp_folder_id = tempFolderInfo.get(0);
                            String temp_folder_name = tempFolderInfo.get(2);
                            String uploaded_on = CommonModel.timeStampToFormalDate(tempFolderInfo.get(9));
                            String uploaded_by = CommonModel.getFullName(tempFolderInfo.get(10));
                            String notes = tempFolderInfo.get(7).replaceAll("(\r\n|\n)", "<br />");
                            String notes_by = CommonModel.getFullName(tempFolderInfo.get(8));
            %>
            <tr>
                <td class="vertical-align-middle">
                    <span id="user_file_upload_folder_rename_span_<% out.print(temp_folder_id); %>"
                          class="text-nowrap" data-attr="<% out.print(temp_folder_id); %>">
                    <a href="javascript:;" class="icon-black"
                       data-attr="<% out.print(temp_folder_id); %>"
                       onclick="filehub_group_file_open_folder(this);"
                       data-toggle="tooltip"
                       data-original-title="Open"><i
                            class="fa fa-folder-o"></i>&nbsp;<% out.print(temp_folder_name); %></a>
                </span>
                </td>
                <td class="vertical-align-middle"><% out.print(uploaded_by); %></td>
                <td class="vertical-align-middle"><% out.print(uploaded_on); %></td>
                <td class="vertical-align-middle"><% out.print(notes); %></td>
                <td class="vertical-align-middle"><% out.print(notes_by); %></td>
                <td class="vertical-align-middle">
                    <a class="btn no-padding" href="javascript:;" data-attr="<% out.print(temp_folder_id); %>"
                       onclick="group_file_upload_folder_rename(this);" data-toggle="tooltip"
                       data-original-title="Rename"><i class="fa fa-pencil"></i></a>
                    <a class="btn no-padding" href="javascript:;" data-attr="<% out.print(temp_folder_id); %>"
                       data-attr2="<% out.print(temp_folder_name); %>" onclick="filehub_group_file_delete_folder_submit(this);"
                       data-toggle="tooltip" data-original-title="Delete"><i class="fa fa-trash-o"></i></a>
                </td>
            </tr>
            <% }
            %>
            <%
                    }
                } %>
            </tbody>
        </table>
    </div>
</div>