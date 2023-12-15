package cn.dq.user.service;

import cn.dq.user.domain.Userinfo;
import cn.dq.user.vo.RegistUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface UseService extends IService<Userinfo> {


    Userinfo findOneByEmail(String email);

    boolean getCode(String email);

    void regist(RegistUserVo userinfo);

    Map<String, Object> login(String name, String password);
}
