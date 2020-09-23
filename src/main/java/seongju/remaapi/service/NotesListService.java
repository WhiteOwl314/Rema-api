package seongju.remaapi.service;

import com.google.gson.JsonObject;
import seongju.remaapi.vo.NotesListVo;

public interface NotesListService {
    String getNotesList(String username);

    String addfolder(
            NotesListVo notesListVo,
            int currentClickId
    );

    JsonObject updateName(NotesListVo notesListVo);
}
