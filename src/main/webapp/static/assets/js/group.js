$(document).ready(function () {
    init();
});

function init(){
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});

    $('[data-toggle="tooltip"]').on('click', function () {
        $(this).blur();
        $(this).tooltip('hide');
    });
}

function group_delete(object) {
    $(object).tooltip('hide');
    var groupName = $(object).attr('data-attr');
    var groupId = $(object).attr('data-attr2');
    var groupOwner = $(object).attr('data-attr3');
    setTimeout(function () {
        swal({
                html: true,
                title: 'Group Deletion',
                text: 'Group <b>' + groupName + '</b> will be deleted.',
                type: 'warning',
                allowOutsideClick: true,
                showCancelButton: true,
                confirmButtonColor: '#dd2420',
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                closeOnConfirm: true,
                closeOnCancel: true,

            },
            function (is_confirm) {
                if (is_confirm) {
                    console.log("confirmed");
                    console.log("group id" + groupId );
                    console.log("group owner" + groupOwner);

                    setTimeout( bootbox.prompt({
                        size: "small",
                        title: "Enter Password",
                        inputType: "password",
                        callback: function(result){
                            if(result!=null) {
                                confirmGroupDeletePassword(result, groupName, groupId, groupOwner);
                            }

                        }
                    }), 200);


                }
            });
    }, 200);
}

function group_password(object) {
    $(object).tooltip('hide');
    var groupName = $(object).attr('data-attr');
    var groupId = $(object).attr('data-attr2');
    setTimeout( bootbox.prompt({
        size: "small",
        title: "Enter Password",
        inputType: "password",
        callback: function(result){
            if(result!=null) {
                confirmGroupPassword(result, groupName, groupId);
            }

        }
    }), 200);
}

function confirmDeleteGroup(groupId, groupOwner) {
    $.ajax({
        type: 'POST',
        url: '/group/delete_group',
        dataType: 'json',
        data: {
            groupId:groupId,
            groupOwner:groupOwner
        },
        beforeSend: function () {
        },
        success: function (response) {
            if(response.status == 'success'){
                successToast(response.title, response.content);
                window.location.reload();
            }else{
                errorToast(response.title, response.content);
            }
        },
        error: function (xhr, status, error) {

        }
    });
}
function confirmGroupPassword(object, groupName, groupId){
    $.ajax({
        url: '/group/confirmpass/'+groupName+'/'+object,
        type: 'GET',
        dataType: "text",
        beforeSend: function () {

        },
        success: function (response) {
            console.log("success")
            console.log("response  = "+response.toString())
            if(response.toString()=="true"){
                successToast("Success", "group login successfull");
                window.location='/file/view/'+groupId;
            }else{
                errorToast("Error", "Wrong Password!");
            }
        },
        error: function (xhr, status, error) {

        }
    });

}

function confirmGroupDeletePassword(object, groupName, groupId, groupOwner){
    $.ajax({
        url: '/group/confirmpass/'+groupName+'/'+object,
        type: 'GET',
        dataType: "text",
        beforeSend: function () {

        },
        success: function (response) {
            console.log("success")
            console.log("response  = "+response.toString())
            if(response.toString()=="true"){
                successToast("Success", "group login successfull");
                confirmDeleteGroup(groupId, groupOwner);
            }else{
                errorToast("Error", "Wrong Password!");
            }
        },
        error: function (xhr, status, error) {

        }
    });

}
function successToast(title, content){
    toastr.success(content, title, {
        "timeOut": "2000",
        "extendedTImeout": "0"
    });
}
function errorToast(title, content){
    toastr.error(content, title, {
        "timeOut": "2000",
        "extendedTImeout": "0"
    });
}

function refreshGroupTable() {
    $.ajax({
        type: 'GET',
        url: '/group/refresh_group_table',
        dataType: 'html',
        beforeSend: function () {

        },
        success: function (response) {
            console.log("refeshing");
            console.log(response);
            //$("#includes_group_table").children().remove();
            $('#includes_group_table_html').html(response);



        },
        error: function (xhr, status, error) {

        }
    });
}

