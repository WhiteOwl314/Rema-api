package seongju.remaapi.service;

import seongju.remaapi.vo.NoteVo;

public interface NoteService {
    String getNote(String username, int id);

    void addNoteInitial(String member_id, int note_id);

    int isFolder(int id, String username);

    void updateContent(NoteVo noteVo);
}
