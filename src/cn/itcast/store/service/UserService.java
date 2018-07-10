package cn.itcast.store.service;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserService {

    void registerUser(User user) throws SQLException;

    void activeUser(String code) throws SQLException;

    User loginUser(User user) throws SQLException;
}
