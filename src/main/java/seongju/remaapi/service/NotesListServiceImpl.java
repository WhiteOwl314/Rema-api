package seongju.remaapi.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.dao.NotesListDao;
import seongju.remaapi.vo.NotesListVo;

import java.util.List;

@Service
@CrossOrigin
@Transactional
public class NotesListServiceImpl implements NotesListService {

    @Autowired
    private NotesListDao notesListDao;
    @Autowired
    private NotesListVo notesListVo;
    @Autowired
    private FolderOrderService folderOrderService;

    @Override
    public String getNotesList(String username) {

        //조회
        List<NotesListVo> notesList =
                notesListDao.getNotesList(username);
        //변환
        Gson gson = new Gson();
        String notesListJson =
                gson.toJson(notesList);

        return notesListJson;
    }

    @Override
    public String addfolder(
            NotesListVo notesListVo,
            int currentClickId
    ) {

        JsonObject result = new JsonObject();
        result.addProperty("success", false);

        try{
            notesListVo.setId(notesListDao.selectNewNO());
            notesListDao.addFolder(notesListVo);
            //folderOrder 추가
            if(currentClickId != 0){
                folderOrderService.addFolderOrder(
                        currentClickId,
                        notesListVo.getId(),
                        notesListVo.getMember_id()
                );
            }

            result.addProperty("success", true);
            return result.toString();
        } catch (Exception e){
            return result.toString();
        }
    }

    @Override
    public JsonObject updateName(NotesListVo notesListVo) {


        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("updateName",false);

        try{
            notesListDao.updateName(notesListVo);
            bodyMessage.addProperty("updateName",true);
            return bodyMessage;
        } catch (Exception e){
            return bodyMessage;
        }
    }

}
