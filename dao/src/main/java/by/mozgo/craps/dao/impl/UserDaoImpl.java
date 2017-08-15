package by.mozgo.craps.dao.impl;

import by.mozgo.craps.dao.UserDao;
import by.mozgo.craps.dao.exception.DaoException;
import by.mozgo.craps.entity.User;
import by.mozgo.craps.util.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    private static final String TABLE_NAME = "user";
    private static final String QUERY_INSERT = "INSERT INTO user (email, password, username) VALUES (?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE user SET email=?, username=?, money=? WHERE id = ?";
    private static final String QUERY_UPDATE_WITH_PASS = "UPDATE user SET email=?, password=?, username=?, money=? WHERE id = ?";
    private static final String QUERY_UPDATE_ROLE = "UPDATE user SET role_id = ? WHERE id = ?";
    private static final String QUERY_GET_ALL = "SELECT user.id, email, username, create_time, money, role FROM user INNER JOIN role ON user.role_id=role.id ORDER BY user.id LIMIT ?,?";
    private static final String QUERY_GET_USERS_NUMBER = "SELECT COUNT(*) FROM user";
    private static final String QUERY_GET_PASSWORD = "SELECT password FROM user WHERE email = ?";
    private static final String QUERY_FIND_USER = "SELECT user.id, email, username, create_time, money, role FROM user INNER JOIN role ON user.role_id=role.id WHERE email = ?";

    private static UserDaoImpl instance = null;

    public UserDaoImpl() {
        tableName = TABLE_NAME;
    }

    public static UserDaoImpl getInstance() {
        if (instance == null) instance = new UserDaoImpl();
        return instance;
    }

    @Override
    public String getPassword(String email) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        String pass = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_PASSWORD)) {
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()) {
                pass = result.getString(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return pass;
    }

    @Override
    public User findUserByEmail(String email) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_USER)) {
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();
            if (result.isBeforeFirst()) {
                result.next();
                user = new User();
                user.setId(result.getInt(1));
                user.setEmail(result.getString(2));
                user.setUsername(result.getString(3));
                user.setCreateTime(result.getTimestamp(4).toLocalDateTime());
                user.setBalance(result.getBigDecimal(5));
                user.setUserRole(User.UserRole.valueOf(result.getString(6).toUpperCase()));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public int create(User entity) throws DaoException {
        Integer id;
        connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_INSERT)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getUsername());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            } else {
                throw new DaoException("Unable to create entity");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return id;
    }

    @Override
    public User findEntityById(Integer id) {
        return null;
    }

    @Override
    public List getAll(int recordsPerPage, int currentPage) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();

        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_ALL)) {
            preparedStatement.setInt(1, (currentPage - 1) * recordsPerPage);
            preparedStatement.setInt(2, recordsPerPage);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt(1));
                user.setEmail(result.getString(2));
                user.setUsername(result.getString(3));
                user.setCreateTime(result.getTimestamp(4).toLocalDateTime());
                user.setBalance(result.getBigDecimal(5));
                user.setUserRole(User.UserRole.valueOf(result.getString(6).toUpperCase()));
                users.add(user);
            }
            result.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }

    public void updateRole(Integer userId, int role) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_ROLE)) {
            ps.setInt(1, role);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public int getNumber() throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        int amount;
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(QUERY_GET_USERS_NUMBER);
            result.next();
            amount = result.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return amount;
    }

    public void update(User entity) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getUsername());
            ps.setBigDecimal(3, entity.getBalance());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void updateWithPass(User entity) throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_WITH_PASS)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getUsername());
            ps.setBigDecimal(4, entity.getBalance());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}