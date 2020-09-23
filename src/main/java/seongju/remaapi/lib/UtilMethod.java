package seongju.remaapi.lib;

import seongju.remaapi.config.jwt.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class UtilMethod {

    public static String getUsername(
            HttpServletRequest request,
            JwtTokenUtil jwtTokenUtil
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
