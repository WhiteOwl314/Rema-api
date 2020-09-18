package seongju.remaapi.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.service.MemberService;
import seongju.remaapi.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;

@RestController
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("*")
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(
            value = "/checkId.do",
            method = RequestMethod.POST
    )
    public String checkId(
            //필요: id
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
            //필요: email
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
            // 필요: id, pw, level, name, email
            @RequestBody MemberVo memberVo
    ) throws Exception {

        JsonObject bodyMessage =
                memberService.addMember(memberVo);

        String url = "/member/members/" + memberVo.getId();

        return ResponseEntity.created(new URI(url))
                .body(bodyMessage.toString());
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
            //필요: id, pw
            @RequestBody MemberVo memberVo,
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
            //필요: email
            @RequestBody MemberVo memberVo
    ) throws Exception{

        JsonObject bodyMessage =
                memberService.findId(memberVo.getEmail());

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @CrossOrigin("*")
    @RequestMapping(
            value = "/findPw.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> findPw(
            //필요: id,email
            @RequestBody MemberVo memberVo
    ) throws Exception{

        String id = memberVo.getId();
        String email = memberVo.getEmail();
        JsonObject bodyMessage =
                memberService.findPw(id,email);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @RequestMapping(
            value = "/updatePw.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> updatePw(
            //필요: pw,
            @RequestBody MemberVo memberVo,
            HttpServletRequest request
    ) throws Exception{
        String username = getUsername(request);
        memberVo.setId(username);

        JsonObject bodyMessage =
                memberService.updatePw(memberVo);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @RequestMapping(
            value = "/sendEmailForUpdateEmail.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> updateEmail(
            //필요: email
            @RequestBody MemberVo memberVo,
            HttpServletRequest request
    ) throws Exception{
        String username = getUsername(request);
        memberVo.setId(username);

        JsonObject bodyMessage =
                memberService.sendEmailForUpdateEmail(memberVo);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @CrossOrigin("*")
    @RequestMapping(
            value = "/approvalUpdateEmail.do",
            method = RequestMethod.POST
    )
    public void approvalUpdateEmail(
            MemberVo memberVo,
            HttpServletResponse response
    ) throws Exception{
        memberService.approvalUpdateEmail(memberVo, response);
    }


    @RequestMapping(
            value = "/updateName.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> updateName(
            //필요: pw,
            @RequestBody MemberVo memberVo,
            HttpServletRequest request
    ) throws Exception{
        String username = getUsername(request);
        memberVo.setId(username);

        JsonObject bodyMessage =
                memberService.updateName(memberVo);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @CrossOrigin("*")
    @RequestMapping(
            value = "/loginCheck.do",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> loginCheck(
            HttpServletRequest request
    ) throws Exception{
        request.setCharacterEncoding("utf-8");

        JsonObject bodyMessage =
                memberService.loginCheck(request);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }

    @CrossOrigin("*")
    @RequestMapping(
            value = "/getMember",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> getMember(
            HttpServletRequest request
    ) throws UnsupportedEncodingException {

        String username = getUsername(request);

        JsonArray bodyMessage =
                memberService.getMember(username);

        return ResponseEntity.ok().body(bodyMessage.toString());

    }

    public String getUsername(
            HttpServletRequest request
    ) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");

        //request 헤더의 Authorization 가져오기
        final String requestTokenHeader =
                request.getHeader("Authorization");

        String username = null;
        String jwtToken = requestTokenHeader.substring(7);

        //username 가져오기
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);

        return username;
    }
}

