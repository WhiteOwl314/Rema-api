package seongju.remaapi.service;


import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.dao.NoteDao;
import seongju.remaapi.dao.NotesListDao;
import seongju.remaapi.vo.NoteVo;
import seongju.remaapi.vo.NotesListVo;

import java.util.HashMap;
import java.util.Map;

@Service
@CrossOrigin
@Transactional
public class NoteServiceImpl implements NoteService{

    @Autowired
    private NoteDao noteDao;
    @Autowired
    private NoteVo noteVo;
    @Autowired
    private NotesListDao notesListDao;
    @Autowired
    private NotesListVo notesListVo;

    @Override
    public String getNote(String username, int id) {

        Map map = new HashMap();
        map.put("id", id);
        map.put("member_id", username);

        //조회
        noteVo = noteDao.getNote(map);

        //title 조회
        //member_id, id 가 들어있는 notesListVo 필요
        notesListVo.setId(id);
        notesListVo.setMember_id(username);
        String title = notesListDao
                .getNotesItem(notesListVo).getTitle();

        JsonObject data = new JsonObject();

        data.addProperty("id",noteVo.getId());
        data.addProperty("note_id",noteVo.getNote_id());
        data.addProperty("title",title);
        data.addProperty("content",noteVo.getContent());
        data.addProperty("is_folder",0);

        //변환
        String noteJson = data.toString();

        return noteJson;
    }

    @Override
    public void addNoteInitial(String member_id, int note_id){
        //새로운 아이디 가져오기

        int newId = noteDao.selectNewNO();

        noteVo.setId(newId);
        noteVo.setMember_id(member_id);
        noteVo.setNote_id(note_id);

        noteDao.addNoteInitial(noteVo);
    }

    @Override
    public int isFolder(int id, String username) {

        notesListVo.setId(id);
        notesListVo.setMember_id(username);

        int is_folder = notesListDao.getNotesItem(notesListVo).getIs_folder();

        return is_folder;
    }


}
