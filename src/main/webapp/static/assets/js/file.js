var processing = false;
var notEditing = true;
var processingRename = false;
$(document).ready(function () {
    file_reinitialization();
});

function file_reinitialization() {
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});

    $('[data-toggle="tooltip"]').on('click', function () {
        $(this).blur();
        $(this).tooltip('hide');
    });

    processing = false;
    notEditing = true;
}

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
            if (response.status == 'success') {
                //this.removeFile(file);
            }
            else {
                if (response.error) {
                    this.defaultOptions.error(file, response.error);
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
    maxFilesize: 25,
    addRemoveLinks: true,
    dictRemoveFile: 'Clear',
    acceptedFiles: 'image/jpeg,image/png,image/gif,application/pdf,.jpeg,.jpg,.png,.gif,.csv,.xls,.xlsx,.doc,.docx,.pdf,.txt,.psd',
    accept: function (file, done) {
        group_file_upload_file_exist_check(file, done);
    }
};

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
                filehub_refresh_files_table();
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

function filehub_group_file_open_folder(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        var backup_html = $('#includes_files_table_html').html();
        $(object).tooltip('hide');
        var id = $(object).attr('data-attr');
        var formData = {};
        formData['id'] = id;
        $('.form-submit-btn').prop('disabled', true);
        $.ajax({
            type: 'POST',
            url: '/file/open_folder_ajax',
            dataType: 'html',
            data: formData,
            beforeSend: function () {
                //$('#includes_files_table_html').html('<div class="text-center"><img src="/assets/images/preloader.gif" /></div>');
            },
            success: function (response) {
                $('#includes_files_table_html').html(response).promise().done(function () {
                    file_reinitialization();
                    $('.form-submit-btn').prop('disabled', false);
                });
            },
            error: function (xhr, status, error) {
                $('#includes_files_table_html').html(backup_html).promise().done(function () {
                    file_reinitialization();
                    $('.form-submit-btn').prop('disabled', false);
                    swalError('Unable to open folder. There may be internet connectivity issues or you may have enough access privilege.');
                });
                console.log(xhr.responseText);
                //$('body').html(xhr.responseText);
            }
        });
    }
}

function filehub_group_file_previous_folder() {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        var backup_html = $('#includes_files_table_html').html();
        $('.form-submit-btn').prop('disabled', true);
        $.ajax({
            type: 'POST',
            url: '/file/previous_folder_ajax',
            dataType: 'html',
            beforeSend: function () {
                //$('#includes_files_table_html').html('<div class="text-center"><img src="/assets/images/preloader.gif" /></div>');
            },
            success: function (response) {
                $('#includes_files_table_html').html(response).promise().done(function () {
                    file_reinitialization();
                    $('.form-submit-btn').prop('disabled', false);
                });
            },
            error: function (xhr, status, error) {
                $('#includes_files_table_html').html(backup_html).promise().done(function () {
                    file_reinitialization();
                    $('.form-submit-btn').prop('disabled', false);
                    swalError('Unable to go to previous folder. There may be internet connectivity issues or you may have enough access privilege.');
                });
                console.log(xhr.responseText);
                //$('body').html(xhr.responseText);
            }
        });
    }
}

