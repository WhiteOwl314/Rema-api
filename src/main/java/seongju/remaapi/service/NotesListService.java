package seongju.remaapi.service;

import seongju.remaapi.vo.NotesListVo;

public interface NotesListService {
    String getNotesList(String username);

    String addfolder(
            NotesListVo notesListVo,
            int currentClickId
    );
}
