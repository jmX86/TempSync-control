package backend.rest;

import backend.rest.dto.CreateDeviceDTO;
import backend.rest.dto.DeviceControllerServiceDTO;
import backend.rest.dto.ReadingControllerServiceDTO;
import backend.service.DeviceService;
import backend.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured("BASIC_USER")
@RequestMapping("/device")
public class DeviceController {
    @PostMapping("")
    public DeviceControllerServiceDTO registerNewDevice(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody CreateDeviceDTO deviceDTO){
        return deviceService.registerNewDevice(userDetails.getUsername(), deviceDTO);
    }

    @GetMapping("/{deviceName}/{topicName}/latest")
    public ReadingControllerServiceDTO getLatestReadingOfTopic(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("topicName") String topicName,
            @PathVariable("deviceName") String deviceName)
    {
        return readingService.getLatestReadingForTopic(userDetails.getUsername(), topicName, deviceName);
    }

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ReadingService readingService;

    @GetMapping("")
    public List<DeviceControllerServiceDTO> listAllDevices(@AuthenticationPrincipal UserDetails userDetails){
        return deviceService.listAllDevicesForUser(userDetails.getUsername());
    }



    @DeleteMapping("/{deviceName}")
    public void deleteDevice(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("deviceName") String deviceName){
        deviceService.deleteDevice(userDetails.getUsername(), deviceName);
    }
}
