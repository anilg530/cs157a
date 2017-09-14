$(document).ready(function() {

	Dropzone.options.dropzoneFileUpload = {
		init: function() {
			this.on('addedfile', function(file) {

			});
			this.on('complete', function(file) {

			});
			this.on('queuecomplete', function(file) {
				luova_refresh_user_file_upload();
			});
			this.on('success', function(file, response) {
				var jsonResponse = jQuery.parseJSON(response);
				if (jsonResponse.status == 'success') {
				//this.removeFile(file);
			}
			else {
				if (jsonResponse.error) {
					this.defaultOptions.error(file, jsonResponse.error);
				}
				else {
					this.defaultOptions.error(file, 'Unknown error during upload.');
				}
			}
		});
		},
		url: location.protocol+'//'+window.location.hostname+'/index.php/file/group_files_upload/',
		paramName: 'fileToUpload',
		parallelUploads: 1,
		maxFilesize: 2,
		addRemoveLinks: true,
		dictRemoveFile: 'Clear',
		acceptedFiles: 'image/jpeg,image/png,image/gif,application/pdf,.jpeg,.jpg,.png,.gif,.csv,.xls,.xlsx,.doc,.docx,.pdf,.txt',
		accept: function(file, done) {
			luova_user_file_upload_file_exist_check(file, done);
		}
	};

});