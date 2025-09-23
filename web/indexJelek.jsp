
 
 <%@ page import="com.crux.util.JSPUtil,
                 com.crux.lang.LanguageManager,
                 com.crux.util.DTOList,
                 com.crux.lang.LanguageView,
                 com.crux.util.Tools,
                 com.crux.common.parameter.Parameter"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response, false); %>
<%
   final boolean firstTime = !"Y".equalsIgnoreCase((String) request.getSession().getAttribute("FIRST_TIME"));

   final DTOList languages = LanguageManager.getInstance().getLanguages();

    final boolean showLogo = Parameter.readBoolean("GEN_SHOW_LOGO");
    
    final boolean blockUser = Parameter.readBoolean("BLOCK_USER");
	
	final Throwable e = (Throwable) request.getAttribute("ERROR_MESSAGE");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"  lang="en"> 
<head> 
    <title></title> 
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" /> 
	<meta http-equiv="Cache-Control" content="no-cache" /> 
    <link rel="stylesheet" href="css/global.css" type="text/css" media="screen" /> 
    <link rel="stylesheet" href="css/master.css" type="text/css" media="screen" /> 
	<!-- no java_main.js so we don't check for idle state -->	
	
</head> 
<%
	if (e != null) {
%>
            <script>alert('Error:<%=e%>');</script>
       <% } %>
	   
<body class="login"> 
    <!--[if lt IE 7]>
    <link rel="stylesheet" href="/CatalystStyles/ie6.css" type="text/css" media="screen" />    
    <![endif]-->	
    <form name=loginform method=POST action="login.ctl"> 
 	<input type=hidden name=EVENT value=LOGIN_MAIN>
 
</div> 
 
    <div id="login"> 
		 
	    <div id="cap-top"></div> 
	    <div id="cap-body"> 
	    <div id="branding"><img id="imgLogo" src="images/businesscatalyst.png" style="border-width:0px;height:60px;width:400px;" /></div>		
            <div id="panelLogin"> 
	
			    <div> 
				    <label> 
                        User ID</label> 
                        <input type="text" class="textbox340" name="userid" id="userid" value="" /> 
			    </div> 
			    <div> 
				    <label> 
                        Password</label> 
                        <input type="password" class="textbox340" name="password" id="password" value="" /> 
			    </div> 
                					
		      <div class="submit clearfix"> 
			        <input type="image" src="images/button-login.png" alt="Submit" name="submit" id="submit" />
		      </div> 
		    
</div>	
            			
            						
		    			
		    
	    </div> 
	    <div id="cap-bottom"><img src="images/cap-bottom.png" alt="" /></div> 
		<div id="cap-lang"><span class="submit clearfix">
		<%

      final String activeLang = LanguageManager.getInstance().getActiveLang();

      for (int i = 0; i < languages.size(); i++) {
         LanguageView lv = (LanguageView) languages.get(i);
         final String imgname = lv.getStLanguageID().toLowerCase();

         final boolean active = Tools.isEqual(lv.getStLanguageID(), activeLang);

         final String bor = active?"border:solid 2px blue":"border:solid 1px black";

         %><img style="<%=bor%>; cursor:hand" alt="<%=lv.getStLanguageName()%>" src="images/flag_<%=imgname%>.gif" onclick="document.location='<%=request.getContextPath()%>/?clangx=<%=lv.getStLanguageID()%>'">&nbsp;&nbsp;<%
      }
%>
		</span></div>
    </div><!-- END #login --> 

    </form> 
 
    <script language="javascript" type="text/javascript"> 
            if (document.getElementById('userid'))
			    document.getElementById('userid').focus();
    </script> 
 
</body> 
</html> 