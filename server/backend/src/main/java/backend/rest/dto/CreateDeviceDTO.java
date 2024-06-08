package backend.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateDeviceDTO {
    private String deviceName;

    @JsonCreator
    public CreateDeviceDTO(@JsonProperty("deviceName") String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
