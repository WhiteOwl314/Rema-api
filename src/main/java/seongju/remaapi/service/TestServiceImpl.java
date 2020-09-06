package seongju.remaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seongju.remaapi.dao.TestDao;
import seongju.remaapi.vo.TestVo;

import java.util.List;

@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private TestDao testDao;

    @Override
    public List<TestVo> getHostName() {
        return this.testDao.getHostName();
    }
}
