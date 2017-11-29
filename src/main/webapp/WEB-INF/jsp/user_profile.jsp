






<!DOCTYPE html>
<html lang="en">

<head>

    <div id="mySidenav" class="sidenav">
        <jsp:include page="common/messaging_sidebar.jsp"/>
    </div>

    <jsp:include page="common/navigation.jsp"/>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <title>User profile </title>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha256-3dkvEK0WLHRJ7/Csr0BZjAWxERc5WH7bdeUya2aXxdU= sha512-+L4yy6FRcDGbXJ9mPG8MT/3UCDzwR9gPeyFNMCtInsol++5m3bk2bXWKdZjvybmohrAsn3Ua5x8gfLnbE1YkOg==" crossorigin="anonymous">
    <!-- Bootstrap Core CSS -->
    <!--     <link href="css/bootstrap.min.css" rel="stylesheet"> -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha256-7s5uDGW3AHqw6xtJmNNtr+OBRJUlgkNJEo78P4b0yRw= sha512-nNo+yCHEyn0smMxSswnf/OnX6/KwJuZTlNZBjauKhTK0c+zT+q5JOCx0UFhXQ6rJR9jg6Es8gPuD2uZcYDLqSw==" crossorigin="anonymous">

    <!-- Custom CSS -->
    <style>


        .othertop{margin-top:10px;}
    </style>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <jsp:include page="common/top.jsp"/>

</head>


<div class="container">
    <div class="row">
        <div class="col-md-10 ">
            <form action = "updateEmail" method = "post" class="form-horizontal">
                <fieldset>

                    <!-- Form Name -->
                    <legend>User profile</legend>

                    <!-- Text input-->

                    <div class="form-group">
                        <label class="col-md-4 control-label">First Name:</label>
                        <div class="col-md-4">
                            <div class="input-group">


                                     <div> <p> ${first_name} </p>
                                     </div>

                            </div>


                        </div>


                    </div>


                    <div class="form-group">
                        <label class="col-md-4 control-label">Last Name:</label>
                        <div class="col-md-4">
                            <div class="input-group">

                                <div> <p> ${last_name} </p>
                                </div>
                            </div>


                        </div>
                    </div>




                    <!-- Text input-->
                    <div class="form-group">
                        <label class="col-md-4 control-label" >Phone number: </label>
                        <div class="col-md-4">
                            <div class="input-group">

                                <div> <p> ${cellphone} </p>
                                </div>
                            </div>


                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="col-md-4 control-label" >Username</label>
                        <div class="col-md-4">
                            <div class="input-group">

                                <input  name="usern" type="text" placeholder= ${username} class="form-control input-md">

                            </div>
                            <div class="form-group">
                                <p class="text-danger">${error_message}</p>
                            </div>

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="col-md-4 control-label" >Password</label>
                        <div class="col-md-4">
                            <div class="input-group">

                                <input  name="password" type="password" placeholder= "new password" class="form-control input-md">

                            </div>

                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label" ></label>
                        <div class="col-md-4">
                            <input type = "submit"  value = "Update" class="btn btn-success">

                        </div>
                    </div>

                </fieldset>
            </form>
        </div>


    </div>
</div>
<!-- jQuery Version 1.11.1 -->
<script src="js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>
<jsp:include page="common/bottom.jsp"/>
</body>

</html>


