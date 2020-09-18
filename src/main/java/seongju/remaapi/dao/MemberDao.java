package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.MemberVo;

@Repository
@Mapper
public interface MemberDao {
    int checkId(String id);

    int checkEmail(String email);

    void addMember(MemberVo memberVo) throws Exception;

    int approval_member(MemberVo memberVo);

    MemberVo login(String id);

    void update_log(String id);

    String findId(String email);

    int updatePw(MemberVo memberVo);

    void updateName(MemberVo memberVo);

    int updateEmail(MemberVo memberVo);
}
