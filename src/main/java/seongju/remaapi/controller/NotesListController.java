package seongju.remaapi.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.lib.UtilMethod;
import seongju.remaapi.service.NotesListService;
import seongju.remaapi.vo.NotesListVo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/notesList")
public class NotesListController {
    @Autowired
    private NotesListService notesListService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private NotesListVo notesListVo;

    @RequestMapping(
            value = "/getNotesList",
            method = RequestMethod.GET
    )
    public ResponseEntity<?> getNotesList(
            HttpServletRequest request
    ) throws Exception {

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //Json형태의 NotesList 가져오기
        String bodyMessage =
                notesListService.getNotesList(username);

        return ResponseEntity.ok().body(bodyMessage);
    }

    @RequestMapping(
            value="/addNotesList",
            method= RequestMethod.POST
    )
    public ResponseEntity<?> addFolder(
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {

        notesListVo.setTitle((String) map.get("title"));
        notesListVo.setIs_first((Integer) map.get("is_first"));
        notesListVo.setIs_folder((Integer) map.get("is_folder"));
        String currentClickId = (String) map.get("current");


        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        notesListVo.setMember_id(username);

        //addFolder -> success: true
        String bodyMessage = null;

        if(currentClickId.equals("background")){
            bodyMessage = notesListService.addfolder(notesListVo,0);
        } else{
            bodyMessage = notesListService
                    .addfolder(
                            notesListVo,
                            Integer.parseInt(currentClickId)
                    );
        }

        return ResponseEntity.ok().body(bodyMessage);
    }


    @RequestMapping(
            value = "/updateName",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> updateName(
            @RequestBody HashMap map,
            HttpServletRequest request
    ) throws Exception{
        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );
        notesListVo.setTitle((String) map.get("title"));
        notesListVo.setId((Integer) map.get("id"));
        notesListVo.setMember_id(username);


        JsonObject bodyMessage =
                notesListService.updateName(notesListVo);

        return ResponseEntity.ok().body(bodyMessage.toString());
    }
}
