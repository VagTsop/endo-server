package org.endofusion.endoserver.repository;


import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("InstrumentSeriesRepository")
public class InstrumentSeriesRepositoryImpl implements InstrumentSeriesRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {

        String sqlQuery = "select ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "from instruments_series as ins \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentDto> fetchAvailableInstruments() {

        String sqlQuery = "select i.name as name, COUNT(name) as instrumentsCount \n" +
                "from instruments as i GROUP BY name \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentSeriesDto> getInstrumentSeriesList() {

        String sqlFromClause = "From instruments as i\n" +
                " INNER JOIN instruments_series AS ins ON i.instrument_series_id = ins.id \n";

        String sqlWhereClause = "WHERE i.id is not null\n";

        String groupByClause = "GROUP BY instrumentSeriesCode, name, description  order by instrumentSeriesCode \n";

        String sqlQuery = "Select i.name AS name, i.description AS description, ins.id AS id,\n" +
                "ins.instrument_series_qr_code  AS instrumentSeriesCode,  COUNT(name) as instrumentsCount \n" +
                sqlFromClause + sqlWhereClause + groupByClause;

        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentSeriesDto.class));
    }

    @Override
    public List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(long qrCode) {

        MapSqlParameterSource in = new MapSqlParameterSource();

        String sqlQuery = "Select i.name as name, i.lot AS instrumentLot, i.description AS description, COUNT(name) as instrumentsCount \n" +
                "From instruments as i \n" +
                "where i.instrument_series_id = :qrCode GROUP BY name, description \n";
        in.addValue("qrCode", qrCode);
        return namedParameterJdbcTemplate.query(sqlQuery, in, new BeanPropertyRowMapper<>(InstrumentSeriesDto.class));
    }
}
