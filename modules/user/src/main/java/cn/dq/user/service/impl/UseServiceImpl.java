package cn.dq.user.service.impl;

import cn.dq.user.domain.Userinfo;
import cn.dq.user.mapper.UseMappser;
import cn.dq.user.service.UseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UseServiceImpl extends ServiceImpl<UseMappser, Userinfo> implements UseService {
}
