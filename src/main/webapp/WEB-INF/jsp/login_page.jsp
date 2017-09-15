<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content="#"/>
    <meta name="author" content="#"/>
    <link rel="icon" type="image/x-icon" href="#"/>
    <title>${page_name}</title>

    <jsp:include page="common/top.jsp"/>
</head>
<body>

<jsp:include page="common/navigation.jsp"/>


<div class="vertical-center-70">
    <div class="vertical-center-container">
        <div class="row">
            <div class="col-xs-offset-1 col-xs-10 col-sm-offset-4 col-sm-4 col-md-offset-4 col-md-4 col-lg-offset-4 col-lg-3">
                <form action="#" method="post" accept-charset="utf-8">
                    <div class="form-group">
                        <label class="control-label">Username</label>
                        <input type="email" class="form-control" name="username" value="" placeholder="email"
                               autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false"
                               autofocus="true" required="true"/>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Password</label>
                        <input type="text" class="form-control" name="password" value="" placeholder="password"
                               autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false"
                               required="true"/>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-block">Log In</button>
                    </div>
                </form>
                <div>
                    <a class="pull-left white" href="#">Sign Up</a>
                    <a class="pull-right white" href="#">Forgot your password?</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/bottom.jsp" />
</body>
</html>