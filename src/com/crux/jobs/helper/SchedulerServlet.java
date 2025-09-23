package com.crux.jobs.helper;

import com.crux.jobs.util.JobUtil;
import com.crux.util.LogManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <explain class description>
 * <p/>
 * Created: Jun 17, 2004 - 9:23:01 AM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public class SchedulerServlet extends HttpServlet {
    private final static transient LogManager logger = LogManager.getInstance(SchedulerServlet.class);

    private static SchedulerServlet thisInstance = null;

    public static SchedulerServlet getInstance() {
        return thisInstance;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.logDebug("+++++++++++++++ SCHEDULER ++++++++++++++");
        logger.logDebug("Quartz Initializer Servlet loaded, initializing Scheduler...");
        thisInstance = this;
        try {
            JobUtil.getScheduler().start();
            JobUtil.executeAllSchedules();
        } catch (Exception e) {
            logger.logError("Quartz Scheduler failed to initialize: " + e.toString());
            throw new ServletException(e);
        }
    }

    public void destroy() {
        try {
            if (JobUtil.getScheduler() != null) {
                JobUtil.getScheduler().shutdown();
            }
        } catch (Exception e) {
            logger.logError("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
        }
        log("Quartz Scheduler successful shutdown.");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }


}

