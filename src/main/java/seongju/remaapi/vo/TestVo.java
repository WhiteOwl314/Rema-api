package seongju.remaapi.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("testVo")
public class TestVo {
    private int id;
    private String title;
}
