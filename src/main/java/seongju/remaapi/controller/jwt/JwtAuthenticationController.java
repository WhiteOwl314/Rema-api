package seongju.remaapi.controller.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.service.jwt.JwtService;
import seongju.remaapi.service.jwt.JwtUserDetailsService;
import seongju.remaapi.vo.JwtRequest;
import seongju.remaapi.vo.JwtResponse;
import seongju.remaapi.vo.MemberVo;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberVo memberVo;

    @Autowired
    private MemberDao memberDao;


    @RequestMapping(
            value = "/authenticate",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> createAuthentiCationToken(
            @RequestBody JwtRequest authenticationRequest
    ) throws Exception{

//        //인증요청
//        JwtResponse response = jwtService.authenticate(
//                authenticationRequest.getUsername(),
//                authenticationRequest.getPassword()
//        );


        authenticate(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        //인증 성공하면

        //User 정보 가져오기
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        JwtResponse response = new JwtResponse();

        memberVo = memberDao.login(authenticationRequest.getUsername());
        if(!memberVo.getApproval_status().equals("1")){
            //이메일 인증이 필요한 경우
            response.setEmailIsAllowed(false);
            response.setLogOn(false);
            return ResponseEntity.ok(response);
        }

        //토큰 생성
        final String token = jwtTokenUtil.generateToken(userDetails);

        response.setEmailIsAllowed(true);
        response.setLogOn(true);
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    private void authenticate(String username, String password)
        throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e){
            throw new Exception("USER_DISABLED",e);
        } catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS",e);
        }
    }



}
