package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.dao.NotesListDao;
import seongju.remaapi.lib.UtilMethod;
import seongju.remaapi.service.NoteService;
import seongju.remaapi.service.NotesListService;
import seongju.remaapi.vo.NoteVo;
import seongju.remaapi.vo.NotesListVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/note")
public class NoteController {
    @Autowired
    private NotesListService notesListService;
    @Autowired
    private NotesListVo notesListVo;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteVo noteVo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(
            value = "/getNote",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> getNote(
            //필요: id
            @RequestBody HashMap<String, Object> map,
            HttpServletRequest request
    ) throws Exception {

        int id = (int) map.get("id");

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //폴더인지 노트인지 구분
        int is_folder = noteService.isFolder(id, username);

        if(is_folder == 1){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("is_folder",1);

            return ResponseEntity.ok().body(jsonObject.toString());
        } else {

            //Json형태 가져오기
            String bodyMessage =
                    noteService.getNote(username, id);

            return ResponseEntity.ok().body(bodyMessage);
        }
    }


    @RequestMapping(
            value = "/updateNote",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> updateNote(
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception{
        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        int id = (int) map.get("id");
        int note_id = (int) map.get("note_id");
        String title = (String) map.get("title");
        String content = (String) map.get("content");

        //title 수정
        notesListVo.setId(note_id);
        notesListVo.setTitle(title);
        notesListVo.setMember_id(username);
        notesListService.updateName(notesListVo);

        //content 수정
        noteVo.setId(id);
        noteVo.setContent(content);
        noteVo.setMember_id(username);
        noteService.updateContent(noteVo);

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("success",true);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }
}
