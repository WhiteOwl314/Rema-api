package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.NoteVo;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface NoteDao {
    NoteVo getNote(Map map);

    int selectNewNO();

    void addNoteInitial(NoteVo noteVo);

    void updateContent(NoteVo noteVo);
}
