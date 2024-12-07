package models.fileOps;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Config {
    @JsonProperty("system_configs")
    private SystemConfigs systemConfigs;

    @JsonProperty("sales_logs")
    private List<SalesLog> salesLogs;


    public SystemConfigs getSystemConfigs() {
        return systemConfigs;
    }

    public void setSystemConfigs(SystemConfigs systemConfigs) {
        this.systemConfigs = systemConfigs;
    }

    public List<SalesLog> getSalesLogs() {
        return salesLogs;
    }

    public void setSalesLogs(List<SalesLog> salesLogs) {
        this.salesLogs = salesLogs;
    }
}
