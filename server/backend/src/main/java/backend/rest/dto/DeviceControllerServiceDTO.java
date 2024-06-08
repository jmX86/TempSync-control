package backend.rest.dto;

import backend.domain.Device;

import java.util.List;

public class DeviceControllerServiceDTO {
    private String deviceName;
    private List<TopicControllerServiceDTO> topics;

    public DeviceControllerServiceDTO(Device device){
        this.deviceName = device.getDeviceName();
        if(device.getDeviceTopics() == null){
            this.topics = List.of();
        }else {
            this.topics = device.getDeviceTopics().stream().map(TopicControllerServiceDTO::new).toList();
        }
    }

    public String getDeviceName() {
        return deviceName;
    }

    public List<TopicControllerServiceDTO> getTopics() {
        return topics;
    }
}
