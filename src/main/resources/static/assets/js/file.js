$(document).ready(function () {
    file_reinitialization();
});

function file_reinitialization() {
    Dropzone.options.dropzoneFileUpload = {
        init: function () {
            this.on('addedfile', function (file) {

            });
            this.on('complete', function (file) {

            });
            this.on('queuecomplete', function (file) {
                filehub_refresh_files_table();
            });
            this.on('success', function (file, response) {
                var jsonResponse = jQuery.parseJSON(response);
                if (jsonResponse.status == 'success') {
                    //this.removeFile(file);
                }
                else {
                    if (jsonResponse.error) {
                        this.defaultOptions.error(file, jsonResponse.error);
                    }
                    else {
                        this.defaultOptions.error(file, 'Unknown error during upload.');
                    }
                }
            });
        },
        url: '/file/group_files_upload/',
        paramName: 'fileToUpload',
        parallelUploads: 1,
        maxFilesize: 2,
        addRemoveLinks: true,
        dictRemoveFile: 'Clear',
        acceptedFiles: 'image/jpeg,image/png,image/gif,application/pdf,.jpeg,.jpg,.png,.gif,.csv,.xls,.xlsx,.doc,.docx,.pdf,.txt',
        accept: function (file, done) {
            group_file_upload_file_exist_check(file, done);
        }
    };

    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
}

function ajaxTest() {
    $.ajax({
        type: 'POST',
        url: '/file/ajax_test',
        dataType: 'json',
        data: {"bob": "cat"},
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                refresh_files_table();
            }
            else if (response.error) {
                var error_array = response.error;
                form.validate().showErrors(error_array);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_refresh_files_table() {
    $.ajax({
        type: 'GET',
        url: '/file/refresh_files_table',
        dataType: 'html',
        beforeSend: function () {
        },
        success: function (response) {
            $('#includes_files_table_html').html(response).promise().done(function () {
                file_reinitialization();
                toastr.success("Files table is refreshed", null, {'positionClass': 'toast-bottom-right'});
            });
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_add_new_folder_html_ajax() {
    $.ajax({
        type: 'GET',
        url: '/file/add_new_folder_html_ajax',
        dataType: 'html',
        beforeSend: function () {
        },
        success: function (response) {
            $('#includes_files_table_header_html').html(response).promise().done(function () {
                $('#filehub_folder_name').focus();
            });
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_exit_new_folder_html_ajax() {
    $.ajax({
        type: 'GET',
        url: '/file/exit_new_folder_html_ajax',
        dataType: 'html',
        beforeSend: function () {
        },
        success: function (response) {
            $('#includes_files_table_header_html').html(response).promise().done(function () {
            });
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function internet_connectivity_swal() {
    setTimeout(function () {
        swal({
            html: true,
            title: 'Oops...',
            text: 'Internet Connectivity Issues. Please try again.',
            type: 'error',
            allowOutsideClick: true,
            showCancelButton: true,
            showConfirmButton: false,
            cancelButtonText: 'OK',
        });
    }, 200);
}