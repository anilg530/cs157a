<?php
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="#" />
	<meta name="author" content="#" />
	<link rel="icon" type="image/x-icon" href="#">

	<title>File Browser</title>
	<?php include 'application/modules/common/views/top.php'; ?>
</head>
<body>
	<?php include 'application/modules/common/views/menu.php'; ?>
	<div class="row">
		<div class="col-xs-12">
			<div id="root_html">
				<div class="row">
					<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
						<div id="includes_files_table_html">
							<?php if ($is_in_group) { ?>
							<?php echo $this->load->view('includes_files_table', $page_data, true); ?>
							<?php } else { ?>
							<h4>Warning. You are not a member of this group</h4>
							<a href="<?php echo base_url(); ?>">Go Back</a>
							<?php } ?>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
						<div id="includes_drag_drop_file_html">
							<?php echo $this->load->view('includes_drag_drop_file', $page_data, TRUE); ?>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<?php include 'application/modules/common/views/bottom.php'; ?>
	<script src="<?php echo base_url('assets/js/file.js'); ?>"></script>
</body>
</html>