package seongju.remaapi.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.dao.FolderOrderDao;
import seongju.remaapi.dao.NotesListDao;
import seongju.remaapi.vo.FolderOrderVo;
import seongju.remaapi.vo.NotesListVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CrossOrigin
@Transactional
public class FolderOrderServiceImpl implements FolderOrderService{

    @Autowired
    private FolderOrderDao folderOrderDao;
    @Autowired
    private FolderOrderVo folderOrderVo;
    @Autowired
    private NotesListDao notesListDao;
    @Autowired
    private NotesListVo notesListVo;

    @Override
    public String getFolderOrderList(String username) {

        //조회
        List<NotesListVo> folderOrderList =
                folderOrderDao.getFolderOrderList(username);
        //변환
        Gson gson = new Gson();
        String folderOrderListJson =
                gson.toJson(folderOrderList);

        return folderOrderListJson;
    }

    @Override
    public void addFolderOrder(
            int currentClickId,
            int in_id,
            String member_id
    ){
        Map map = new HashMap();

        //기본세팅
        map.put("currentClickId",currentClickId);
        map.put("folder_id",currentClickId);
        map.put("in_id",in_id);
        map.put("member_id",member_id);

        //currentClickId 가 폴더인지 조회
        notesListVo.setMember_id(member_id);
        notesListVo.setId(currentClickId);
        notesListVo = notesListDao.getNotesItem(notesListVo);

        //만약 노트라면
        if(notesListVo.getIs_folder() == 0){
            //상위폴더 조회
            folderOrderVo.setIn_id(notesListVo.getId());
            folderOrderVo.setMember_id(member_id);
            folderOrderVo = folderOrderDao
                    .getFolderOrderByInId(folderOrderVo);
            //folder_id put
            map.put("folder_id",folderOrderVo.getFolder_id());
        }

        //폴더오더 추가
            //새로운 아이디 가져오기
            int newId = folderOrderDao.selectNewId();
            map.put("id",newId);
        folderOrderDao.addFolderOrder(map);
    }

}
