package seongju.remaapi.service;

public interface NoteService {
    String getNote(String username, int id);

    void addNoteInitial(String member_id, int note_id);

    int isFolder(int id, String username);
}
