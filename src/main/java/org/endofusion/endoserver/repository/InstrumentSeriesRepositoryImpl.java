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

        String sqlQuery = "select ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "from instruments_series as ins \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentDto> fetchAvailableInstruments() {

        String sqlQuery = "select i.id as id, i.name as name, i.description AS description, COUNT(name) as instrumentsCount, GROUP_CONCAT(`id`) as instrumentIdsList \n" +
                "from instruments as i WHERE i.available = 1 GROUP BY name, description \n";
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

        String sqlQuery = "Select i.name as name, i.lot AS instrumentLot, i.description AS description, ins.instrument_series_qr_code as qrCode, COUNT(name) as instrumentsCount \n" +
                "From instruments as i INNER JOIN instruments_series as ins ON i.instrument_series_id = ins.id \n" +
                "and ins.instrument_series_qr_code= :qrCode GROUP BY name, description \n";
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

        String sqlQueryTwo = " UPDATE instruments SET \n " +
                "instrument_series_id = :keyholder,\n " +
                "available = 0 \n " +
                "WHERE instruments.id in (:instrumentIdsList)";

        MapSqlParameterSource in2 = new MapSqlParameterSource();
        in2.addValue("keyholder", keyHolder.getKey());
        in2.addValue("instrumentIdsList", dto.getInstrumentIdsList());

        namedParameterJdbcTemplate.update(sqlQueryTwo, in2);

        return 1;
    }
}
