<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<div class="row">
    <div class="col-xs-12">
        <p>Group Name: <strong>${group_name}</strong></p>
    </div>
    <form action="" id="filehub_send_group_invite_form" method="post" accept-charset="utf-8">
        <input type="hidden" name="group_id" value="${group_id}"/>
        <div class="col-xs-12">
            <div class="form-group">
                <label>Send Invite To:</label>
                <input id="send_to_email" name="send_to_email" type="text" class="form-control"
                       autocomplete="off"
                       autocorrect="off"
                       spellcheck="false" value="" required>
            </div>
        </div>
    </form>
</div>
<br>
<div class="row">
    <div class="col-xs-12 form-group">
        <button class="btn btn-sm btn-default" onclick="filehub_modal_cancel();" type="button">
            Cancel
        </button>
        <a class="btn btn-sm btn-primary pull-right" href="javascript:;"
           onclick="filehub_send_group_invite_submit();">
            <i class="fa fa-paper-plane-o"></i> Send</a>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip({container: 'body'});

        $('[data-toggle="tooltip"]').on('click', function () {
            $(this).blur();
            $(this).tooltip('hide');
        });

        $('#filehub_send_group_invite_form').validate({
            errorPlacement: function (error, element) {
                element.after(error);
            }
        });

        setTimeout(function () {
            $('#send_to_email').focus();
        }, 200);

        $('#send_to_email').devbridgeAutocomplete({
            serviceUrl: '/messaging/send_message_autocomplete_suggestions',
            deferRequestBy: 400,
            onSelect: function (suggestion) {
                //alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
                $('#send_to_email').val(suggestion.data);
                setTimeout(function () {
                    $('#send_to_email').blur();
                }, 200);
            },
            formatResult: function (suggestion, currentValue) {
                var suggestion_string = suggestion.value + " (" + suggestion.data + ")";
                var currentValue = new RegExp("(" + currentValue + ")", "gi");
                return suggestion_string.replace(currentValue, "<strong>$1</strong>");
            }
        });
    });

    function filehub_modal_cancel() {
        $('#ajax_modal_sm').modal('hide');
    }
</script>