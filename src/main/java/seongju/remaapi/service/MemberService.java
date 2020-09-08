package seongju.remaapi.service;

import com.google.gson.JsonObject;
import seongju.remaapi.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MemberService {
    String checkId(String id);

    boolean checkIdBoolean(String id);

    String checkEmail(String email);

    String create_key() throws Exception;

    void sendEmail(MemberVo memberVo);

    void addMember(MemberVo memberVo) throws Exception;

    void approval_member(MemberVo memberVo, HttpServletResponse response) throws Exception;

    JsonObject login(
            MemberVo memberVo,
            HttpServletRequest request
    ) throws IOException;

    String findId(String email);
}
