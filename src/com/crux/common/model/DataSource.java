/***********************************************************************
 * Module:  com.crux.common.model.DataSource
 * Author:  Denny Mahendra
 * Created: Jun 20, 2005 10:48:32 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import com.crux.common.controller.ReportHandler;
import net.sf.jasperreports.engine.JRDataSource;

public abstract class DataSource extends ReportHandler implements JRDataSource {
}
