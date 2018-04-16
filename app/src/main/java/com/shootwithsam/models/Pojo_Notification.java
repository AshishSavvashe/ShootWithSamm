package com.shootwithsam.models;

import java.io.Serializable;

/**
 * Created by SAI on 8/31/2017.
 */

public class Pojo_Notification implements Serializable {
    private String strNotificationId;
    private String strLeadId;
    private String strFranchiseeId;
    private String strNotification;
    private String strDateTime;
    private String strIsSeen;

    public String getStrNotificationId() {
        return strNotificationId;
    }

    public void setStrNotificationId(String strNotificationId) {
        this.strNotificationId = strNotificationId;
    }

    public String getStrLeadId() {
        return strLeadId;
    }

    public void setStrLeadId(String strLeadId) {
        this.strLeadId = strLeadId;
    }

    public String getStrFranchiseeId() {
        return strFranchiseeId;
    }

    public void setStrFranchiseeId(String strFranchiseeId) {
        this.strFranchiseeId = strFranchiseeId;
    }

    public String getStrNotification() {
        return strNotification;
    }

    public void setStrNotification(String strNotification) {
        this.strNotification = strNotification;
    }

    public String getStrDateTime() {
        return strDateTime;
    }

    public void setStrDateTime(String strDateTime) {
        this.strDateTime = strDateTime;
    }

    public String getStrIsSeen() {
        return strIsSeen;
    }

    public void setStrIsSeen(String strIsSeen) {
        this.strIsSeen = strIsSeen;
    }
}
