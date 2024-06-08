package backend.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@PreAuthorize("hasAuthority('BASIC_USER') or hasAuthority('ADMIN')")
public class LoginController {
    @GetMapping("")
    public void tryLogin(){
    }
}
