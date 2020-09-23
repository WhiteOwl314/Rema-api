package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

@Data
@Alias("notesListVo")
@Component
public class NotesListVo {
    private int id;
    private String title;
    private int is_first;
    private int is_folder;
    private int is_review;
    private int is_deleted;
    private String cre_date;
    private String del_date;
    private String member_id;
}
