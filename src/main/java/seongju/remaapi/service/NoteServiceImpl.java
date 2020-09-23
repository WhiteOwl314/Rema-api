package seongju.remaapi.service;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.dao.NoteDao;
import seongju.remaapi.vo.NoteVo;

import java.util.List;

@Service
@CrossOrigin
@Transactional
public class NoteServiceImpl implements NoteService{

    @Autowired
    private NoteDao noteDao;
    @Autowired
    private NoteVo noteVo;

    @Override
    public String getNoteList(String username) {
        //조회
        List<NoteVo> noteList =
                noteDao.getNoteList(username);
        //변환
        Gson gson = new Gson();
        String noteListJson =
                gson.toJson(noteList);

        return noteListJson;
    }
}
