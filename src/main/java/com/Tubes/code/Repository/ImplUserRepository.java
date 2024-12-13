package com.Tubes.code.Repository;

import com.Tubes.code.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ImplUserRepository implements UserRepository {

    @Autowired
    private JdbcTemplate template;

    @Override
    public void save(User user) throws Exception {

    }

    @Override
    public Optional<User> find(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        List<User> result = template.query(sql,this::mapRowToUser,username,password);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("namalengkap"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
