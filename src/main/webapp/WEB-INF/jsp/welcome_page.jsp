<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">




    <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Open+Sans|Candal|Alegreya+Sans">
    <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="assets/css/imagehover.min.css">
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
</head>

<body>
<!--Navigation bar-->
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">File<span>Hub</span></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">

                <li><a href="#organisations">Organisations</a></li>
                <li><a href="#courses">link2</a></li>
                <li><a href="#pricing">link1</a></li>
                <li><a href="form" >Sign in</a></li>

            </ul>
        </div>
    </div>
</nav>
<!--/ Navigation bar-->
<!--Modal box-->
<div class="modal fade" id="login" role="dialog">
    <div class="modal-dialog modal-sm">

        <!-- Modal content no 1-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title text-center form-title">Login</h4>
            </div>
            <div class="modal-body padtrbl">

                <div class="login-box-body">
                    <p class="login-box-msg">Sign in to start your session</p>
                    <div class="form-group">
                        <form name="" id="loginForm">
                            <div class="form-group has-feedback">
                                <!----- username -------------->
                                <input class="form-control" placeholder="Username" id="loginid" type="text" autocomplete="off" />
                                <span style="display:none;font-weight:bold; position:absolute;color: red;position: absolute;padding:4px;font-size: 11px;background-color:rgba(128, 128, 128, 0.26);z-index: 17;  right: 27px; top: 5px;" id="span_loginid"></span>
                                <!---Alredy exists  ! -->
                                <span class="glyphicon glyphicon-user form-control-feedback"></span>
                            </div>
                            <div class="form-group has-feedback">
                                <!----- password -------------->
                                <input class="form-control" placeholder="Password" id="loginpsw" type="password" autocomplete="off" />
                                <span style="display:none;font-weight:bold; position:absolute;color: grey;position: absolute;padding:4px;font-size: 11px;background-color:rgba(128, 128, 128, 0.26);z-index: 17;  right: 27px; top: 5px;" id="span_loginpsw"></span>
                                <!---Alredy exists  ! -->
                                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                            </div>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="checkbox icheck">
                                        <label>
                                            <input type="checkbox" id="loginrem" > Remember Me
                                        </label>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <button type="button" class="btn btn-green btn-block btn-flat" onclick="userlogin()">Sign In</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<!--/ Modal box-->
<!--Banner-->
<div class="banner">
    <div class="bg-color">
        <div class="container">
            <div class="row">
                <div class="banner-text text-center">
                    <div class="text-border">
                        <h2 class="text-dec">File Sharing Made Simple</h2>
                    </div>
                    <div class="intro-para text-center quote">
                        <p class="big-text">Share today.. Share tomorrow..</p>
                        <p class="small-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Laudantium enim repellat sapiente quos architecto<br>Laudantium enim repellat sapiente quos architecto</p>
                        <a href="form" class="btn get-quote">Sign up Today</a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<!--/ Banner-->
<!--Feature-->
<section id="feature" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="header-section text-center">
                <h2>Features</h2>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Exercitationem nesciunt vitae,<br> maiores, magni dolorum aliquam.</p>
                <hr class="bottom-line">
            </div>
            <div class="feature-info">
                <div class="fea">
                    <div class="col-md-4">
                        <div class="heading pull-right">
                            <h4>Latest Technologies</h4>
                            <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
                        </div>
                        <div class="fea-img pull-left">
                            <i class="fa fa-css3"></i>
                        </div>
                    </div>
                </div>
                <div class="fea">
                    <div class="col-md-4">
                        <div class="heading pull-right">
                            <h4>Toons Background</h4>
                            <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
                        </div>
                        <div class="fea-img pull-left">
                            <i class="fa fa-drupal"></i>
                        </div>
                    </div>
                </div>
                <div class="fea">
                    <div class="col-md-4">
                        <div class="heading pull-right">
                            <h4>Award Winning Design</h4>
                            <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
                        </div>
                        <div class="fea-img pull-left">
                            <i class="fa fa-trophy"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>



</form>

</div>
</div>
</section>
<!--/ Contact-->
<!--Footer-->
<footer id="footer" class="footer">
</footer>
<!--/ Footer-->

<script src="js/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/custom.js"></script>
<script src="contactform/contactform.js"></script>

</body>

</html>
