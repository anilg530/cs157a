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
                            <p class="small-text">FileHub aims to provide file sharing services without the headache. <br> Upload files
                                and use a unique url to share with your friends!
                              </p>
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
                    <p> </p>
                    <hr class="bottom-line">
                </div>
                <div class="feature-info">
                    <div class="fea">
                        <div class="col-md-4">
                            <div class="heading pull-right">
                                <h4> What's special about FileHub?</h4>
                                <p>FileHub can be hosted on your private server without the need of extra deployment. Save your
                                documents on a private storage and forget about Dropbox, or Google Drives.</p>
                            </div>
                            <div class="fea-img pull-left">
                                <i class="fa fa-css3"></i>
                            </div>
                        </div>
                    </div>
                    <div class="fea">
                        <div class="col-md-4">
                            <div class="heading pull-right">
                                <h4>Easy To Use</h4>
                                <p>FileHub's main goal is to enhance user usability. Uploading documents is simple as dragging
                                and dropping. </p>
                            </div>
                            <div class="fea-img pull-left">
                                <i class="fa fa-drupal"></i>
                            </div>
                        </div>
                    </div>
                    <div class="fea">
                        <div class="col-md-4">
                            <div class="heading pull-right">
                                <h4>Group Management</h4>
                                <p>Have more people to share documents with? FileHub creates a unique url of your document. You
                                have the freedom to allow access to who can access the document.</p>
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
