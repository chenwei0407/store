package cn.itcast.store.dao;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserDao {

    void saveUser(User user) throws SQLException;

    User findUserByCode(String code) throws SQLException;

    void updateUser(User user) throws SQLException;

    User findUser(String username, String password) throws SQLException;
}
