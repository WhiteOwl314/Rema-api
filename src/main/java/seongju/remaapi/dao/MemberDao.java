package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.MemberVo;

@Repository
@Mapper
public interface MemberDao {
    int checkId(String id);

    int checkEmail(String email);

    void addMember(MemberVo memberVo);

    int approval_member(MemberVo memberVo);
}
