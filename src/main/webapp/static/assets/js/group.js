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

