package cn.itcast.store.dao.impl;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    /**
     * 保存用户
     *
     * @param user
     * @throws SQLException
     */
    @Override
    public void saveUser(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into " +
                "user(uid, username, password, name, email, telephone, birthday, sex, state, code) " +
                "values (?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
                user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(),
                user.getCode()};

        queryRunner.update(sql, params);
    }

    /**
     * 用户注册码寻找用户
     * @param code
     * @return
     * @throws SQLException
     */
    @Override
    public User findUserByCode(String code) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select " +
                "u.uid, u.username, u.password, u.name, u.email, u.telephone, u.birthday, u.sex, u.state, u.code " +
                "from user u where code = ?";

        User existUser = queryRunner.query(sql, new BeanHandler<>(User.class), code);

        return existUser;
    }

    /**
     * 更新用户
     * @param user
     * @throws SQLException
     */
    @Override
    public void updateUser(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "update user set " +
                "username=?, password=?, name=?, email=?, telephone=?, birthday=?, sex=?, state=?, code=? " +
                "where uid=?";

        Object[] params = {user.getUsername(), user.getPassword(), user.getName(),
                user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode(), user.getUid()};

        queryRunner.update(sql, params);
    }

    /**
     * 寻找用户
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    @Override
    public User findUser(String username, String password) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select u.uid, u.username, u.password, u.name, u.email, u.telephone, u.birthday, u.sex, u.state, u.code " +
                "from user u where u.username=? and u.password=?";

        User existUser = queryRunner.query(sql, new BeanHandler<>(User.class), username, password);

        return existUser;
    }
}
