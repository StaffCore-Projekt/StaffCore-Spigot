package de.lacodev.staffcore.api;

public class StaffCoreAPI {

    public BanOptions getBanOptions() {
        return new BanOptions();
    }

    public MuteOptions getMuteOptions() {
        return new MuteOptions();
    }

    public PlayerUtils getPlayerUtils() {
        return new PlayerUtils();
    }

    public ReportOptions getReportOptions() {
        return new ReportOptions();
    }

    public WarnOptions getWarnOptions() {
        return new WarnOptions();
    }
}
