package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.dao.NotesListDao;
import seongju.remaapi.lib.UtilMethod;
import seongju.remaapi.service.NoteService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
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
}
