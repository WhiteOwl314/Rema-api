package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.service.MemberService;
import seongju.remaapi.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("*")
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

    @CrossOrigin("*")
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

    @CrossOrigin("*")
    @RequestMapping(
            value = "/approvalMember.do",
            method = RequestMethod.POST
    )
    public void approval_member(
            MemberVo memberVo,
            HttpServletResponse response
    ) throws Exception{
        memberService.approval_member(memberVo, response);
    }

    @CrossOrigin("*")
    @RequestMapping(
            value = "/login.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> login(
            MemberVo memberVo,
            HttpServletRequest request
    ) throws Exception{
        request.setCharacterEncoding("utf-8");

        JsonObject bodyMessage =
                memberService.login(memberVo, request);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @RequestMapping(
            value = "/findId.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> findId(
            @RequestParam("email") String email
    ) throws Exception{
        JsonObject bodyMessage = memberService.findId(email);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @RequestMapping(
            value = "/findPw.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> findPw(
            @RequestParam("id") String id,
            @RequestParam("email") String email
    ) throws Exception{
        JsonObject bodyMessage =
                memberService.findPw(id,email);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

}

