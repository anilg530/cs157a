<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>


<div class="vertical-center-70">
    <div class="vertical-center-container">
        <div class="row">
            <div class="col-xs-offset-1 col-xs-10 col-sm-offset-4 col-sm-4 col-md-offset-4 col-md-4 col-lg-offset-4 col-lg-3">
                <p>Enter the following information to join your group:</p>
                <hr>
                <form id="join_group_form" action="" method="post" accept-charset="utf-8">
                    <div class="form-group">
                        <label class="control-label">Group Name</label>
                        <input type="text" class="form-control" name="group_name" value="" placeholder="group name"
                               autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false"
                               autofocus="true" required="true"/>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Group Password</label>
                        <input type="text" class="form-control" name="group_password" value="" placeholder="group password"
                               autocomplete="off" autocapitalize="none" autocorrect="off" spellcheck="false"
                               required="true"/>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-success btn-block" onclick="submit_join_group()">Join Group</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
