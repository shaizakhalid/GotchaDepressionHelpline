package com.example.gotcha_depressionhelpline;

public class Report {

    String patientName, sessionDate, reportStatement,therapistName;



    public Report(String patientName, String sessionDate, String reportStatement) {
        this.patientName = patientName;
        this.sessionDate = sessionDate;
        this.reportStatement = reportStatement;
    }

    public Report(String patientName, String therapistName, String sessionDate, String reportStatement) {
        this.therapistName= therapistName;
        this.patientName = patientName;
        this.sessionDate = sessionDate;
        this.reportStatement = reportStatement;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getReportStatement() {
        return reportStatement;
    }

    public void setReportStatement(String reportStatement) {
        this.reportStatement = reportStatement;
    }
}
