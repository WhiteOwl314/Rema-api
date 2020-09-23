package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.NoteVo;

import java.util.List;

@Repository
@Mapper
public interface NoteDao {
    List<NoteVo> getNoteList(String username);
}
