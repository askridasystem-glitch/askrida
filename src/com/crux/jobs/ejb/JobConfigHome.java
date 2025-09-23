/*
 * $Header: /cvs/webfin/ejbx/src/com/crux/jobs/ejb/JobConfigHome.java,v 1.1.1.1 2005/12/26 03:07:30 cvsdenny Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2005/12/26 03:07:30 $
 *
 * JobConfigHome.java
 *
 * Copyright &#169; 2004 PineappleTech
 * All rights reserved.
 */
package com.crux.jobs.ejb;

import javax.ejb.EJBHome;

/**
 * <explain class description>
 * <p/>
 * Created: Jun 16, 2004 - 3:04:26 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public interface JobConfigHome extends EJBHome {
    JobConfig create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
