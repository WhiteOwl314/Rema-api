package seongju.remaapi.service;

import seongju.remaapi.vo.MemberVo;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {
    String checkId(String id);

    String checkEmail(String email);

    String create_key() throws Exception;

    void sendEmail(MemberVo memberVo);

    void addMember(MemberVo memberVo) throws Exception;

    void approval_member(MemberVo memberVo, HttpServletResponse response) throws Exception;
}
