package cn.itcast.store.dao;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserDao {

    void save(User user) throws SQLException;

    User findByCode(String code) throws SQLException;

    void update(User user) throws SQLException;

    User find(String username, String password) throws SQLException;
}
