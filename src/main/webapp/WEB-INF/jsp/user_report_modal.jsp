<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<div class="row">
    <form action="" id="filehub_send_report_form" method="post" accept-charset="utf-8">
        <div class="col-xs-12">
            <div class="form-group">
                <label>User you are reporting:</label>
                <input id="report_id" name="report_id" type="text" class="form-control"
                       autocomplete="off"
                       autocorrect="off"
                       spellcheck="false" value="" required>
            </div>
        </div>
        <div class="col-sm-12">
            <div class="form-group">
                <label>Describe Your Issue with User:</label>
                <textarea id="send_report_textarea" name="message" rows="5"
                          class="form-control custom_dynamic_text_area" maxlength="255" required></textarea>
            </div>
        </div>
    </form>
</div>
<br>
<div class="row">
    <div class="col-sm-12 form-group">
        <button class="btn btn-sm btn-default" onclick="filehub_modal_cancel();" type="button">
            Cancel
        </button>
        <a class="btn btn-sm btn-primary pull-right" href="javascript:;"
           onclick="filehub_user_report_submit();">
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

        setTimeout(function () {
            $('#report_id').focus();
        }, 200);

        $('#report_id').devbridgeAutocomplete({
            serviceUrl: '/messaging/send_message_autocomplete_suggestions',
            deferRequestBy: 400,
            onSelect: function (suggestion) {
                //alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
                $('#report_id').val(suggestion.data);
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

        autosize($('#send_issue_textarea'));

        $('#filehub_send_report_form').validate({
            errorPlacement: function (error, element) {
                element.after(error);
            }
        });
    });

    function filehub_modal_cancel() {
        $('#ajax_modal_sm').modal('hide');
    }
</script>