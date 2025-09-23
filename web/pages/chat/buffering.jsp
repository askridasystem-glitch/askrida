<html>
<meta http-equiv=Refresh content="<%= autorefresh %>;URL=<%= action %>?todo=refresh">
<head>
<%= charset %>
<title>Buffered ChatRoom</title>
<script language="JavaScript"><!--
var thechat = '<%= chat %>';
function execute()
{
	if (typeof(self.parent.dbroom) != "undefined")
	{
		self.parent.dbroom.document.open("text/html");
		self.parent.dbroom.document.writeln(unescape(thechat));
		self.parent.dbroom.document.close();
	}
}
//--></script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="execute()">
</body>
</html>
