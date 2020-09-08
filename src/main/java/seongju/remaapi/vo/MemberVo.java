package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("memberVo")
public class MemberVo {
    private String id;
    private String pw;
    private int level;
    private String name;
    private String email;
    private String isDeleted;
    private String log_date;
    private String approval_status;
    private String approval_key;
    private String reg_date;
}
