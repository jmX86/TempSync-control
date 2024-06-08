package backend.service.impl;

import backend.dao.DeviceRepository;
import backend.dao.TopicRepository;
import backend.dao.UserRepository;
import backend.domain.Device;
import backend.domain.Topic;
import backend.domain.User;
import backend.rest.dto.CreateDeviceDTO;
import backend.rest.dto.DeviceControllerServiceDTO;
import backend.rest.dto.UserControllerServiceDTO;
import backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceJpa implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public List<DeviceControllerServiceDTO> listAllDevicesForUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return user.getDevices().stream().map(DeviceControllerServiceDTO::new).toList();
    }

    @Override
    public DeviceControllerServiceDTO registerNewDevice(String userName, CreateDeviceDTO deviceDTO) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Assert.hasText(deviceDTO.getDeviceName(), "Device name must be given.");
        Assert.isTrue(deviceDTO.getDeviceName().length() <= 64, "Device name must be shorter than 64 characters.");

        Device newDevice = new Device(deviceDTO.getDeviceName(), user);
        
        return new DeviceControllerServiceDTO(deviceRepository.save(newDevice));
    }

    @Override
    public void deleteDevice(String userName, String deviceName) {
        User user = userRepository.findByUserName(userName).orElseThrow(()-> new RuntimeException("No user with that username"));
        Device device = deviceRepository.findDeviceByNameAndOwner(deviceName, user).orElseThrow(()-> new RuntimeException("No device with that name"));
        topicRepository.deleteAll(device.getDeviceTopics());
        deviceRepository.deleteById(device.getDeviceId());
    }


}
