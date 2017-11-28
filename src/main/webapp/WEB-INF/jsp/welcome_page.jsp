<%@ page import="filehub.demo.CommonModel" %>
<%@ page import="javax.websocket.Session" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <jsp:include page="common/top.jsp"/>
</head>

<body>

<div id="mySidenav" class="sidenav">
    <jsp:include page="common/messaging_sidebar.jsp"/>
</div>
<div id="root_html">
    <jsp:include page="common/navigation.jsp"/>
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
                            <p class="small-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Laudantium
                                enim repellat sapiente quos architecto<br>Laudantium enim repellat sapiente quos
                                architecto</p>
                            <% if (!CommonModel.isLoggedIn(request, session)) { %>
                            <a href="form" class="btn get-quote">Sign up Today</a>
                            <% } %>
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
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Exercitationem nesciunt vitae,<br>
                        maiores, magni dolorum aliquam.</p>
                    <hr class="bottom-line">
                </div>
                <div class="feature-info">
                    <div class="fea">
                        <div class="col-md-4">
                            <div class="heading pull-right">
                                <h4>Latest Technologies</h4>
                                <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas
                                    sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
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
                                <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas
                                    sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
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
                                <p>Donec et lectus bibendum dolor dictum auctor in ac erat. Vestibulum egestas
                                    sollicitudin metus non urna in eros tincidunt convallis id id nisi in interdum.</p>
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

<jsp:include page="common/bottom.jsp"/>
</body>

</html>
