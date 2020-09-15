package seongju.remaapi.service.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.vo.MemberVo;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberDao memberDao;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        String id = username;

        //아이디가 있다면
        if(memberDao.checkId(id) != 0){
            //디비 불러오기
            MemberVo memberVo = memberDao.login(id);

            return new User(
                    memberVo.getId(),
                    encoder.encode(memberVo.getPw()),
                    new ArrayList<>()
            );
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
