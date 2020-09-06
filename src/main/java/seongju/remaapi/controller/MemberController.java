package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.service.MemberService;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @CrossOrigin("*")
    @RequestMapping(
            value = "/checkId.do",
            method = RequestMethod.POST
    )
    public String checkId(
            @RequestBody HashMap<String, Object> map
    ){
        String id = (String) map.get("id");
        return memberService.checkId(id);
    }
}
