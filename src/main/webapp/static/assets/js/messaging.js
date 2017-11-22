var last_message_count = "0";
$(document).ready(function () {
    $('body').click(function (e) {
        var container = $("#mySidenav");
        if ($(e.target).hasClass('messaging_button') && !$('#ajax_modal_sm').is(':visible')) {
            toggleNav();
        }
        else if (!container.is(e.target) && container.has(e.target).length === 0 && !$('#ajax_modal_sm').is(':visible')) {
            closeNav();
        }
    });

    $('#messaging_button').on('click', function () {
        $(this).blur();
    });

    filehub_refresh_message_count();
});

/* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
function toggleNav() {
    if (document.getElementById("mySidenav").style.width == "" || document.getElementById("mySidenav").style.width == "0" || document.getElementById("mySidenav").style.width == "0px" || document.getElementById("mySidenav").style.width == "0%") {
        document.getElementById("mySidenav").style.width = "250px";
        document.getElementById("root_html").style.marginLeft = "250px";
    }
    else {
        document.getElementById("mySidenav").style.width = "0";
        document.getElementById("root_html").style.marginLeft = "0";
    }
}

function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
    document.getElementById("root_html").style.marginLeft = "250px";
}

/* Set the width of the side navigation to 0 and the left margin of the page content to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("root_html").style.marginLeft = "0";
}

function refresh_messaging_sidebar() {
    $.ajax({
        type: 'GET',
        url: '/messaging/refresh_files_table',
        dataType: 'html',
        beforeSend: function () {
        },
        success: function (response) {
            $('#mySidenav').html(response).promise().done(function () {
            });
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            //$('body').html(xhr.responseText);
        }
    });
    return false;
}

function filehub_send_message(object) {
    var formData = {};
    var send_to = $(object).attr('data-attr');
    if (send_to) {
        formData['send_to'] = send_to;
    }
    $.ajax({
        type: 'POST',
        url: '/messaging/send_message',
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

function filehub_send_message_submit() {
    var form = $('#filehub_send_message_form');
    form.validate().settings.ignore = ':disabled,:hidden';
    if (form.valid()) {
        var serialized = $(form).serialize();
        $.ajax({
            type: 'POST',
            url: '/messaging/send_message_submit',
            dataType: 'json',
            data: serialized,
            beforeSend: function () {
            },
            success: function (response) {
                if (response.status == 'success') {
                    filehub_refresh_sent_messages();
                    if (response.toastr) {
                        toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                    }
                    $('#ajax_modal_sm').modal('hide');
                }
                else if (response.swal_error) {
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
                if (response.error) {
                    $('#send_to_email').focus();
                    var error_array = jQuery.parseJSON(response.error);
                    form.validate().showErrors(error_array);
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                internet_connectivity_swal();
                //$('body').html(xhr.responseText);
            }
        });
    }
    return false;
}

function filehub_refresh_message_count() {
    var refreshTime = 10000; // in milliseconds, so 10 seconds
    window.setInterval(function () {
        $.ajax({
            type: 'GET',
            url: '/messaging/refresh_message_count',
            dataType: 'json',
            beforeSend: function () {
            },
            success: function (response) {
                if (response.data && response.data > 0 && last_message_count != response.data) {
                    $('.filehub_new_message').text(response.data).promise().done(function () {
                    });
                }
                else if (response.data && response.data <= 0) {
                    $('.filehub_new_message').text('').promise().done(function () {
                    });
                }
                if (last_message_count != response.data) {
                    last_message_count = response.data;
                    filehub_refresh_received_messages();
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                //$('body').html(xhr.responseText);
            }
        });
    }, refreshTime);
}

function filehub_refresh_received_messages() {
    if ($('#received_html').length != 0) {
        $.ajax({
            type: 'GET',
            url: '/messaging/refresh_received_messages',
            dataType: 'html',
            beforeSend: function () {
            },
            success: function (response) {
                $('#received_html').html(response).promise().done(function () {
                });
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                //$('body').html(xhr.responseText);
            }
        });
    }
    return false;
}

function filehub_refresh_sent_messages() {
    if ($('#sent_html').length != 0) {
        $.ajax({
            type: 'GET',
            url: '/messaging/refresh_sent_messages',
            dataType: 'html',
            beforeSend: function () {
            },
            success: function (response) {
                $('#sent_html').html(response).promise().done(function () {
                });
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                //$('body').html(xhr.responseText);
            }
        });
    }
    return false;
}

function filehub_send_issue() {
    var formData = {};
    $.ajax({
        type: 'POST',
        url: '/messaging/send_issue',
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

function filehub_send_issue_submit() {
    var form = $('#filehub_send_issue_form');
    form.validate().settings.ignore = ':disabled,:hidden';
    if (form.valid()) {
        var serialized = $(form).serialize();
        $.ajax({
            type: 'POST',
            url: '/messaging/send_issue_submit',
            dataType: 'json',
            data: serialized,
            beforeSend: function () {
            },
            success: function (response) {
                if (response.status == 'success') {
                    if (response.toastr) {
                        toastr.success(response.toastr, null, {'positionClass': 'toast-bottom-right'});
                    }
                    $('#ajax_modal_sm').modal('hide');
                }
                else if (response.swal_error) {
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
                if (response.error) {
                    var error_array = jQuery.parseJSON(response.error);
                    form.validate().showErrors(error_array);
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                internet_connectivity_swal();
                //$('body').html(xhr.responseText);
            }
        });
    }
    return false;
}