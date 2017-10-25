<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<div class="row">
    <form action="" id="filehub_send_message_form" method="post" accept-charset="utf-8">
        <div class="col-sm-12">
            <div class="form-group">
                <label>Send To:</label>
                <c:choose>
                    <c:when test="${not empty send_to_username}">
                        <input id="send_to_email" name="send_to_email" type="text" class="form-control"
                               autocomplete="off"
                               autocorrect="off"
                               spellcheck="false" value="${send_to_username}" required>
                    </c:when>
                    <c:otherwise>
                        <input id="send_to_email" name="send_to_email" type="text" class="form-control"
                               autocomplete="off"
                               autocorrect="off"
                               spellcheck="false" value="" required>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col-sm-12">
            <div class="form-group">
                <label>Message:</label>
                <c:choose>
                    <c:when test="${not empty send_to_username}">
                    <textarea id="send_message_textarea" name="message" rows="5"
                              class="form-control custom_dynamic_text_area" required></textarea>
                    </c:when>
                    <c:otherwise>
                    <textarea id="send_message_textarea" name="message" rows="5"
                              class="form-control custom_dynamic_text_area" required></textarea>
                    </c:otherwise>
                </c:choose>
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
           onclick="filehub_send_message_submit();">
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

        autosize($('#send_message_textarea'));

        $('#filehub_send_message_form').validate({
            errorPlacement: function (error, element) {
                element.after(error);
            }
        });

        <c:choose>
        <c:when test="${not empty send_to_username}">
        setTimeout(function () {
            $('#send_message_textarea').focus();
        }, 200);
        </c:when>
        <c:otherwise>
        setTimeout(function () {
            $('#send_to_email').focus();
        }, 200);
        </c:otherwise>
        </c:choose>

        $('#send_to_email').devbridgeAutocomplete({
            serviceUrl: '/messaging/send_message_autocomplete_suggestions',
            deferRequestBy: 400,
            onSelect: function (suggestion) {
                //alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
                $('#send_to_email').val(suggestion.data);
                setTimeout(function () {
                    $('#send_message_textarea').focus();
                }, 200);
            },
            formatResult: function (suggestion, currentValue) {
                var suggestion_string = suggestion.value + " (" + suggestion.data + ")";
                var currentValue = new RegExp("(" + currentValue + ")","gi");
                return suggestion_string.replace(currentValue, "<strong>$1</strong>");
            }
        });
    });

    function filehub_modal_cancel() {
        $('#ajax_modal_sm').modal('hide');
    }
</script>