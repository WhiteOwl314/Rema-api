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

    //이메일 보내기
    void sendEmail(
            MemberVo memberVo,
            String kind
    ) throws Exception;

    JsonObject addMember(MemberVo memberVo) throws Exception;

    void approval_member(MemberVo memberVo, HttpServletResponse response) throws Exception;

    JsonObject login(
            MemberVo memberVo,
            HttpServletRequest request
    ) throws IOException;

    JsonObject findId(String email);

    JsonObject findPw(String id, String email) throws Exception;

    JsonObject updatePw(
            MemberVo memberVo,
            String oldPw,
            HttpServletRequest request
    )throws Exception;
}
