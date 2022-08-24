package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.dto.SortField;
import org.endofusion.endoserver.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository("UserRepository")
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public void enableAppUser(String email) {
    }

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

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends User> S save(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public User getOne(Long aLong) {
        return null;
    }

    @Override
    public User getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}

