package models.fileOps;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SalesLog {

    @JsonProperty("log")
    private String log;

    @JsonProperty("date_time")
    private String dateTime;


    // Default constructor (required by Jackson)
    public SalesLog() {
    }

    // Parameterized constructor (for easier object creation in code)
    public SalesLog(String log, String dateTime) {
        this.log = log;
        this.dateTime = dateTime;
    }

    // Getters and setters
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
