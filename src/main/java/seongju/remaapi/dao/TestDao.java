package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.TestVo;

import java.util.List;

@Repository
@Mapper
public interface TestDao {
    List<TestVo> getHostName();
}
