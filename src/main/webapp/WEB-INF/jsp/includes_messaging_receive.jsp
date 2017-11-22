<%@ page import="java.util.ArrayList" %>
<%@ page import="filehub.demo.CommonModel" %>
<%@ page import="filehub.demo.MessagingModel" %>
<div class="col-xs-12">
    <div class="table-responsive no-wrap-xs">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Message</th>
                <th>From</th>
                <th>Status</th>
                <th>Sent On</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <% ArrayList<ArrayList<String>> received_messages_array = MessagingModel.getAllReceivedMessages(Integer.toString((int) session.getAttribute("user_id")));
                if (!received_messages_array.isEmpty()) {
                    for (ArrayList<String> single_message : received_messages_array) {
                        String received_message = single_message.get(0).replaceAll("(\r\n|\n)", "<br />");
                        String send_from_user_id = single_message.get(1);
                        String message_status = single_message.get(2);
                        String sent_on = CommonModel.timeStampToFormalDate(single_message.get(3));
                        String send_from_full_name = single_message.get(4) + " " + single_message.get(5);
            %>
            <tr>
                <td class="vertical-align-middle">
                    <% out.print(received_message); %>
                </td>
                <td class="vertical-align-middle">
                    <% out.print(send_from_full_name); %>
                </td>
                <td class="vertical-align-middle">
                    <% out.print(message_status); %>
                </td>
                <td class="vertical-align-middle">
                    <% out.print(sent_on); %>
                </td>
                <td class="vertical-align-middle">
                    <a href="javascript:;" data-attr="<% out.print(send_from_user_id); %>"
                       onclick="filehub_send_message(this);" data-toggle="tooltip" data-original-title="Reply"><i class="fa fa-mail-reply"></i></a>
                </td>
            </tr>
            <% } %>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
<% MessagingModel.markAllMessagesAsRead(Integer.toString((int) session.getAttribute("user_id"))); %>

<script>
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip({container: 'body'});

        $('[data-toggle="tooltip"]').on('click', function () {
            $(this).blur();
            $(this).tooltip('hide');
        });
    });
</script>