function filehub_refresh_files_table() {
    var backup_html = $('#includes_files_table_html').html();
    $.ajax({
        type: 'GET',
        url: '/file/refresh_files_table',
        dataType: 'html',
        beforeSend: function () {
            //$('#includes_files_table_html').html('<div class="text-center"><img src="/assets/images/preloader.gif" /></div>');
        },
        success: function (response) {
            $('#includes_files_table_html').html(response).promise().done(function () {
                file_reinitialization();
                $('.form-submit-btn').prop('disabled', false);
            });
        },
        error: function (xhr, status, error) {
            $('#includes_files_table_html').html(backup_html).promise().done(function () {
                file_reinitialization();
                $('.form-submit-btn').prop('disabled', false);
                internet_connectivity_swal();
            });
            console.log(xhr.responseText);
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_refresh_files_table_and_header() {
    var backup_html = $('#includes_files_table_html').html();
    $.ajax({
        type: 'GET',
        url: '/file/refresh_files_table',
        dataType: 'html',
        beforeSend: function () {
            //$('#includes_files_table_html').html('<div class="text-center"><img src="/assets/images/preloader.gif" /></div>')
        },
        success: function (response) {
            $('#includes_files_table_html').html(response).promise().done(function () {
                filehub_exit_new_folder_html_ajax();
                file_reinitialization();
                $('.form-submit-btn').prop('disabled', false);
            });
        },
        error: function (xhr, status, error) {
            $('#includes_files_table_html').html(backup_html).promise().done(function () {
                filehub_exit_new_folder_html_ajax();
                file_reinitialization();
                $('.form-submit-btn').prop('disabled', false);
                internet_connectivity_swal();
            });
            console.log(xhr.responseText);
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

                $('#filehub_group_file_new_folder_form').validate({
                    errorPlacement: function (error, element) {
                        error.appendTo($('#filehub_group_file_new_folder_form_error'));
                    }
                });

                $('#filehub_group_file_new_folder_form').on('submit', function (e) {
                    e.preventDefault();
                    var form = $(this);
                    if (form.valid() && notEditing) {
                        filehub_group_file_new_folder_submit(form);
                    }
                    return false;
                });
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

function filehub_group_file_new_folder_submit(form) {
    var serialized = $(form).serialize();
    $('.form-submit-btn').prop('disabled', true);
    $.ajax({
        type: 'POST',
        url: '/file/add_new_folder_submit_ajax',
        dataType: 'json',
        data: serialized,
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                filehub_refresh_files_table_and_header();
            }
            else if (response.error) {
                $('.form-submit-btn').prop('disabled', false);
                setTimeout(function () {
                    swal({
                            html: true,
                            title: 'Oops...',
                            text: response.error,
                            type: 'error',
                            allowOutsideClick: true,
                            showCancelButton: true,
                            showConfirmButton: false,
                            cancelButtonText: 'OK',
                        },
                        function (is_confirm) {
                            if (!is_confirm) {
                                $('#filehub_folder_name').focus();
                            }
                        });
                }, 200);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            $('.form-submit-btn').prop('disabled', false);
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_group_file_delete_folder_submit(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        $(object).tooltip('hide');
        var id = $(object).attr('data-attr');
        var formData = {};
        formData['id'] = id;
        var file_name = $(object).attr('data-attr2');

        setTimeout(function () {
            swal({
                    html: true,
                    title: 'Delete this folder?',
                    text: 'Are you sure you want to delete: <b>' + file_name + '</b>?',
                    type: 'warning',
                    allowOutsideClick: true,
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Delete',
                    cancelButtonText: 'Cancel',
                    closeOnConfirm: true,
                    closeOnCancel: true
                },
                function (is_confirm) {
                    if (is_confirm) {
                        $('.form-submit-btn').prop('disabled', true);
                        $.ajax({
                            type: 'POST',
                            url: '/file/delete_folder_submit_ajax',
                            dataType: 'json',
                            data: formData,
                            beforeSend: function () {
                            },
                            success: function (response) {
                                if (response.status == 'success') {
                                    if (response.toastr) {
                                        toastr.warning(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                                    }
                                    filehub_refresh_files_table();
                                }
                                else if (response.error) {
                                    swalError(response.error);
                                }
                            },
                            error: function (xhr, status, error) {
                                console.log(xhr.responseText);
                                internet_connectivity_swal();
                                $('.form-submit-btn').prop('disabled', false);
                                //$('body').html(xhr.responseText);
                            }
                        });
                    }
                });
        }, 200);
    }
}

function filehub_group_file_delete_file_submit(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        $(object).tooltip('hide');
        var id = $(object).attr('data-attr');
        var formData = {};
        formData['id'] = id;
        var file_name = $(object).attr('data-attr2');
        setTimeout(function () {
            swal({
                    html: true,
                    title: 'Delete this file?',
                    text: 'Are you sure you want to delete: <b>' + file_name + '</b>?',
                    type: 'warning',
                    allowOutsideClick: true,
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Delete',
                    cancelButtonText: 'Cancel',
                    closeOnConfirm: true,
                    closeOnCancel: true
                },
                function (is_confirm) {
                    if (is_confirm) {
                        $('.form-submit-btn').prop('disabled', true);
                        $.ajax({
                            type: 'POST',
                            url: '/file/delete_file_submit_ajax',
                            dataType: 'json',
                            data: formData,
                            beforeSend: function () {
                            },
                            success: function (response) {
                                if (response.status == 'success') {
                                    if (response.toastr) {
                                        toastr.warning(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                                    }
                                    filehub_refresh_files_table();
                                }
                                else if (response.error) {
                                    swalError(response.error);
                                }
                            },
                            error: function (xhr, status, error) {
                                console.log(xhr.responseText);
                                internet_connectivity_swal();
                                $('.form-submit-btn').prop('disabled', false);
                                //$('body').html(xhr.responseText);
                            }
                        });
                    }
                });
        }, 200);
    }
}

function swalError(error_msg) {
    setTimeout(function () {
        swal({
            html: true,
            title: 'Oops...',
            text: error_msg,
            type: 'error',
            allowOutsideClick: true,
            showCancelButton: true,
            showConfirmButton: false,
            cancelButtonText: 'OK',
        });
    }, 200);
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

function filehub_group_file_upload_get_queue_size() {
    var myDropzone = Dropzone.forElement('#dropzone_file_upload');
    return myDropzone.getUploadingFiles().length;
}

function filehub_group_file_upload_folder_rename(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        notEditing = false;
        processingRename = false;
        $(object).tooltip('hide');

        $('.form-submit-btn').prop('disabled', true);
        var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
        dropzoneFileUpload.removeEventListeners();

        var id = $(object).attr('data-attr');

        var rename_span = $("#filehub_folder_rename_span_" + id);
        var old_name = $.trim(rename_span.text());
        var new_input = $('<input>', {
            val: old_name,
            type: 'text',
            class: 'form-control filehub_rename_input_box'
        });

        $(rename_span).replaceWith(new_input);
        new_input.focus();

        $('.filehub_rename_input_box').on('blur', function () {
            if (!processingRename) {
                var new_name = $.trim(new_input.val());
                filehub_group_file_upload_folder_rename_submit(new_name, id, old_name, new_input);
            }
        });

        $('.filehub_rename_input_box').on('keydown', function (event) {
            var x = event.which;
            if (x === 13) {
                event.preventDefault();
                processingRename = true;
                var new_name = $.trim(new_input.val());
                filehub_group_file_upload_folder_rename_submit(new_name, id, old_name, new_input);
            }
            else if (x === 27) {
                filehub_refresh_files_table();
            }
        });
    }
}

function filehub_group_file_upload_folder_rename_submit(folder_name, folder_id, old_name, input_object) {
    var formData = {};
    formData['id'] = folder_id;
    formData['folder_name'] = folder_name;
    $.ajax({
        type: 'POST',
        url: '/file/rename_folder_submit_ajax',
        dataType: 'json',
        data: formData,
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                dropzoneFileUpload.setupEventListeners();
                filehub_refresh_files_table();
            }
            else {
                if (response.error) {
                    setTimeout(function () {
                        swal({
                                html: true,
                                title: 'Oops...',
                                text: response.error,
                                type: 'error',
                                allowOutsideClick: true,
                                showCancelButton: true,
                                showConfirmButton: false,
                                cancelButtonText: 'OK',
                            },
                            function (is_confirm) {
                                if (!is_confirm) {
                                    input_object.val(old_name);
                                    setTimeout(function () {
                                        input_object.focus();
                                        processingRename = false;
                                    }, 200);
                                }
                            });
                    }, 200);
                }
                if (response.swal_error) {
                    var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                    dropzoneFileUpload.setupEventListeners();
                    filehub_refresh_files_table();
                    setTimeout(function () {
                        swal({
                            html: true,
                            title: 'Oops...',
                            text: response.swal_error,
                            type: 'error',
                            allowOutsideClick: true,
                            showCancelButton: true,
                            showConfirmButton: false,
                            cancelButtonText: 'OK',
                        });
                    }, 200);
                }
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
}

function filehub_group_file_edit_file_folder_notes(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        notEditing = false;

        $('.form-submit-btn').prop('disabled', true);
        var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
        dropzoneFileUpload.removeEventListeners();

        $(object).tooltip('hide');
        if ($(object).attr('isEmpty') == 'true') {
            var notes_text = '';
        }
        else {
            var text = $(object).html();
            var regex = /<br\s*[\/]?>/gi;
            var notes_text = text.replace(regex, "\n");
        }
        var id = $(object).attr('data-attr');
        var newTextArea = "<textarea class=\"form-control custom_dynamic_text_area group_file_edit_notes_input_box\" data-attr=\"" + id + "\">" + notes_text + "</textarea>";

        $(object).replaceWith(newTextArea);

        var new_textarea_element = $('.group_file_edit_notes_input_box').get(0);
        var elemLen = new_textarea_element.value.length;
        new_textarea_element.selectionStart = elemLen;
        new_textarea_element.selectionEnd = elemLen;
        autosize($(new_textarea_element));
        new_textarea_element.focus();

        $('.group_file_edit_notes_input_box').on('blur', function () {
            var notes = $(new_textarea_element).val();
            filehub_group_file_edit_file_folder_notes_submit(new_textarea_element, id, notes);
        });
    }
}

function filehub_group_file_edit_file_folder_notes_submit(new_textarea_element, id, notes) {
    var formData = {};
    formData['id'] = id;
    formData['notes'] = notes;
    $.ajax({
        type: 'POST',
        url: '/file/edit_notes_submit_ajax',
        dataType: 'json',
        data: formData,
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                dropzoneFileUpload.setupEventListeners();
                filehub_refresh_files_table();
            }
            else {
                if (response.error) {
                    setTimeout(function () {
                        swal({
                                html: true,
                                title: 'Oops...',
                                text: response.error,
                                type: 'error',
                                allowOutsideClick: true,
                                showCancelButton: true,
                                showConfirmButton: false,
                                cancelButtonText: 'OK',
                            },
                            function (is_confirm) {
                                if (!is_confirm) {
                                    setTimeout(function () {
                                        new_textarea_element.focus();
                                    }, 200);
                                }
                            });
                    }, 200);
                }
                if (response.swal_error) {
                    var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                    dropzoneFileUpload.setupEventListeners();
                    filehub_refresh_files_table();
                    setTimeout(function () {
                        swal({
                            html: true,
                            title: 'Oops...',
                            text: response.swal_error,
                            type: 'error',
                            allowOutsideClick: true,
                            showCancelButton: true,
                            showConfirmButton: false,
                            cancelButtonText: 'OK',
                        });
                    }, 200);
                }
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
}

function group_file_upload_file_exist_check(file, done) {
    if (!processing) {
        processing = true;
        $('.form-submit-btn').prop('disabled', true);
        var file_name = file.name;
        $.ajax({
            type: 'POST',
            url: '/file/file_exist_check',
            dataType: 'json',
            data: {file_name: file_name},
            beforeSend: function () {
            },
            success: function (response) {
                if (response.status == 'success') {
                    done();
                    processing = false;
                }
                else {
                    if (response.file_exist) {
                        setTimeout(function () {
                            swal({
                                    html: true,
                                    title: 'Overwrite?',
                                    text: response.swal_error,
                                    type: 'warning',
                                    allowOutsideClick: true,
                                    showCancelButton: true,
                                    confirmButtonColor: '#DD6B55',
                                    confirmButtonText: 'Overwrite',
                                    cancelButtonText: 'Cancel',
                                    closeOnConfirm: true,
                                    closeOnCancel: true
                                },
                                function (is_confirm) {
                                    if (is_confirm) {
                                        done();
                                    }
                                    else {
                                        done(response.error);
                                    }
                                    processing = false;
                                });
                        }, 200);
                    }
                    else {
                        if (response.error) {
                            done(response.error);
                        }
                        processing = false;
                    }
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                done('Internet connectivity issues. Please try again.');
                processing = false;
            }
        });
        return false;
    }
    else {
        setTimeout(function () {
            group_file_upload_file_exist_check(file, done);
        }, 1500);
    }
}

function filehub_clear_all_files(object) {
    var myDropzone = Dropzone.forElement('#dropzone_file_upload');
    myDropzone.removeAllFiles();
    $(object).blur();
}

function filehub_group_file_upload_file_rename(object) {
    if (notEditing && !processing && filehub_group_file_upload_get_queue_size() <= 0) {
        notEditing = false;
        processingRename = false;
        $(object).tooltip('hide');

        $('.form-submit-btn').prop('disabled', true);
        var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
        dropzoneFileUpload.removeEventListeners();

        var id = $(object).attr('data-attr');

        var rename_span = $("#filehub_file_rename_span_" + id);
        var file_name_with_ext = $.trim(rename_span.text());
        var ext = file_name_with_ext.split('.').pop();
        var old_name = file_name_with_ext.replace(/\.[^/.]+$/, '');
        var new_input = $('<input>', {
            val: old_name,
            type: 'text',
            class: 'form-control filehub_rename_input_box'
        });

        $(rename_span).replaceWith(new_input);
        new_input.focus();

        $('.filehub_rename_input_box').on('blur', function () {
            if (!processingRename) {
                var new_name = $.trim(new_input.val() + "." + ext);
                filehub_group_file_upload_file_rename_submit(new_name, id, old_name, new_input);
            }
        });

        $('.filehub_rename_input_box').on('keydown', function (event) {
            var x = event.which;
            if (x === 13) {
                event.preventDefault();
                processingRename = true;
                var new_name = $.trim(new_input.val() + "." + ext);
                filehub_group_file_upload_file_rename_submit(new_name, id, old_name, new_input);
            }
            else if (x === 27) {
                filehub_refresh_files_table();
            }
        });
    }
}

function filehub_group_file_upload_file_rename_submit(file_name, file_id, old_name, input_object) {
    var formData = {};
    formData['id'] = file_id;
    formData['file_name'] = file_name;
    $.ajax({
        type: 'POST',
        url: '/file/rename_file_submit_ajax',
        dataType: 'json',
        data: formData,
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                dropzoneFileUpload.setupEventListeners();
                filehub_refresh_files_table();
            }
            else {
                if (response.error) {
                    setTimeout(function () {
                        swal({
                                html: true,
                                title: 'Oops...',
                                text: response.error,
                                type: 'error',
                                allowOutsideClick: true,
                                showCancelButton: true,
                                showConfirmButton: false,
                                cancelButtonText: 'OK',
                            },
                            function (is_confirm) {
                                if (!is_confirm) {
                                    input_object.val(old_name);
                                    setTimeout(function () {
                                        input_object.focus();
                                        processingRename = false;
                                    }, 200);
                                }
                            });
                    }, 200);
                }
                if (response.swal_error) {
                    var dropzoneFileUpload = Dropzone.forElement('#dropzone_file_upload');
                    dropzoneFileUpload.setupEventListeners();
                    filehub_refresh_files_table();
                    setTimeout(function () {
                        swal({
                            html: true,
                            title: 'Oops...',
                            text: response.swal_error,
                            type: 'error',
                            allowOutsideClick: true,
                            showCancelButton: true,
                            showConfirmButton: false,
                            cancelButtonText: 'OK',
                        });
                    }, 200);
                }
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
}

function filehub_group_share_file(object) {
    var file_id = $(object).attr('data-attr');
    var formData = {};
    formData['file_id'] = file_id;
    $.ajax({
        type: 'POST',
        url: '/file/group_share_file',
        dataType: 'html',
        data: formData,
        beforeSend: function () {
            //$('#includes_files_table_html').html('<div class="text-center"><img src="/assets/images/preloader.gif" /></div>');
        },
        success: function (response) {
            $('#ajax_modal_body_sm').html(response).promise().done(function () {
            });
            $('#ajax_modal_sm').modal('show');
        },
        error: function (xhr, status, error) {
            internet_connectivity_swal();
            console.log(xhr.responseText);
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function copyToClipboard(object) {
    var element_id = $(object).attr('id');
    var aux = document.createElement("div");
    aux.setAttribute("contentEditable", true);
    aux.innerHTML = document.getElementById(element_id).innerHTML;
    aux.setAttribute("onfocus", "document.execCommand('selectAll',false,null)");
    document.body.appendChild(aux);
    aux.focus();
    document.execCommand("copy");
    document.body.removeChild(aux);
    toastr.success("Copied", null, {'positionClass': 'toast-bottom-right'});
}

function filehub_remove_file_url(object) {
    var file_id = $(object).attr('data-attr');
    var formData = {};
    formData['file_id'] = file_id;
    $.ajax({
        type: 'POST',
        url: '/file/remove_file_url',
        dataType: 'json',
        data: formData,
        beforeSend: function () {
        },
        success: function (response) {
            if (response.status == 'success') {
                if (response.toastr) {
                    toastr.warning(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                }
                $('#ajax_modal_sm').modal('hide');
            }
            else if (response.error) {
                setTimeout(function () {
                    swal({
                        html: true,
                        title: 'Oops...',
                        text: response.error,
                        type: 'error',
                        allowOutsideClick: true,
                        showCancelButton: true,
                        showConfirmButton: false,
                        cancelButtonText: 'OK',
                    });
                }, 200);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            internet_connectivity_swal();
            //$('body').html(xhr.responseText);
        }
    });
}