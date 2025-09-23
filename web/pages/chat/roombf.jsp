<html>
<head>
<%= charset %>
<title>JavaZOOM Chat</title>
</head>
<frameset rows="68,0,*" frameborder="NO" border="0" framespacing="0">
  <frame name="text" scrolling="NO" noresize src="<%= action %>?todo=text" marginwidth="0" marginheight="0" frameborder="NO" >
  <frame name="room" src="<%= action %>?todo=refresh" scrolling="NO" marginwidth="0" marginheight="0" frameborder="NO" noresize>
  <frame name="dbroom" src="<%= action %>?todo=dummy" scrolling="AUTO" marginwidth="0" marginheight="0" frameborder="NO" noresize>
</frameset>
<noframes>
<body bgcolor="#FFFFFF">
Frames support needed to run jzChat V1.12
</body>
</noframes>
</html>
