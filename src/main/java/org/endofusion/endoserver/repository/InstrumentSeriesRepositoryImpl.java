package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("InstrumentSeriesRepository")
public class InstrumentSeriesRepositoryImpl implements InstrumentSeriesRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {

        String sqlQuery = "select ins.id, ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "from instruments_series as ins \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentDto> fetchAvailableInstruments() {

        String sqlQuery = "select i.id as id, i.name as name, i.description AS description, i.lot AS instrumentLot \n" +
                "from instruments as i where available = 1 \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentSeriesDto> getInstrumentSeriesList() {

        String sqlFromClause = "From instruments as i\n" +
                " INNER JOIN instruments_series AS ins ON i.instrument_series_id = ins.id \n";

        String sqlWhereClause = "WHERE i.id is not null\n";

        String groupByClause = "GROUP BY instrumentSeriesCode, name, description, instrumentLot, id  order by instrumentSeriesCode \n";

        String sqlQuery = "Select i.name AS name, i.description AS description, i.lot AS instrumentLot, ins.id AS id,\n" +
                "ins.instrument_series_qr_code  AS instrumentSeriesCode,  COUNT(name) as instrumentsCount \n" +
                sqlFromClause + sqlWhereClause + groupByClause;

        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentSeriesDto.class));
    }

    @Override
    public List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(String qrCode) {

        MapSqlParameterSource in = new MapSqlParameterSource();

        String sqlQuery = "Select i.name as name, i.lot AS instrumentLot, i.description AS description, ins.instrument_series_qr_code as qrCode, COUNT(name) as instrumentsCount \n" +
                "From instruments as i INNER JOIN instruments_series as ins ON i.instrument_series_id = ins.id \n" +
                "and ins.instrument_series_qr_code= :qrCode GROUP BY name, instrumentLot, description, qrCode  \n";
        in.addValue("qrCode", qrCode);
        return namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(InstrumentSeriesDto.class));
    }



    @Override
    public long createInstrumentSeries(InstrumentSeriesDto dto) {

        String sqlQueryOne = " INSERT INTO instruments_series (\n" +
                "instrument_series_qr_code\n" +
                ") VALUES (\n" +
                ":instrumentSeriesCode" +
                ")";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("instrumentSeriesCode", dto.getInstrumentSeriesCode());

                namedParameterJdbcTemplate.update(sqlQueryOne, in, keyHolder);

        if (dto.getConnectedInstrumentsIds().size() == 0) {
            return 1;
        }

        String sqlQueryTwo = " UPDATE instruments SET \n " +
                "instrument_series_id = :keyholder,\n " +
                "available = 0 \n " +
                "WHERE instruments.id in (:connectedInstrumentsIds)";

        MapSqlParameterSource in2 = new MapSqlParameterSource();
        in2.addValue("keyholder", keyHolder.getKey());
        in2.addValue("connectedInstrumentsIds", dto.getConnectedInstrumentsIds());

        namedParameterJdbcTemplate.update(sqlQueryTwo, in2);

        return 1;
    }

    @Override
    public boolean updateInstrumentSeries(InstrumentSeriesDto instrumentSeriesDto) {

        String sqlQueryOne = "";
        String sqlQueryTwo = "";
        String sqlQueryThree = "";
        String sqlQueryFour = "";

        if (instrumentSeriesDto.getConnectedInstrumentsIds().size() > 0 && instrumentSeriesDto.getUnconnectedInstrumentsIds().size() > 0) {

            sqlQueryOne = " UPDATE instruments SET \n " +
                    "instrument_series_id = null, \n " +
                    "available = 1 \n " +
                    "WHERE instruments.id in (:unconnectedInstrumentsIds)";

            MapSqlParameterSource in = new MapSqlParameterSource();
            in.addValue("unconnectedInstrumentsIds", instrumentSeriesDto.getUnconnectedInstrumentsIds());

            namedParameterJdbcTemplate.update(sqlQueryOne, in);

             sqlQueryTwo = " UPDATE instruments SET \n " +
                      "instrument_series_id = :id, \n " +
                    "available = 0 \n " +
                    "WHERE instruments.id in (:connectedInstrumentsIds)";

            MapSqlParameterSource in2 = new MapSqlParameterSource();
            in2.addValue("id", instrumentSeriesDto.getId());
            in2.addValue("connectedInstrumentsIds", instrumentSeriesDto.getConnectedInstrumentsIds());

            return namedParameterJdbcTemplate.update(sqlQueryTwo, in2) > 0;

        } else if (instrumentSeriesDto.getConnectedInstrumentsIds().size() == 0) {
            sqlQueryThree = " UPDATE instruments SET \n " +
                    "instrument_series_id = null, \n " +
                    "available = 1 \n " +
                    "WHERE instruments.instrument_series_id = :id";

            MapSqlParameterSource in = new MapSqlParameterSource();
            in.addValue("id", instrumentSeriesDto.getId());
            return namedParameterJdbcTemplate.update(sqlQueryThree, in) > 0;
        } else {
            sqlQueryFour = " UPDATE instruments SET \n " +
                    "instrument_series_id = :id, \n" +
                    "available = 0 \n " +
                    "WHERE instruments.instrument_series_id IS NULL";

            MapSqlParameterSource in = new MapSqlParameterSource();
            in.addValue("id", instrumentSeriesDto.getId());
            return namedParameterJdbcTemplate.update(sqlQueryFour, in) > 0;
        }
    }

    @Override
    public List<InstrumentDto> getInstrumentSeriesById(long id) {

        String sqlQuery = "SELECT i.id as id, i.name as name, i.description AS description, i.lot AS instrumentLot, ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "FROM instruments as i \n" +
                "INNER JOIN instruments_series as ins on i.instrument_series_id = ins.id AND ins.id = :id";

        MapSqlParameterSource in = new MapSqlParameterSource("id", id);

        List<InstrumentDto> instrumentSeries = namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(InstrumentDto.class));

        if(instrumentSeries.isEmpty()) {
            String sqlQueryTwo = "select instrument_series_qr_code as instrumentSeriesCode from instruments_series where id = :id";
            return namedParameterJdbcTemplate.query(sqlQueryTwo, in, new BeanPropertyRowMapper<>(InstrumentDto.class));
        }

        return instrumentSeries;
    }

    @Override
    public boolean deleteInstrumentSeries(Long id) {
        MapSqlParameterSource in = new MapSqlParameterSource("id", id);
        String sqlQuery = "";
        String sqlQueryTwo = "";

        sqlQuery = " UPDATE instruments SET \n " +
                "instrument_series_id = null, \n " +
                "available = 1 \n " +
                "WHERE instruments.instrument_series_id = :id";

        namedParameterJdbcTemplate.update(sqlQuery, in);

        sqlQueryTwo = " DELETE FROM instruments_series WHERE id = :id";

        return namedParameterJdbcTemplate.update(sqlQueryTwo, in) > 0;
    }
}
