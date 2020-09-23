package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

@Data
@Alias("noteVo")
@Component
public class NoteVo {
    private int id;
    private String title;
    private int order;
    private int isReview;
    private int folder_id;
    private String content;
    private String member_id;
    private int isDeleted;
    private String creDate;
    private String delDate;
}
