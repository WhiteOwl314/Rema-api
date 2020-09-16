package seongju.remaapi.service.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.vo.JwtResponse;
import seongju.remaapi.vo.MemberVo;

@Service
public class JwtserviceImpl implements JwtService{

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MemberVo memberVo;

    @Override
    public JwtResponse authenticate(
            String username, String password
    ) throws Exception {

        JwtResponse response = new JwtResponse();
//        response.setIdIsExisted(false);
//        response.setPwIsCorrect(false);
        response.setEmailIsAllowed(false);
        response.setLogOn(false);

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e){
            //아이디가 없을 경우
            return response;
        } catch (BadCredentialsException e){
            //비밀번호가 틀렸을 경우
//            response.setIdIsExisted(true);
            return response;
        }

        memberVo = memberDao.login(username);
        if(!memberVo.getApproval_status().equals("1")){
            //이메일 인증이 필요한 경우
//            response.setIdIsExisted(true);
//            response.setPwIsCorrect(true);
            return response;
        }

        //로그인 완료
//        response.setIdIsExisted(true);
//        response.setPwIsCorrect(true);
        response.setEmailIsAllowed(true);
        response.setLogOn(true);

        return response;
    }
}
