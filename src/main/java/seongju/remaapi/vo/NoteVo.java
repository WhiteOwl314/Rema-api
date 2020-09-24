package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

@Data
@Alias("noteVo")
@Component
public class NoteVo {
    private int id;
    private int note_id;
    private String content;
    private String member_id;
    private int is_deleted;
    private String cre_date;
    private String del_date;
}
