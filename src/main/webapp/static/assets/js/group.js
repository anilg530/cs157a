$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});

    $('[data-toggle="tooltip"]').on('click', function () {
        $(this).blur();
        $(this).tooltip('hide');
    });
});



function group_delete(object) {
    $(object).tooltip('hide');
    var groupName = $(object).attr('data-attr');
    setTimeout(function () {
        swal({
                html: true,
                title: 'Group Deletion',
                text: 'Group <b>' + groupName + '</b> will be deleted.',
                type: 'warning',
                allowOutsideClick: true,
                showCancelButton: true,
                confirmButtonColor: '#DD6B55',
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                closeOnConfirm: true,
                closeOnCancel: true,

            },
            function (is_confirm) {
                if (is_confirm) {
                    console.log("confirmed")

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
            confirmGroupPassword(result, groupName, groupId)

        }
    }), 200);
}

function confirmGroupPassword(object, groupName, groupId){
    console.log("pass " + object);
    console.log("group name " + groupName);
    console.log("group id " + groupId);
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
}


