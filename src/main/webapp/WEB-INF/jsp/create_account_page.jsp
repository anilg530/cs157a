<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<head>   <jsp:include page="common/top.jsp"/>
</head>

<body> <jsp:include page="common/navigation.jsp"/></body>


<div class="container">

    <div class="row">
        <div class="col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">
            <form action = "sign-up" method ="post">
                <h2>FileHub Registration</h2>
                <hr/>
                <div class="row">
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <div class="form-group">
                            <input type="text" name="firstname"  class="form-control input-lg" placeholder="First Name" tabindex="1" autocomplete="off" required>
                        </div>
                    </div>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <div class="form-group">
                            <input type="text" name="lastname"  class="form-control input-lg" placeholder="Last Name" tabindex="2" autocomplete="off" required>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <input type="email" name="username"  class="form-control input-lg" placeholder="email" tabindex="3" autocomplete="off" required>
                </div>
                <div class="form-group">
                    <input type="phone" name="phone"  class="form-control input-lg" placeholder="Phone Number" tabindex="4" autocomplete="off" required>
                </div>
                <div class="form-group">
                    <input type="password" name="password"  class="form-control input-lg" placeholder="password" tabindex="5" autocomplete="off" required>
                </div>
                <div class="form-group">
                    <p class="text-danger">${error_message}</p>
                </div>
                <hr/>
                <div class="row">
                    <div class="col-xs-6 col-md-6"><input type="submit" value="Register" class="btn btn-primary btn-block btn-lg" tabindex="7"></div>
                </div>
            </form>
        </div>
    </div>

<jsp:include page="common/bottom.jsp" />

