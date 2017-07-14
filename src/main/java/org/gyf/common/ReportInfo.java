package org.gyf.common;

import java.sql.Date;

/**
 * Report类
 * reportContent:报告内容
 * reporterID:发出报告的用户id
 * reportTime:报告发出的时间
 * reportID:报告id
 */
public class ReportInfo extends Info{

    private String reportContent;
    private String reporterID;
    private Date reportTime;
    private int reportID;


    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReporterID() {
        return reporterID;
    }

    public void setReporterID(String reporterID) {
        this.reporterID = reporterID;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }
}
