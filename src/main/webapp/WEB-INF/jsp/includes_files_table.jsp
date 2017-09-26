<?php
$root_path = $this->file_model->get_root_dir();
//$get_files = $this->file_model->user_file_upload_get_files($uuid, $current_path);
$folders_list_array = $this->file_model->get_folder_list($current_path);
?>
<?php
if ($current_path != $root_path) {
$folder_name = basename($current_path);
?>
<div class="col-xs-12">
    <h4 class="form-control-inline">Folder: <?php echo $folder_name; ?></h4>
    <button class="btn btn-default btn-sm" type="button"><i class="fa fa-arrow-left"></i> Previous Folder</button>
</div>
<?php } ?>
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
            <?php if (!empty($folders_list_array) || !empty($get_files)) { ?>
            <?php } else { ?>
            <tr>
                <td colspan="6"><span>no files added yet</span></td>
            </tr>
            <?php } ?>
            </tbody>
        </table>
    </div>
</div>