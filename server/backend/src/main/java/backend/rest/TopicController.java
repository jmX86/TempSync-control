package backend.rest;

import backend.rest.dto.CreateDeviceDTO;
import backend.rest.dto.CreateTopicDTO;
import backend.rest.dto.ReadingControllerServiceDTO;
import backend.rest.dto.TopicControllerServiceDTO;
import backend.service.ReadingService;
import backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Secured("BASIC_USER")
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping("")
    public TopicControllerServiceDTO registerNewTopic(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateTopicDTO topicDTO){
        return topicService.registerNewTopic(userDetails.getUsername(), topicDTO);
    }



}
