package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.SortField;
import org.endofusion.endoserver.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("UserRepositoryCustom")
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<UserDto> fetchFirstNames() {

        String sqlQuery = "select distinct(u.first_name) as firstName \n" +
                "from user as u\n" +
                "order by u.first_name asc";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserDto.class));
    }

    @Override
    public List<UserDto> fetchLastNames() {

        String sqlQuery = "select distinct(u.last_name) as lastName \n" +
                "from user as u\n" +
                "order by u.last_name asc";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserDto.class));
    }

    @Override
    public List<UserDto> fetchUsernames() {

        String sqlQuery = "select distinct(u.username) as username \n" +
                "from user as u\n" +
                "order by u.username asc";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserDto.class));
    }

    @Override
    public List<UserDto> fetchEmails() {

        String sqlQuery = "select distinct(u.email) as email \n" +
                "from user as u\n" +
                "order by u.email asc";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserDto.class));
    }

    @Override
    public Page<UserDto> getUsersList(Pageable pageable, UserDto dto) {
        String sqlFromClause = "From user as u\n";

        String sqlWhereClause = "WHERE u.id is not null\n";

        MapSqlParameterSource in = new MapSqlParameterSource();

        if (dto.getUserId() != null) {
            sqlWhereClause += "AND u.user_id like :userId\n";
            in.addValue("userId", "%" + dto.getUserId() + "%");
        }

        if (dto.getUsername() != null) {
            sqlWhereClause += "AND u.username like :username\n";
            in.addValue("username", "%" + dto.getUsername() + "%");
        }

        if (dto.getFirstName() != null) {
            sqlWhereClause += "AND u.first_name like :firstName\n";
            in.addValue("firstName", "%" + dto.getFirstName() + "%");
        }

        if (dto.getLastName() != null) {
            sqlWhereClause += "AND u.last_name like :lastName\n";
            in.addValue("lastName", "%" + dto.getLastName() + "%");
        }

        if (dto.getEmail() != null) {
            sqlWhereClause += "AND u.email like :email\n";
            in.addValue("email", "%" + dto.getEmail() + "%");
        }

        if (dto.getStatus() != null) {
            sqlWhereClause += "AND u.status like :status\n";
            in.addValue("status", "%" + dto.getStatus() + "%");
        }

        if (dto.getLocked() != null) {
            sqlWhereClause += "AND u.is_not_locked like :locked\n";
            in.addValue("locked", "%" + dto.getLocked() + "%");
        }

        List<String> validSortColumns = new ArrayList<String>();
        validSortColumns.add("ID");
        validSortColumns.add("FIRST_NAME");
        validSortColumns.add("LAST_NAME");
        validSortColumns.add("USERNAME");
        validSortColumns.add("EMAIL");
        validSortColumns.add("STATUS");
        validSortColumns.add("LOCKED");

        List<Sort.Order> sortOrders = pageable.getSort().stream().collect(Collectors.toList());
        Sort.Order order = sortOrders.get(0);

        String sqlPaginationClause = "ORDER BY ";

        if (validSortColumns.contains(order.getProperty())) {
            sqlPaginationClause += SortField.Field.valueOf(order.getProperty()).getValue();
            if (order.getDirection().isAscending()) {
                sqlPaginationClause += " ASC ";
            } else {
                sqlPaginationClause += " DESC ";
            }
        }

        sqlPaginationClause += "limit :offset,:size";

        in.addValue("offset", pageable.getOffset());
        in.addValue("size", pageable.getPageSize());

        String rowCountSql = "SELECT count(*) AS row_count " +
                sqlFromClause + sqlWhereClause;
        ;

        int total = this.namedParameterJdbcTemplate.queryForObject(
                rowCountSql, in, Integer.class);

        String sqlQuery = "Select u.id as id, u.user_id as userId, u.username as username, u.first_name as firstName,\n" +
                "u.last_name as lastName, u.email as email, u.is_active as status, u.is_not_locked as locked \n" +

                sqlFromClause + sqlWhereClause + sqlPaginationClause;

        List<UserDto> res = namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(UserDto.class));

        return new PageImpl<>(res, pageable, total);
    }

    @Override
    public boolean updateUser(UserDto userDto) {

        String sqlQuery = "UPDATE user SET\n " +
                "user_id = :userId,\n " +
                "username = :username,\n " +
                "first_name = :firstName,\n " +
                "last_name = :lastName,\n " +
                "email = :email,\n " +
                "is_not_locked = :locked,\n " +
                "is_active = :status,\n " +
                "profile_image = :profileImage\n " +
                "WHERE id = :id";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", userDto.getId());
        in.addValue("userId", userDto.getUserId());
        in.addValue("username", userDto.getUsername());
        in.addValue("firstName", userDto.getFirstName());
        in.addValue("lastName", userDto.getLastName());
        in.addValue("email", userDto.getEmail());
        in.addValue("status", userDto.getStatus());
        in.addValue("locked", userDto.getLocked());
        in.addValue("profileImage", userDto.getProfileImage());

        return namedParameterJdbcTemplate.update(sqlQuery, in) > 0;
    }

    @Override
    public UserDto getUserById(long id) {

        String sqlQuery = "SELECT u.id as id,\n" +
                "u.user_id as userId,\n" +
                "u.username as username,\n" +
                "u.first_name as firstName,\n" +
                "u.last_name as lastName,\n" +
                "u.email as email,\n" +
                "u.is_active as status, \n" +
                "u.is_not_locked as locked, \n" +
                "u.profile_image as profileImage \n" +
                "FROM user AS u\n" +
                "WHERE u.id = :id";

        MapSqlParameterSource in = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.queryForObject(sqlQuery, in, (resultSet, i) -> {

            UserDto userDto = new UserDto();
            userDto.setId(resultSet.getLong("id"));
            userDto.setUserId(resultSet.getNString("userId"));
            userDto.setUsername(resultSet.getNString("username"));
            userDto.setFirstName(resultSet.getNString("firstName"));
            userDto.setLastName(resultSet.getNString("lastName"));
            userDto.setEmail(resultSet.getNString("email"));
            userDto.setStatus(resultSet.getBoolean("status"));
            userDto.setLocked(resultSet.getBoolean("locked"));
            userDto.setProfileImage(resultSet.getBytes("profileImage"));

            return userDto;
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        MapSqlParameterSource in = new MapSqlParameterSource("id", id);

        String sqlQueryOne = "DELETE FROM confirmation_token where user_id = :id";
        namedParameterJdbcTemplate.update(sqlQueryOne, in);

        String sqlQueryTwo = "DELETE FROM user WHERE id = :id";
        namedParameterJdbcTemplate.update(sqlQueryTwo, in);

        return true;
    }
}
