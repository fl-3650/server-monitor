package com.example.servermonitor.monitoring;

import java.util.List;

public class ServerInfo {
    private double total_disk_space;
    private double occupied_disk_space;
    private double free_disk_space_percentage;
    private double cpu_load;
    private double ram_usage;
    private double uptime;
    private List<String> logged_in_users;

    public ServerInfo() {
    }

    public ServerInfo(double total_disk_space, double occupied_disk_space,
                      double free_disk_space_percentage, double cpu_load,
                      double ram_usage, double uptime, List<String> logged_in_users) {

        this.total_disk_space = total_disk_space;
        this.occupied_disk_space = occupied_disk_space;
        this.free_disk_space_percentage = free_disk_space_percentage;
        this.cpu_load = cpu_load;
        this.ram_usage = ram_usage;
        this.uptime = uptime;
        this.logged_in_users = logged_in_users;
    }

    public double getTotal_disk_space() {
        return total_disk_space;
    }

    public void setTotal_disk_space(double total_disk_space) {
        this.total_disk_space = total_disk_space;
    }

    public double getOccupied_disk_space() {
        return occupied_disk_space;
    }

    public void setOccupied_disk_space(double occupied_disk_space) {
        this.occupied_disk_space = occupied_disk_space;
    }

    public double getFree_disk_space_percentage() {
        return free_disk_space_percentage;
    }

    public void setFree_disk_space_percentage(double free_disk_space_percentage) {
        this.free_disk_space_percentage = free_disk_space_percentage;
    }

    public double getCpu_load() {
        return cpu_load;
    }

    public void setCpu_load(double cpu_load) {
        this.cpu_load = cpu_load;
    }

    public double getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(double ram_usage) {
        this.ram_usage = ram_usage;
    }

    public double getUptime() {
        return uptime;
    }

    public void setUptime(double uptime) {
        this.uptime = uptime;
    }

    public List<String> getLogged_in_users() {
        return logged_in_users;
    }

    public void setLogged_in_users(List<String> logged_in_users) {
        this.logged_in_users = logged_in_users;
    }
}
