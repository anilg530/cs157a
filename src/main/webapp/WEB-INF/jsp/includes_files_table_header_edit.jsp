<form action="#" id="filehub_group_file_new_folder_form" method="post" accept-charset="utf-8">
    <div class="flex-new-folder-row-reverse">
        <div class="flex-new-folder-column">
            <div class="flex-new-folder-row">
                <input id="filehub_folder_name" name="folder_name" type="text"
                       class="form-control flex-new-folder-child auto-width"
                       autocomplete="off" autocorrect="off" spellcheck="false" value=""
                       placeholder="new folder name" autofocus required>
                <button class="btn btn-default btn-sm form-submit-btn flex-new-folder-child" type="button"
                        onclick="filehub_exit_new_folder_html_ajax();"><i class="fa fa-times"></i> Cancel
                </button>
                <button class="btn btn-default btn-sm form-submit-btn flex-new-folder-child" type="submit"><i
                        class="fa fa-folder"></i> Add Folder
                </button>
            </div>
            <div class="flex-new-folder-row">
                <div class="text-danger" id="filehub_group_file_new_folder_form_error"
                     style="padding-left: 12px;"></div>
            </div>
        </div>
    </div>
</form>