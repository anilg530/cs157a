<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<div class="row">
    <form action="" id="filehub_send_issue_form" method="post" accept-charset="utf-8">
        <div class="col-sm-12">
            <div class="form-group">
                <label>Describe Your Issues:</label>
                <textarea id="send_issue_textarea" name="message" rows="5"
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
           onclick="filehub_send_issue_submit();">
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

        autosize($('#send_issue_textarea'));

        $('#filehub_send_issue_form').validate({
            errorPlacement: function (error, element) {
                element.after(error);
            }
        });

        setTimeout(function () {
            $('#send_issue_textarea').focus();
        }, 200);
    });

    function filehub_modal_cancel() {
        $('#ajax_modal_sm').modal('hide');
    }
</script>