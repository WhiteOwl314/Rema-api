package seongju.remaapi.service;

public interface FolderOrderService {
    String getFolderOrderList(String username);

    void addFolderOrder(
            int currentClickId,
            int insertId,
            String memberId
    );
}
