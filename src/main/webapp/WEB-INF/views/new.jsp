<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>BeachBox</title>
    <!-- Bootstrap -->
    <link href="webjars/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Optional theme -->
    <link rel="stylesheet" href="webjars/bootstrap/3.1.1/css/bootstrap-theme.min.css" />
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script type="text/javascript" src="webjars/jquery/2.1.0-2/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script type="text/javascript" src="webjars/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h1>BeachBox</h1>
    <form:form id="formId" commandName="vinyl" method="post"
               class="form-horizontal">
        <div class="input-group">
            <form:label path="rubrik" class="control-label">Rubrik</form:label>
            <div class="controls">
                <form:select path="rubrik">
                    <form:options items="${rubrik}"/>
                </form:select>
            </div>
            <form:errors path="rubrik" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="tontraeger" class="control-label">Tontraeger</form:label>
            <div class="controls">
                <form:select path="tontraeger">
                    <form:options items="${tontraeger}" />
                </form:select>
            </div>
            <form:errors path="tontraeger" class="alert alert-danger"/>
        </div>

        <div class="input-group">
            <form:label path="interpret" class="control-label">Interpret</form:label>
            <div class="controls">
                <form:input path="interpret" class="form-control" />
            </div>
            <form:errors path="interpret" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="song" class="control-label">Song</form:label>
            <div class="controls">
                <form:input path="song" class="form-control" />
            </div>
            <form:errors path="song" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="name" class="control-label">Name</form:label>
            <div class="controls">
                <form:input path="name" class="form-control" />
            </div>
            <form:errors path="name" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="seite" class="control-label">Seite</form:label>
            <div class="controls">
                <form:input path="seite" class="form-control" />
            </div>
            <form:errors path="seite" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="jahr" class="control-label">Jahr</form:label>
            <div class="controls">
                <form:input path="jahr" class="form-control" />
            </div>
            <form:errors path="jahr" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="genre" class="control-label">Genre</form:label>
            <div class="controls">
                <form:input path="genre" class="form-control" />
            </div>
            <form:errors path="genre" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="label" class="control-label">Label</form:label>
            <div class="controls">
                <form:input path="label" class="form-control" />
            </div>
            <form:errors path="label" class="alert alert-danger"/>
        </div>
        <div class="input-group">
            <form:label path="bemerkung" class="control-label">Bemerkung</form:label>
            <div class="controls">
                <form:input path="bemerkung" class="form-control" />
            </div>
            <form:errors path="bemerkung" class="alert alert-danger"/>
        </div>
        <div class="controls">
            <form:hidden path="id"/>
            <input id="createNewVinyl" type="submit" value="Speichern" class="btn btn-primary" />
        </div>
    </form:form>
</div>
</body>
</html>