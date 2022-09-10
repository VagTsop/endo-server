package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.SortField;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository("InstrumentRepository")
public class InstrumentRepositoryImpl implements InstrumentRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<InstrumentDto> fetchInstruments() {

        String sqlQuery = "select distinct(i.name) as name \n" +
                "from instruments as i\n" +
                "order by i.name asc";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {

        String sqlQuery = "select ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "from instruments_series as ins \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public Page<InstrumentDto> getInstrumentsList(Pageable pageable, InstrumentDto dto) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String sqlFromClause = "From instruments as i\n" +
                "left join instruments_series as os on i.instrument_series_id = os.id\n";

        String sqlWhereClause = "WHERE i.id is not null\n";

        MapSqlParameterSource in = new MapSqlParameterSource();

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            sqlWhereClause += "AND i.name like :name\n";
            in.addValue("name", "%" + dto.getName() + "%");
        }

        if (dto.getPurchaseDateFrom() != null) {
            sqlWhereClause += "AND i.purchase_date >= :purchaseDateFrom ";
            in.addValue("purchaseDateFrom", formatter.format(dto.getPurchaseDateFrom()));
        }

        if (dto.getPurchaseDateTo() != null) {
            sqlWhereClause += "AND i.purchase_date <= :purchaseDateTo ";
            in.addValue("purchaseDateTo", formatter.format(dto.getPurchaseDateTo()));
        }

        if (dto.getInstrumentSeriesCodesList() != null) {
            sqlWhereClause += "AND i.instrument_series_id in ( :instrumentSeriesCodesList)";
            in.addValue("instrumentSeriesCodesList", dto.getInstrumentSeriesCodesList());
        }

        List<String> validSortColumns = new ArrayList<String>();
        validSortColumns.add("INSTRUMENT_NAME");
        validSortColumns.add("INSTRUMENT_REF");
        validSortColumns.add("INSTRUMENT_LOT");
        validSortColumns.add("INSTRUMENT_MANUFACTURER");
        validSortColumns.add("INSTRUMENT_PURCHASE_DATE");
        validSortColumns.add("INSTRUMENT_SERIES_CODE");

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

        String sqlQuery = "Select i.id as id, i.name as name, i.description as description,\n" +
                "i.ref as instrumentRef, i.lot as instrumentLot, i.manufacturer as instrumentManufacturer,\n" +
                "i.purchase_date as instrumentPurchaseDate, i.notes as instrumentNotes,\n" +
                "os.instrument_series_qr_code as instrumentSeriesQrCode \n" +
                sqlFromClause + sqlWhereClause + sqlPaginationClause;

        List<InstrumentDto> res = namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(InstrumentDto.class));

        return new PageImpl<>(res, pageable, total);
    }


    @Override
    public long createInstrument(InstrumentDto instrumentDto) {

        String sqlQuery = " INSERT INTO instruments (\n" +
                "name,\n" +
                "description,\n" +
                "ref,\n" +
                "lot,\n" +
                "manufacturer,\n" +
                "purchase_date,\n" +
                "user_photo,\n" +
                "notes\n" +
                ") VALUES (\n" +
                ":name,\n" +
                ":description,\n" +
                ":instrumentRef,\n" +
                ":instrumentLot,\n" +
                ":instrumentManufacturer,\n" +
                ":instrumentPurchaseDate,\n" +
                ":userPhoto,\n" +
                ":instrumentNotes" +
                ")";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("name", instrumentDto.getName());
        in.addValue("description", instrumentDto.getDescription());
        in.addValue("instrumentRef", instrumentDto.getInstrumentRef());
        in.addValue("instrumentLot", instrumentDto.getInstrumentLot());
        in.addValue("instrumentLot", instrumentDto.getInstrumentLot());
        in.addValue("instrumentManufacturer", instrumentDto.getInstrumentManufacturer());
        in.addValue("instrumentPurchaseDate", instrumentDto.getInstrumentPurchaseDate());
        in.addValue("userPhoto", instrumentDto.getUserPhoto());
        in.addValue("instrumentNotes", instrumentDto.getInstrumentNotes());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sqlQuery, in, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();

    }

    @Override
    public boolean updateInstrument(InstrumentDto instrumentDto) {

        String sqlQuery = "UPDATE instruments SET\n " +
                "name = :name,\n " +
                "description = :description,\n " +
                "ref = :instrumentRef,\n " +
                "lot = :instrumentLot,\n " +
                "manufacturer = :instrumentManufacturer,\n " +
                "purchase_date = :instrumentPurchaseDate,\n " +
                "user_photo = :userPhoto,\n " +
                "notes = :instrumentNotes\n " +
                "WHERE id = :id";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", instrumentDto.getId());
        in.addValue("name", instrumentDto.getName());
        in.addValue("description", instrumentDto.getDescription());
        in.addValue("instrumentRef", instrumentDto.getInstrumentRef());
        in.addValue("instrumentLot", instrumentDto.getInstrumentLot());
        in.addValue("instrumentManufacturer", instrumentDto.getInstrumentManufacturer());
        in.addValue("instrumentPurchaseDate", instrumentDto.getInstrumentPurchaseDate());
        in.addValue("userPhoto", instrumentDto.getUserPhoto());
        in.addValue("instrumentNotes", instrumentDto.getInstrumentNotes());

        return namedParameterJdbcTemplate.update(sqlQuery, in) > 0;
    }

    @Override
    public InstrumentDto getInstrumentById(long id) {

        String sqlQuery = "SELECT i.id as id,\n" +
                "i.name as name,\n" +
                "i.description as description,\n" +
                "i.ref as instrumentRef,\n" +
                "i.lot as instrumentLot,\n" +
                "i.manufacturer as instrumentManufacturer,\n" +
                "i.purchase_date as instrumentPurchaseDate,\n" +
                "i.user_photo as userPhoto,\n" +
                "i.notes as instrumentNotes \n" +
                "FROM instruments AS i\n" +
                "WHERE i.id = :instrumentId";

        MapSqlParameterSource in = new MapSqlParameterSource("instrumentId", id);

        return namedParameterJdbcTemplate.queryForObject(sqlQuery, in, (resultSet, i) -> {

            InstrumentDto instrumentDto = new InstrumentDto();
            instrumentDto.setId(resultSet.getLong("id"));
            instrumentDto.setName(resultSet.getNString("name"));
            instrumentDto.setDescription(resultSet.getNString("description"));
            instrumentDto.setInstrumentRef(resultSet.getNString("instrumentRef"));
            instrumentDto.setInstrumentLot(resultSet.getNString("instrumentLot"));
            instrumentDto.setInstrumentManufacturer(resultSet.getNString("instrumentManufacturer"));
            instrumentDto.setInstrumentPurchaseDate(resultSet.getDate("instrumentPurchaseDate"));
            instrumentDto.setUserPhoto(resultSet.getBytes("userPhoto"));
            instrumentDto.setInstrumentNotes(resultSet.getNString("instrumentNotes"));

            return instrumentDto;
        });
    }
    @Override
    public boolean deleteInstrument(Long id) {
        MapSqlParameterSource in = new MapSqlParameterSource("id", id);
        String sqlQuery = "DELETE FROM instruments WHERE id = :id";
        return namedParameterJdbcTemplate.update(sqlQuery, in) > 0;
    }
}
