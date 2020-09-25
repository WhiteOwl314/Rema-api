package seongju.remaapi.service;

import java.util.HashMap;

public interface ReviewService {
    String getDefaultReviewDate(String username);

    String getReviewDateList(HashMap map);

    void addReviewDate(HashMap map);

    void UpdateReviewDate(HashMap map);

    void deleteReviewDate(HashMap map);
}
