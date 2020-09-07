package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.service.MemberService;
import seongju.remaapi.vo.MemberVo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

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


    @RequestMapping(
            value = "/checkEmail.do",
            method = RequestMethod.POST
    )
    public String checkEmail(
            @RequestBody HashMap<String, Object> map
    ){
        String email = (String) map.get("email");
        return memberService.checkEmail(email);
    }

    @RequestMapping(
            value = "/addMember.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> addMember(
            MemberVo memberVo
    ) throws Exception {
        memberService.addMember(memberVo);

        String url = "/member/members/" + memberVo.getId();

        return ResponseEntity.created(new URI(url)).body("{}");
    }
}
