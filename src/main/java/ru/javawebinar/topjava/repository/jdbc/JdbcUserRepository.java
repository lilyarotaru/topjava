package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> resultSetExtractor = new RowMapperResultSetExtractor<>(ROW_MAPPER) {
        @Override
        public List<User> extractData(ResultSet rs) throws DataAccessException, SQLException {
            Map<Integer, User> map = new LinkedHashMap<>();             //cause resultSet may be order by columns in table, not by id
            while (rs.next()) {
                int id = rs.getInt("id");
                User user = map.computeIfAbsent(id, key -> {
                    try {
                        User mappedUser = ROW_MAPPER.mapRow(rs, rs.getRow());
                        mappedUser.setRoles(Collections.emptyList());
                        return mappedUser;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                String roleValue = rs.getString("role");
                if (roleValue != null) {
                    user.getRoles().add(Role.valueOf(roleValue));
                }
            }
            return new ArrayList<>(map.values());
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", user.id());
        }
        if (user.getRoles() != null) {
            List<Role> roles = new ArrayList<>(user.getRoles());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role)  values (? ,?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.id());
                    ps.setString(2, roles.get(i).name());
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> userList = jdbcTemplate.query("SELECT * FROM  users LEFT JOIN user_roles ur on users.id = ur.user_id WHERE id=?",
                resultSetExtractor, id);
        return DataAccessUtils.singleResult(userList);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> userList = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur on users.id = ur.user_id WHERE email=?",
                resultSetExtractor, email);
        return DataAccessUtils.singleResult(userList);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur on users.id = ur.user_id ORDER BY name, email", resultSetExtractor);
    }
}
