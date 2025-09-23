/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.system.notification.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

/**
 *
 * @author doni
 */
public class NotificationSettingView extends DTO implements RecordAudit {

    /*
     *
     * CREATE TABLE s_notification
(
  notif_id bigint NOT NULL,
  user_id character varying(32),
  cc_code character varying(4),
  notif_type character varying(32),
  CONSTRAINT s_notification_pkey PRIMARY KEY (notif_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE s_notification
  OWNER TO postgres;

     */

    public static String tableName = "s_notification";

   public static String fieldMap[][] = {
      {"stNotificationID","notif_id*pk"},
      {"stUserID","user_id"},
      {"stCcCode","cc_code"},
      {"stNotificationType","notif_type"},

   };

   private String stNotificationID;
   private String stUserID;
   private String stCcCode;
   private String stNotificationType;

    public String getStCcCode() {
        return stCcCode;
    }

    public void setStCcCode(String stCcCode) {
        this.stCcCode = stCcCode;
    }

    public String getStNotificationID() {
        return stNotificationID;
    }

    public void setStNotificationID(String stNotificationID) {
        this.stNotificationID = stNotificationID;
    }

    public String getStNotificationType() {
        return stNotificationType;
    }

    public void setStNotificationType(String stNotificationType) {
        this.stNotificationType = stNotificationType;
    }

    public String getStUserID() {
        return stUserID;
    }

    public void setStUserID(String stUserID) {
        this.stUserID = stUserID;
    }

}
