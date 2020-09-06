package seongju.remaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seongju.remaapi.service.TestService;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(
            value = "/getHostName",
            method = RequestMethod.GET
    )
    private String getHostName(){
        return this.testService.getHostName();
    }
}
