package seongju.remaapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import seongju.remaapi.vo.FolderOrderVo;
import seongju.remaapi.vo.NotesListVo;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FolderOrderDao {
    List<NotesListVo> getFolderOrderList(String username);

    FolderOrderVo getFolderOrderByInId(FolderOrderVo folderOrderVo);

    void addFolderOrder(Map map);

    int selectNewId();
}
