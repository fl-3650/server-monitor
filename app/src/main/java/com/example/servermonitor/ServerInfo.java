package com.example.servermonitor;

import java.util.List;
import java.util.Objects;

public class ServerInfo {
    private final double total_disk_space;
    private final double occupied_disk_space;
    private final double free_disk_space_percentage;
    private final double cpu_load;
    private final double ram_usage;
    private final double uptime;
    private final List<String> logged_in_users;

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

    public double getOccupied_disk_space() {
        return occupied_disk_space;
    }

    public double getFree_disk_space_percentage() {
        return free_disk_space_percentage;
    }

    public double getCpu_load() {
        return cpu_load;
    }

    public double getRam_usage() {
        return ram_usage;
    }

    public double getUptime() {
        return uptime;
    }

    public List<String> getLogged_in_users() {
        return logged_in_users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return Double.compare(total_disk_space, that.total_disk_space) == 0
                && Double.compare(occupied_disk_space, that.occupied_disk_space) == 0
                && Double.compare(free_disk_space_percentage, that.free_disk_space_percentage) == 0
                && Double.compare(cpu_load, that.cpu_load) == 0
                && Double.compare(ram_usage, that.ram_usage) == 0
                && Double.compare(uptime, that.uptime) == 0
                && Objects.equals(logged_in_users, that.logged_in_users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total_disk_space, occupied_disk_space, free_disk_space_percentage,
                cpu_load, ram_usage, uptime, logged_in_users);
    }
}
