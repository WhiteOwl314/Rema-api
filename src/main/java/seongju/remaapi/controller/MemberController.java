package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seongju.remaapi.service.MemberService;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping(
            value = "/checkId.do",
            method = RequestMethod.POST
    )
    public String checkId(
            @RequestParam("id") String id
    ){
        return memberService.checkId(id);
    }
}
