package seongju.remaapi.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.dao.ReviewDao;
import seongju.remaapi.vo.NotesListVo;
import seongju.remaapi.vo.ReviewVo;

import java.util.HashMap;
import java.util.List;

@Service
@CrossOrigin
@Transactional
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private ReviewVo reviewVo;

    @Override
    public String getDefaultReviewDate(String username) {
        //조회
        reviewVo = reviewDao.getDefaultReviewDate(username);
        //변환
        Gson gson = new Gson();
        String reviewJsonJson = gson.toJson(reviewVo);

        return reviewJsonJson;
    }

    @Override
    public String getReviewDateList(HashMap map) {

        List<ReviewVo> reviewDateList =
                reviewDao.getReviewDateList(map);

        //변환
        Gson gson = new Gson();
        String reviewDateListJson =
                gson.toJson(reviewDateList);

        return reviewDateListJson;
    }

    @Override
    public void addReviewDate(HashMap map) {
        map.put("id", reviewDao.selectNewNO());

        reviewDao.addReviewDate(map);
    }

    @Override
    public void UpdateReviewDate(HashMap map) {
        reviewDao.UpdateReviewDate(map);
    }

    @Override
    public void deleteReviewDate(HashMap map) {
        reviewDao.deleteReviewDate(map);
    }


    @Override
    public String getReviewDateListByDate(HashMap map) {

        List<NotesListVo> reviewDateList =
                reviewDao.getReviewDateListByDate(map);

        //변환
        Gson gson = new Gson();
        String reviewDateListJson =
                gson.toJson(reviewDateList);

        return reviewDateListJson;
    }

}
