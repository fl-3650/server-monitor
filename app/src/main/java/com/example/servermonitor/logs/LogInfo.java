package com.example.servermonitor.logs;

public class LogInfo {
    private String timestamp;
    private String level;
    private String message;
    private String server_id;

    public LogInfo() {}

    public LogInfo(String timestamp, String level, String message, String server_id) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.server_id = server_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}
