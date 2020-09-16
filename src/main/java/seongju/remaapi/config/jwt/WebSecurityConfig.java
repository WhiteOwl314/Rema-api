package seongju.remaapi.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig
        extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth
    ) throws Exception{
        auth.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
        throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
        throws Exception{
        //for CORS error
        httpSecurity.cors().configurationSource(
                request -> new CorsConfiguration().applyPermitDefaultValues()
        );
        //We don't need CSRF for this example
        httpSecurity.csrf().disable()
                //특정 경로 -> 특정 사용자 통제
                .authorizeRequests()
                    //경로
                    .antMatchers("/authenticate")
                        //모두 허용
                        .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
                //예외 처리 -> 401페이지
//                .exceptionHandling()
//                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                    .and()
                //세션 정책
                .sessionManagement()
                    //스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음 ->JWT 같은 토큰방식을 쓸때 사용하는 설정
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //필터 추가
        httpSecurity.addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
