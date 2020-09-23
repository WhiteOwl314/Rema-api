package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.NotesListVo;

import java.util.List;

@Repository
@Mapper
public interface NotesListDao {
    List<NotesListVo> getNotesList(String username);

    void addFolder(NotesListVo notesListVo);

    int selectNewNO();

    NotesListVo getNotesItem(NotesListVo notesListVo);

    void updateName(NotesListVo notesListVo);

    void deleteNotesList(NotesListVo notesListVo);
}
