package seongju.remaapi.service.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //db 불러와야 함
        if("user_id".equals(username)){
            return new User(
                    "user_id",
                    "$2a$10$UR3xWG7Z2Q.W9dJfA2DJDu4lHy1b.2HwHpWwD5RgiFZAa29y/CJE6",
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
