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
    setTimeout( bootbox.prompt({
        size: "small",
        title: "Enter Password",
        callback: function(result){

            console.log(result);
        }
    }), 200);
}


