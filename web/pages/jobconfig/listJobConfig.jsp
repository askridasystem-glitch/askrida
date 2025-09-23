<%@ page import="com.crux.util.JSPUtil,
                 com.crux.jobs.model.JobConfigView,
                 com.crux.util.LookUpUtil,
                 com.crux.util.DTOList,
                 com.crux.util.DateUtil,
                 org.quartz.JobDataMap,
                 org.quartz.Scheduler,
                 com.crux.jobs.util.JobUtil,
                 org.quartz.JobDetail,
                 com.crux.jobs.constant.JobConstant,
                 java.util.List,
                 org.quartz.JobExecutionContext"%>
 <% final JSPUtil jspUtil = new JSPUtil(request, response);

    DTOList dtoList = (DTOList) request.getAttribute("JOB_LIST");
	if(dtoList == null) {
		dtoList = new DTOList();
	}

 %>
<html>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
    <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <table cellpadding=2 cellspacing=1 width="100%">
            <%=jspUtil.getHeader("JOB CONFIGURATION")%>
            <tr><td><br></td></tr>
            <tr>
               <td>
               <table>
				<tr>
                    <td class=header >Job ID</td>
                    <td class=header >Job Event</td>
                    <td class=header >Scheduling Mode</td>
                    <td class=header >Enabled</td>
                    <td class=header >Status</td>
                    <td class=header >Last Status</td>
				</tr>
	<%
            //LookUpUtil jobsLU = JobConfigView.Jobs.getLookUp();
		int size = dtoList.size();
		if(size == 0) {
	%>
	  	        <tr colspan=6><td>No jobs.</td></tr>

	<%
		} else {
            for (int i = 0; i < size; i++) {
	            JobConfigView view = (JobConfigView) dtoList.get(i);
	            String jobId = view.getStJobID();
	            String jobEvent = view.getStJobEvent();

	%>
				<tr class=row<%=i%2%>>
                    <td>
                    <input type="radio" name="jobRadio" onclick="selectJob('<%= jobId %>', '<%= view.getStEnabled() %>')"><%= jobId %>
                    </td>
                    <td><%= jspUtil.print(JobConfigView.Jobs.getLookUp().getValue(jobEvent))%></td>
                    <td><%= jspUtil.print(view.getStSchedulingMode()) %></td>
                    <td align=center><%= jspUtil.print(view.getStEnabled()) %></td>
                    <td>
                    <%
	                    List list = JobUtil.getScheduler().getCurrentlyExecutingJobs();
	                    boolean isRunning = false;
	                    for(int l = 0; l < list.size(); l++) {
		                    JobExecutionContext  jobCtx = (JobExecutionContext) list.get(l);
		                    String runningJob = jobCtx.getJobDetail().getName();
		                    if(runningJob.equals(jobId) ||
		                       runningJob.equals(jobId + "invoke") ||
		                       runningJob.indexOf(jobId + "retry") > 0) {
			                    isRunning = true;
		                    }
	                    }
		                    if(isRunning)
			                    out.println("Running");
		                    else
			                    out.println("Off");
                     %>
                    </td>
                    <td>
                       <%= jspUtil.print(view.getStJobLastStatus()) %>
                    </td>
				</tr>

	<%      }
		}
	%>
               </table>
               </td>
            </tr>
             <tr><td><br></td></tr>
<tr>
	<td>
      <input type=hidden name=EVENT>
      <input type=hidden name=jobId>
      <input type=submit name=badd value="Add Job" onClick="f.EVENT.value='JOB_ADD'">
      <input type=submit name=bedit disabled value="Edit Job" onClick="f.EVENT.value='JOB_EDIT'">
      <input type=submit name=bdelete disabled value="Delete Job" onClick="deleteJob()">
      <input type=submit name=binvoke disabled value="Invoke Job" onClick="invokeJob()">
      <input type=submit name=binvoke2 disabled value="Execute Job" onClick="execJob()">
	</td>
</tr>
         </table>
      </form>
   </body>
</html>

<script>
function selectJob(jobId, enabled) {
      f.bedit.disabled = false;
      f.bdelete.disabled = false;
      f.binvoke.disabled = false;
      f.binvoke2.disabled = false;
/*
      if(enabled == 'Y') {
        f.binvoke.disabled = false;
      } else {
        f.binvoke.disabled = true;
      }
*/
      f.jobId.value = jobId;
}

function deleteJob(){
    if(confirm("Are you sure want to delete this job?")){
        f.EVENT.value ="JOB_DELETE";
    }
}

function invokeJob(){
    if(confirm("Are you sure want to invoke this job?")){
        f.EVENT.value ="JOB_TRIGGER";
    }
}
    
function execJob(){
    if(confirm("Are you sure want to execute this job?")){
        f.EVENT.value ="JOB_INVOKE";
    }  
}

</script>