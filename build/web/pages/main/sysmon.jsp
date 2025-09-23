<%@ page import="com.crux.web.controller.SessionManager,
                 com.crux.pool.DTOPool,
                 com.crux.common.filter.ProfilingFilter,
                 com.crux.util.thread.ThreadPool,
                 com.crux.common.controller.SessionKeeper,
                 com.crux.util.JSPUtil,
                 java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   int fc = SessionManager.getInstance().getFormCount();

   long dtoCount = DTOPool.getInstance().getDTOCount();

   int sessionCount = SessionKeeper.getInstance().getSessions().size();


   //int sessionCount = 0;

   long cpc = ProfilingFilter.cpc;
%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr><td>Number of Forms</td><td>:</td><td align=right><%=fc%></td></tr>
               <tr><td>Number of DTOs</td><td>:</td><td align=right><%=dtoCount%></td></tr>
               <tr><td>Number of Concurrent Users</td><td>:</td><td align=right><%=sessionCount%></td></tr>
               <tr><td>Number of Processes</td><td>:</td><td align=right><%=cpc%></td></tr>
               <tr><td>Internal Thread Pool Size</td><td>:</td><td align=right><%=ThreadPool.getInstance().getSize()%></td></tr>
               <tr><td>Internal Thread Pool Available</td><td>:</td><td align=right><%=ThreadPool.getInstance().getFree()%></td></tr>
               <tr><td>Available Processors</td><td>:</td><td align=right><%=Runtime.getRuntime().availableProcessors()%></td></tr>
               <tr><td>Memory Free</td><td>:</td><td align=right><%=JSPUtil.print(new BigDecimal(Runtime.getRuntime().freeMemory()),2)%></td></tr>
               <tr><td>Memory Booked</td><td>:</td><td align=right><%=JSPUtil.print(new BigDecimal(Runtime.getRuntime().totalMemory()),2)%></td></tr>
               <tr><td>Memory Max</td><td>:</td><td align=right><%=JSPUtil.print(new BigDecimal(Runtime.getRuntime().maxMemory()),2)%></td></tr>
            </table>
         </td>
      </tr>
   </table>
</c:frame>