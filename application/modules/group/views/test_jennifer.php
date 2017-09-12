
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

	<title>jennifer test page</title>
	<?php include 'application/modules/common/views/top.php'; ?>
</head>
<body>
<?php include 'application/modules/common/views/menu.php'; ?>

<div class="row">
	<div class="col-xs-12">
		<h4><?php echo "hi"; ?></h4>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
		<pre>
		<?php print_r($extra_data); ?>
		</pre>
	</div>
</div>

	<?php include 'application/modules/common/views/bottom.php'; ?>
</body>
</html>