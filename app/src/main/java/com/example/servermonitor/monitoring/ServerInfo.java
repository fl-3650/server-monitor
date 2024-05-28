package com.example.servermonitor.monitoring;

import java.util.List;

public class ServerInfo {
    private double totalDiskSpace;
    private double occupiedDiskSpace;
    private double freeDiskSpacePercentage;
    private double cpuLoad;
    private double ramUsage;
    private double uptime;
    private List<String> loggedInUsers;

    public ServerInfo() {
    }

    public ServerInfo(double totalDiskSpace, double occupiedDiskSpace,
                      double freeDiskSpacePercentage, double cpuLoad,
                      double ramUsage, double uptime, List<String> loggedInUsers) {

        this.totalDiskSpace = totalDiskSpace;
        this.occupiedDiskSpace = occupiedDiskSpace;
        this.freeDiskSpacePercentage = freeDiskSpacePercentage;
        this.cpuLoad = cpuLoad;
        this.ramUsage = ramUsage;
        this.uptime = uptime;
        this.loggedInUsers = loggedInUsers;
    }

    public double getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public void setTotalDiskSpace(double totalDiskSpace) {
        this.totalDiskSpace = totalDiskSpace;
    }

    public double getOccupiedDiskSpace() {
        return occupiedDiskSpace;
    }

    public void setOccupiedDiskSpace(double occupiedDiskSpace) {
        this.occupiedDiskSpace = occupiedDiskSpace;
    }

    public double getFreeDiskSpacePercentage() {
        return freeDiskSpacePercentage;
    }

    public void setFreeDiskSpacePercentage(double freeDiskSpacePercentage) {
        this.freeDiskSpacePercentage = freeDiskSpacePercentage;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public double getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(double ramUsage) {
        this.ramUsage = ramUsage;
    }

    public double getUptime() {
        return uptime;
    }

    public void setUptime(double uptime) {
        this.uptime = uptime;
    }

    public List<String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(List<String> loggedInUsers) {
        this.loggedInUsers = loggedInUsers;
    }
}
