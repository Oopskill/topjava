package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

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
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        deleteRole(user);
        insertRole(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("select * from users order by name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> map = new HashMap<>();

        jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day,
                    string_agg(r.role, ', ')  AS roles FROM users u LEFT JOIN user_roles r ON r.user_id = u.id
                    GROUP BY u.id
                """, rs -> {
            do {
                Set<Role> roles = new HashSet<>();
                for (String s : rs.getString("roles").split(", ")) {
                    roles.add(Role.valueOf(s));
                }
                map.put(rs.getInt("id"), roles);
            } while (rs.next());
        });
        users.forEach(u -> u.setRoles(map.get(u.getId())));
        return users;
    }
    
    private User setRoles(User u){
        if (u != null){
            List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles  WHERE user_id=?",
                    (rs, rowNum) -> Role.valueOf(rs.getString("role")), u.getId());
            u.setRoles(roles);
//            jdbcTemplate.query("""
//                    SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day,
//                    string_agg(r.role, ', ')  AS roles FROM users u LEFT JOIN user_roles r ON r.user_id = ?\s
//                    GROUP BY u.id""", rs -> {
//                            while (rs.next()){
//                                Set<Role> roles = new HashSet<>();
//                                for (String s : rs.getString("roles").split(", ")){
//                                    roles.add(Role.valueOf(s));
//                                }
//                                u.setRoles(roles);
//                            }
//                    }, u.getId());

        }
        return u;
    }

    private void deleteRole(User u){
        jdbcTemplate.update("delete from user_roles where user_id =?", u.getId());
    }

    private void insertRole(User u){
        Set<Role> roles = u.getRoles();
        if (!CollectionUtils.isEmpty(u.getRoles())){
            jdbcTemplate.batchUpdate("insert into user_roles (user_id, role) values (?, ?) ", roles, roles.size(),
            (ps, role) -> {
                ps.setInt(1, u.getId());
                ps.setString(2, role.name());
            });
        }
    }
}
