package backend.service;

import backend.rest.dto.CreateDeviceDTO;
import backend.rest.dto.DeviceControllerServiceDTO;

import java.util.List;

public interface DeviceService {
    List<DeviceControllerServiceDTO> listAllDevicesForUser(String userName);

    DeviceControllerServiceDTO registerNewDevice(String userName, CreateDeviceDTO deviceDTO);

    void deleteDevice(String userName, String deviceName);
}
