package seongju.remaapi.service;

import seongju.remaapi.vo.MemberVo;

public interface MemberService {
    String checkId(String id);

    String checkEmail(String email);

    String create_key() throws Exception;

    void sendEmail(MemberVo memberVo);

    void addMember(MemberVo memberVo) throws Exception;
}
