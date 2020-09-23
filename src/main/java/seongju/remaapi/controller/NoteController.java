package seongju.remaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seongju.remaapi.config.jwt.JwtTokenUtil;
import seongju.remaapi.lib.UtilMethod;
import seongju.remaapi.service.NoteService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(
            value = "/getNoteList",
            method = RequestMethod.GET
    )
    public ResponseEntity<?> getNoteList(
            HttpServletRequest request
    ) throws Exception {

        //Id 가져오기
        String username = UtilMethod.getUsername(
                request,
                jwtTokenUtil
        );

        //Json형태의 FolderList 가져오기
        String bodyMessage =
                noteService.getNoteList(username);

        return ResponseEntity.ok().body(bodyMessage);
    }
}