function filehub_send_group_invite(object) {
    var formData = {};
    var group_name = $(object).attr('data-attr');
    var group_id = $(object).attr('data-attr2');
    if (group_id && group_name) {
        formData['group_name'] = group_name;
        formData['group_id'] = group_id;
    }
    $.ajax({
        type: 'POST',
        url: '/group/send_group_invite',
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

function filehub_send_group_invite_submit() {
    var form = $('#filehub_send_group_invite_form');
    form.validate().settings.ignore = ':disabled,:hidden';
    if (form.valid()) {
        var serialized = $(form).serialize();

        $.ajax({
            type: 'POST',
            url: '/group/send_group_invite_submit',
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

function join_a_group_popup(object){
    var formData = {};
    var user_id = $(object).attr('data-attr');
    if (user_id) {
        formData['user_id'] = user_id;
    }
    $.ajax({
        type: 'POST',
        url: '/group/join_a_group',
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

function submit_join_group() {
    console.log("in submit_join_group")
    var form = $('#join_group_form');
    if (form.valid()) {
        var serialized = $(form).serialize();

        $.ajax({
            type: 'POST',
            url: '/group/submit_join_group',
            dataType: 'json',
            data: serialized,
            beforeSend: function () {
            },
            success: function (response) {
                var group_id= response.group_id;
                var code = response.code;
                var group_name = response.group_name;
                if(response.status=="success" && response.invitation=="yes"){
                    setTimeout(function () {
                        swal({
                                html: true,
                                title: 'Invitation found',
                                text: response.content + ". Click Confirm to accept invitation and join the group.",
                                type: 'warning',
                                allowOutsideClick: true,
                                showCancelButton: true,
                                confirmButtonColor: '#dd2420',
                                confirmButtonText: 'Confirm',
                                cancelButtonText: 'Cancel',
                                closeOnConfirm: true,
                                closeOnCancel: true,
                            },
                            function (is_confirm) {
                                if (is_confirm) {
                                    $.ajax({
                                        type: 'POST',
                                        url: 'invite_code/'+code,
                                        dataType: 'html',
                                        data: {
                                            group_id: group_id,
                                            group_name: group_name
                                        },
                                        beforeSend: function () {
                                        },
                                        success: function (response) {
                                            data=$(response).find('div#main_row');
                                            $('#ajax_modal_body_sm').html(data).promise().done(function () {
                                            });
                                            $('#ajax_modal_sm').modal('show');
                                        },
                                        error: function (xhr, status, error) {
                                            internet_connectivity_swal();
                                            console.log(xhr.responseText);
                                        }
                                    });

                                }
                            });
                    }, 200);

                }else if(response.status=="success" && response.invitation=="no"){
                    //successToast(response.status, response.content);
                    swal(response.status, response.content, "success");
                    window.location='/group';

                }else if(response.status=="success" && response.member=="yes"){
                    setTimeout(function () {
                        swal({
                                html: true,
                                title: 'You already join this group',
                                text: response.content + " Click Confirm to view the group.",
                                type: 'warning',
                                allowOutsideClick: true,
                                showCancelButton: true,
                                confirmButtonColor: '#dd2420',
                                confirmButtonText: 'Confirm',
                                cancelButtonText: 'Cancel',
                                closeOnConfirm: true,
                                closeOnCancel: true,
                            },
                            function (is_confirm) {
                                if (is_confirm) {
                                    window.location='/file/view/'+group_id;
                                }
                            });
                    }, 200);
                }else{
                    swal(response.status, response.content, "error")
                    return false;
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                internet_connectivity_swal();
            }
        });
    }
    return false;
}

function submit_add_group() {
    console.log("in submit_join_group")
    var form = $('#add_group_form');
    if (form.valid()) {
        var serialized = $(form).serialize();

        $.ajax({
            type: 'POST',
            url: '/group/create_group/add',
            dataType: 'json',
            data: serialized,
            beforeSend: function () {
            },
            success: function (response) {
                if(response.status=="success"){
                    swal(response.status, response.content, "success");
                    window.location='../group';
                }else {
                    if(response.type=="mysql" || response.type=="name exist"){
                        swal(response.status, response.content, "error");
                    } else {
                        $(this).html(response.content).promise().done(function () {
                        });
                    }
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                internet_connectivity_swal();
            }
        });
    }
    return false;
}


function dataOnchange(object) {
    var permission =  object.options[object.selectedIndex].value;
    var userId = $(object).attr('data-attr');
    var fullName = $(object).attr('data-attr1');
    var groupId = $(object).attr('data-attr2');
    //alert("permission " + selectedValue + "user id "+ userId);
    setTimeout(function () {
        swal({
                html: true,
                title: 'Update user permission',
                text: 'Update <b>' + fullName + '</b> to <b>' +  getPermissionString(permission) +'</b> permission.',
                type: 'warning',
                allowOutsideClick: true,
                showCancelButton: true,
                confirmButtonColor: '#dd2420',
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                closeOnConfirm: true,
                closeOnCancel: true,

            },
            function (is_confirm) {
                if (is_confirm) {
                    updatePermision(userId, groupId, permission)
                }
            });
    }, 200);

}

function updatePermision(userId, groupId, userPermission){
    $.ajax({
        type: 'POST',
        url: '/group/permission/update',
        dataType: 'json',
        data: {
            userId:userId,
            groupId:groupId,
            userPermission:userPermission
        },
        beforeSend: function () {
        },
        success: function (response) {
            if(response.status == 'success'){
                successToast(response.title, response.content);
                window.location.reload();
            }else{
                errorToast(response.title, response.content);
            }
        },
        error: function (xhr, status, error) {

        }
    });
}


function getPermissionString(code) {
    code = Number(code);
    var str = "";
    switch (code){
        case 1:
            str = "Guest";
            break;
        case 2:
            str = "User";
            break;
        case 3:
            str = "Advanced User";
            break;
        case 4:
            str = "Admin";
            break;

    }

    return str;
}

function userDelete(object){
    $(object).tooltip('hide');

    var userId = $(object).attr('data-attr');
    var fullName = $(object).attr('data-attr2');
    var groupId = $(object).attr('data-attr3');
    var groupName = $(object).attr('data-attr4');
    setTimeout(function () {
        swal({
                html: true,
                title: 'User Deletion',
                text: 'User <b>' + fullName + '</b> will be deleted from group <b>' + groupName + '</b>. Are you sure ?',
                type: 'warning',
                allowOutsideClick: true,
                showCancelButton: true,
                confirmButtonColor: '#dd2420',
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                closeOnConfirm: true,
                closeOnCancel: true,

            },
            function (is_confirm) {
                if (is_confirm) {
                    $.ajax({
                        type: 'POST',
                        url: '/group/members/delete',
                        dataType: 'json',
                        data: {
                            groupId:groupId,
                            userId:userId,
                            fullName:fullName
                        },
                        beforeSend: function () {
                        },
                        success: function (response) {
                            if(response.status == 'success'){
                                successToast(response.title, response.content);
                                window.location.reload();
                            }else{
                                errorToast(response.title, response.content);
                            }
                        },
                        error: function (xhr, status, error) {

                        }
                    });


                }
            });
    }, 200);

}
