package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

@Data
@Alias("reviewVo")
@Component
public class ReviewVo {
    private int id;
    private int note_id;
    private String date;
    private int is_default;
    private String member_id;
}
