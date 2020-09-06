package seongju.remaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.service.TestService;
import seongju.remaapi.vo.TestVo;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    //tmp
    @Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "reviewmachinemail@gmail.com";
    //tmp

    @Autowired
    private TestService testService;

    @RequestMapping(
            value = "/getHostName",
            method = RequestMethod.GET
    )
    public List<TestVo> getHostName(){
        return this.testService.getHostName();
    }

    //tmp
    @RequestMapping(
            value = "/sendEmail",
            method = RequestMethod.GET
    )
    public void sendEmail (){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("parksj914@naver.com");
        message.setFrom(FROM_ADDRESS);
        message.setSubject("테스트 이메일");
        message.setText("안녕하세요");

        mailSender.send(message);
    }
    //tmp

    @RequestMapping(
            value = "/postTest",
            method = RequestMethod.POST
    )
    public String postTest () {
        return "success";
    }

    @PostMapping(
            value = "/postTestWithBody"
    )
    public String postTestWithBody(
            @RequestBody TestVo testVo
    ){
        return "success";
    }


    @PostMapping(
            value = "/postTestWithForm"
    )
    public String postTestWithForm(
             TestVo testVo
    ){
        return testVo.getTitle();
    }
}
