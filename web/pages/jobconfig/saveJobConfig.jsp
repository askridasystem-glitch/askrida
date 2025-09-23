<%@ page import="com.crux.util.JSPUtil,
                 com.crux.jobs.validator.JobValidator,
                 com.crux.util.LookUpUtil,
                 com.crux.jobs.model.JobConfigView,
                 java.text.SimpleDateFormat,
                 com.crux.jobs.ejb.JobConfig,
                 com.crux.jobs.helper.JobConfigHelper,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.validation.Validator,
                 com.crux.common.config.Config"%>
 <%
	 final JSPUtil jspUtil = new JSPUtil(request, response);
	 final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	 LookUpUtil jobsLU =
          Config.isHO() ?
          JobConfigView.Jobs.getHOJobs():
          JobConfigView.Jobs.getBranchJobs();

	 JobConfigView view = (JobConfigView)request.getAttribute("JOB_CONFIG");
	 String strAction = (String)request.getAttribute("ACTION");

	 boolean isAdd = strAction == "CREATE";
     String title = isAdd ? "ADD JOB" : "EDIT JOB";

	 String sStartTime = "";
	 if(view.getDtStart() != null) {
		 sStartTime = df.format(view.getDtStart());
	 }
	 String sEndTime = "";
	 if(view.getDtEnd() != null) {
		 sStartTime = df.format(view.getDtEnd());
	 }

 %>
<html>
   <head>
	<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
    <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
         <table cellpadding=2 cellspacing=1 width="100%">
            <tr>
               <td>
               <table cellpadding=1 cellspacing=1>
      <form name=f method=POST action="ctl.ctl">
	  <input type=hidden name=EVENT value="JOB_SAVE">
	  <input type=hidden name=ACTION value="<%=strAction%>">

				<%=jspUtil.getHeader(title)%>
				<tr>
					<td><br></td>
					<td></td>
				</tr>
				<tr>
					<td style="width:120px">Job ID :</td>
					<td style="width:450px">
					<%  if(isAdd) { %>
						<%=jspUtil.getInputText("jobId", JobValidator.vfJobID, view.getStJobID(), 100, JSPUtil.MANDATORY)%>
					<% } else { %>
						<input name=jobId readOnly style="width:130px" value="<%= view.getStJobID() %>">

					<% } %>
					</td>
				</tr>
				<tr>
					<td>Job Event :</td>
					<td>
                    <%= jspUtil.getInputSelect("jobEvent", JobValidator.vfJobEvent, jobsLU.getComboContent(view.getStJobEvent()), 400) %>
                    <script>
                        document.f.jobEvent.onchange = function() {
                           document.f.EVENT.value='JOB_CHANGE_EVENT';
                           document.f.submit();
                        }
                    </script>
					</td>
				</tr>
            <tr>
					<td>Status:</td>
					<td>
                    <%= jspUtil.getInputSelect("status", null, JobConfigView.Enabled.getLookUp().setNoNull().getComboContent(view.getStEnabled()), 100) %>
					</td>
				</tr>
