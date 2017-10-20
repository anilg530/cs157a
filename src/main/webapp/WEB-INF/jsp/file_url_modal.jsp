<div class="row">
    <div class="col-sm-12 form-group">
        <p style="font-size: 14px;">Share the following URL to allow that person to have access to the file:
            <b>${file_name}</b></p>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <pre style="font-size: 16px; text-align: center;"><code id="url_code" class="cursor-hand"
                                                                onclick="copyToClipboard(this);" data-toggle="tooltip"
                                                                data-original-title="Copy"></code></pre>
    </div>
</div>
<br>
<div class="row">
    <div class="col-sm-12 form-group">
        <button class="btn btn-sm btn-danger" data-attr="${file_id}" onclick="filehub_remove_file_url(this);">
            Remove URL
        </button>
        <button class="btn btn-sm btn-default pull-right" onclick="cancel();">
            Close
        </button>
    </div>
</div>

<script>
    $(document).ready(function () {
        var url = location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/file/open_file/' + '${file_url}';
        $('#url_code').html(url);

        $('[data-toggle="tooltip"]').tooltip({container: 'body'});

        $('[data-toggle="tooltip"]').on('click', function () {
            $(this).blur();
            $(this).tooltip('hide');
        });
    });

    function cancel() {
        $('#ajax_modal_sm').modal('hide');
    }
</script>