<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#menu-collapse" aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<?php echo base_url(); ?>"><i class="fa fa-file-o"></i> <strong>FileHub</strong></a>
		</div>
		<div id="menu-collapse" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="<?php echo site_url('login'); ?>"><i class="fa fa-key"></i> Login</a></li>
				<li><a href="<?php echo site_url('group') ?>"><i class="fa fa-user"></i> Group Management</a></li>
				<li><a href="<?php echo site_url('file') ?>"><i class="fa fa-file"></i> Group Files</a></li>
				<li><a href="#"><i class="fa fa-cog"></i> My Profile</a></li>
			</ul>

			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><i class="fa fa-envelope-o"></i><span> Messaging</span></a></li>
			</ul>
		</div>
	</div>
</nav>