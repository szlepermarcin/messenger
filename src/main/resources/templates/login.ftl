<html>
<head>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" 
	crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<#if RequestParameters['error']??>
			<div class="alert alert-danger">
				There was a problem logging in. Please try again.
			</div>
		</#if>
		<div class="container">
			<form role="form" action="login" method="post">
		  		<div class="form-group">
		    		<label for="username">Username:</label>
		    		<input type="text" class="form-control" id="username" name="username"/>
		  		</div>
		 		<div class="form-group">
		    		<label for="password">Password:</label>
		    		<input type="password" class="form-control" id="password" name="password"/>
		  		</div>
		  		<input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		  		<button type="submit" class="btn btn-primary">Submit</button>
			</form>
		</div>
	</div>
	<script  src="https://code.jquery.com/jquery-3.1.1.min.js"
  		integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
  		crossorigin="anonymous"></script>
</body>
</html>