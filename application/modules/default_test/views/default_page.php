<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="#" />
	<meta name="author" content="#" />
	<link rel="icon" type="image/x-icon" href="#">

	<title>Default Page</title>
	<?php include 'application/modules/common/views/top.php'; ?>
</head>
<body>
	<div class="row">
		<div class="col-xs-12 cs-sm-12 col-md-12 col-lg-6">
			<p>Base url: <?php echo base_url(); ?></p>
			<p>Site url: <?php echo site_url(); ?></p>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 cs-sm-12 col-md-12 col-lg-6">
			<pre>
				<?php print_r($users_array); ?>
			</pre>
		</div>
	</div>
	<?php include 'application/modules/common/views/bottom.php'; ?>
</body>
</html>