<%
   for (int i=1;i<=5;i++) {
      final FieldValidator fv = (FieldValidator)view.getAttribute("param"+i+"Validator");
      final LookUpUtil lov = (LookUpUtil)view.getAttribute("param"+i+"LOV");
      if (fv!=null) {
         final boolean mandatory = (fv.getFlags() & Validator.MANDATORY) != 0;
         String p=null;
         switch (i) {
            case 1:p = view.getStParam1();break;
            case 2:p = view.getStParam2();break;
            case 3:p = view.getStParam3();break;
            case 4:p = view.getStParam4();break;
            case 5:p = view.getStParam5();break;
         }
%>
            <tr>
					<td><%=fv.getStFieldName()%> :</td>
					<td>
<% if (lov!=null) {%>
                    <%= jspUtil.getInputSelect("param"+i, fv,lov.getComboContent(p) , 200, mandatory?JSPUtil.MANDATORY:0) %>
<% } else {%>
                    <%= jspUtil.getInputText("param"+i, fv,p , 200, mandatory?JSPUtil.MANDATORY:0) %>
<% } %>
					</td>
				</tr>
<% } } %>
				<tr>
					<td>Trigger mode:</td>
					<td>
                    <%= jspUtil.getInputSelect("triggerMode", JobValidator.vfTriggerMode, JobConfigView.TriggerMode.getLookUp().setNoNull().getComboContent(view.getStTriggerMode()), 150, JSPUtil.MANDATORY) %>
					</td>
				</tr>
				<tr>
					<td>Start Date:</td>
					<td><table cellpadding=2 cellspacing=1>
					   <tr>
					      <td><%=jspUtil.getInputText("startdate", JobValidator.vfStartDate, view.getDtStart(), 100)%></td>
					      <td>Time:
                    <%=jspUtil.getInputText("starttime", JobValidator.vfStartTime, sStartTime, 50)%></td>
					   </tr>
					</table>
					</td>
				</tr>
				<tr>
					<td>End Date:</td>
					<td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td><%=jspUtil.getInputText("enddate", JobValidator.vfEndDate, view.getDtEnd(), 100)%>
                        </td>
                        <td>Time:
                    <%=jspUtil.getInputText("endtime", JobValidator.vfEndTime, sEndTime, 50)%>
                        </td>
                     </tr>
                  </table>
					</td>
				</tr>
				<tr>
					<td>Repeat Count:</td>
					<td><%=jspUtil.getInputText("repetition", JobValidator.vfRepetition, view.getLgRepeatNum(), 50)%>
					</td>
				</tr>
				<tr>
					<td>Repeat Interval:</td>
					<td><%=jspUtil.getInputText("interval", JobValidator.vfInterval, view.getLgInterval(), 50)%> (in minutes)
					</td>
				</tr>
				<tr>
					<td>Cron Expression:</td>
					<td><%=jspUtil.getInputText("cronExpression", JobValidator.vfCronExpression, view.getStCRONExpression(), 200, JSPUtil.MANDATORY)%>
					&nbsp;&nbsp;&nbsp;<input type=button value="Help" onclick="displayCronHelp()">
					</td>
				</tr>
				<tr>
					<td>Retry when fail:</td>
					<td>
                    <%= jspUtil.getInputText("retry", JobValidator.vfRetry, view.getItRetry(), 50) %>
					</td>
				</tr>
				<tr>
					<td>Retry interval:</td>
					<td>
                    <%= jspUtil.getInputText("retry_interval", JobValidator.vfRetryInterval, view.getItRetryInterval(), 50) %>
                    &nbsp;minutes
					</td>
				</tr>
				<tr>
					<td>Started by Job :</td>
					<td>
                    <%= jspUtil.getInputSelect("job_starter", JobValidator.vfJobStarterId, JobConfigHelper.availableJobLookUp().getComboContent(view.getStStarterJobID()), 200) %>
					</td>
				</tr>
				<tr>
					<td>Interval before Started :</td>
					<td>
                    <%= jspUtil.getInputText("job_starter_interval", JobValidator.vfJobStarterInterval, view.getItStarterJobInterval(), 50) %>
                    &nbsp;minutes
					</td>
				</tr>
				<tr>
					<td colspan=2>
					<br><br>
						<%=jspUtil.getButtonSubmit("bsubmit"," Submit ")%>
						<%=jspUtil.getButtonNormal("bcancel"," Cancel ","window.location='"+jspUtil.getControllerURL("CONFIG_JOB")+"'")%>
					</td>
				</tr>
               </form>
				</table>
               </td>
            </tr>

         </table>

   </body>
</html>

<script language="JavaScript">
	onchangeTriggerMode();
    f.triggerMode.onchange = onchangeTriggerMode;

    function onchangeTriggerMode() {
    	if(f.triggerMode.value == 'SIMPL') {
	        f.cronExpression.disabled = true;
	        f.repetition.disabled = false;
	        f.interval.disabled = false;
	    } else {
	        f.cronExpression.disabled = false;
	        f.repetition.disabled = true;
	        f.interval.disabled = true;
	    }
    }

    function displayCronHelp() {
        window.open('<%= jspUtil.getPagesPath()%>' + '/jobconfig/help.jsp', 'Help','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=640,height=480');
    }


</script>

