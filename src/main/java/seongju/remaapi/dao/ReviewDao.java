package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.ReviewVo;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface ReviewDao {
    ReviewVo getDefaultReviewDate(String username);

    List<ReviewVo> getReviewDateList(HashMap map);

    void addReviewDate(HashMap map);

    int selectNewNO();

    void UpdateReviewDate(HashMap map);

    void deleteReviewDate(HashMap map);
}
