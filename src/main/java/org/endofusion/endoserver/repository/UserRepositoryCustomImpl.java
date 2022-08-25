package org.endofusion.endoserver.repository;

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

        List<String> validSortColumns = new ArrayList<String>();
        validSortColumns.add("ID");
        validSortColumns.add("FIRST_NAME");
        validSortColumns.add("LAST_NAME");
        validSortColumns.add("USERNAME");
        validSortColumns.add("EMAIL");
        validSortColumns.add("STATUS");

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
                "u.last_name as lastName, u.email as email, u.is_active as status \n" +

                sqlFromClause + sqlWhereClause + sqlPaginationClause;

        List<UserDto> res = namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(UserDto.class));

        return new PageImpl<>(res, pageable, total);
    }
}
