package ZIoVPO.ZIoVPO_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/Hello/{name}")
    public String HelloUser(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
