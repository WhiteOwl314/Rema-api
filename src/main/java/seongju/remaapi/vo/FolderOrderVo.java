package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

@Data
@Alias("folderOrderVo")
@Component
public class FolderOrderVo {
    private int id;
    private int folder_id;
    private int in_id;
    private String member_id;
}
