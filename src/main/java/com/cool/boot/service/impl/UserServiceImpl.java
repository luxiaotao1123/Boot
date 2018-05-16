package com.cool.boot.service.impl;

import com.cool.boot.entity.Response;
import com.cool.boot.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vincent
 */
@Service("userServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {


    /**
     * 登录
     * @return
     */
    @Override
    public Response login() {
        return null;
    }
}
