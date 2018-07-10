package cn.itcast.store.service.impl;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.dao.impl.UserDaoImpl;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.utils.MailUtils;

import javax.mail.MessagingException;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    UserDao userDao = new UserDaoImpl();

    @Override
    public void regist(User user) throws SQLException {
        //保存用户
        userDao.save(user);

        //发送邮件
        try {
            MailUtils.sendMail(user.getEmail(), user.getCode());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activeUser(String code) throws SQLException {
       if (code != null && code != "") {

           User existUser = userDao.findByCode(code);


           if (null == existUser) {
               throw new RuntimeException("用户激活无效，请从新发送邮件");
           }

           existUser.setState(1);
           existUser.setCode(null);
           userDao.update(existUser);
       } else {
           throw new RuntimeException("用户激活无效，请从新发送邮件");
       }

    }

    @Override
    public User login(User user) throws SQLException {
        return userDao.find(user.getUsername(), user.getPassword());
    }
}
