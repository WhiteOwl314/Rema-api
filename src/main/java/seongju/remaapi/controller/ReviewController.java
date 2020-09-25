package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.lib.UtilMethod;
import seongju.remaapi.service.ReviewService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/review")
public class ReviewController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ReviewService reviewService;

    @RequestMapping(
            value = "/getDefaultReviewDate",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> getDefaultReviewDate(
            HttpServletRequest request
    ) throws Exception {

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //Json형태 가져오기
        String bodyMessage =
                reviewService.getDefaultReviewDate(username);

        return ResponseEntity.ok().body(bodyMessage);
    }


    @RequestMapping(
            value = "/getReviewDateList",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> getReviewDateList(
            //map: {id}
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception {

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        map.put("member_id",username);

        //Json형태 가져오기
        String bodyMessage =
                reviewService.getReviewDateList(map);

        return ResponseEntity.ok().body(bodyMessage);
    }

    @RequestMapping(
            value = "/addReviewDate",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> addReviewDate(
            //map: {note_id}
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception {

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        map.put("member_id",username);

        reviewService.addReviewDate(map);

        //Json형태 가져오기
        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("success",true);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }


    @RequestMapping(
            value = "/updateReviewDate",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> UpdateReviewDate(
            //{id,date}
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception{
        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //title 수정
        map.put("member_id",username);
        reviewService.UpdateReviewDate(map);

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("success",true);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }


    @RequestMapping(
            value = "/deleteReviewDate",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> deleteReviewDate(
            //{id,}
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception{
        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //삭제
        map.put("member_id",username);
        reviewService.deleteReviewDate(map);

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("success",true);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }
}
