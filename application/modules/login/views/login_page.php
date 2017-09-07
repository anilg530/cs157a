<?php 
$is_logged_in_check = $this->login_model->is_logged_in();
if ($is_logged_in_check) {
	$username = $this->login_model->get_logged_in_username();
}
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

	<title>Filehub Login Page</title>
	<?php include 'application/modules/common/views/top.php'; ?>
</head>
<body>

	<div class="vertical-center-70">
		<div class="vertical-center-container">
			<?php if ($is_logged_in_check) { ?>
			<div class="row">
				<div class="col-xs-offset-1 col-xs-10 col-sm-offset-4 col-sm-4 col-md-offset-4 col-md-4 col-lg-offset-4 col-lg-3">
					<div>
						<p>Logged in as: <?php echo $username; ?> <a class="pull-right" href="<?php echo site_url('login/logout') ?>">Logout</a></p>
					</div>
				</div>
			</div>
			<?php } ?>
			<div class="row">
				<div class="col-xs-offset-1 col-xs-10 col-sm-offset-4 col-sm-4 col-md-offset-4 col-md-4 col-lg-offset-4 col-lg-3">
					<form action="<?php echo site_url('login/login_submit') ?>" method="post" accept-charset="utf-8">
						<div class="form-group">
							<label class="control-label">Username</label>
							<input type="email" class="form-control" name="username" value="<?php echo set_value('username'); ?>" placeholder="email" autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false" autofocus required />
						</div>
						<div class="form-group">
							<label class="control-label">Password</label>
							<input type="text" class="form-control" name="password" value="" placeholder="password" autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false" autofocus required />
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-primary btn-block">Log In</button>
						</div>
					</form>
					<div>
						<a class="pull-left white" href="<?php echo site_url('register'); ?>" class="link">Sign Up</a>
						<a class="pull-right white" href="<?php echo site_url('login/forgot_password'); ?>">Forgot your password?</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<?php include 'application/modules/common/views/bottom.php'; ?>
</body>
</html>