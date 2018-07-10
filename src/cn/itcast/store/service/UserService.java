package cn.itcast.store.service;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserService {

    void regist(User user) throws SQLException;

    void activeUser(String code) throws SQLException;

    User login(User user) throws SQLException;
}
