package seongju.remaapi.service;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seongju.remaapi.dao.MemberDao;

@Service
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;

    @Override
    public String checkId(String id) {

        boolean idIsExisted ;

        int idCount = memberDao.checkId(id);

        if(idCount == 0){
            idIsExisted = false;
        }else{
            idIsExisted = true;
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("idIsExisted",idIsExisted);

        return obj.toString();
    }
}
