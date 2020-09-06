package seongju.remaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seongju.remaapi.service.TestService;
import seongju.remaapi.vo.TestVo;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(
            value = "/getHostName",
            method = RequestMethod.GET
    )
    private List<TestVo> getHostName(){
        return this.testService.getHostName();
    }
}
