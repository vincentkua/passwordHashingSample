package vk.loginsample.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vk.loginsample.models.User;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String INSERTUSERSQL = """
            insert into userregistry (user_name , hashed_password) values (?,?);
            """;

    private static final String FINDUSERSQL = """
            select * from userregistry where user_name = ?;
            """;

    public Integer insertUser(User user) {
        Integer rowsUpdated = jdbcTemplate.update(INSERTUSERSQL, user.getUserName(), user.getHashedPassword());
        return rowsUpdated;
    }

    public User findUser(String username) {
        User userfound = jdbcTemplate.queryForObject(FINDUSERSQL, BeanPropertyRowMapper.newInstance(User.class),
                username);
        return userfound;
    }

}
