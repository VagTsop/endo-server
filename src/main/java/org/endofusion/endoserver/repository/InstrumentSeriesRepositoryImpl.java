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
public class InstrumentSeriesRepositoryImpl implements InstrumentSeriesRepository {//

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {

        String sqlQuery = "select ins.instrument_series_qr_code as instrumentSeriesCode \n" +
                "from instruments_series as ins \n";
        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentDto.class));
    }

    @Override
    public List<InstrumentSeriesDto> getInstrumentSeriesList() {

        String sqlFromClause = "From instruments as i\n" +
                " INNER JOIN instruments_series AS ins ON i.instrument_series_id = ins.instrument_series_id \n";

        String sqlWhereClause = "WHERE i.instrument_id is not null\n";

        String groupByClause = "GROUP BY instrumentSeriesCode, instrumentName, instrumentDescription  order by instrumentSeriesCode \n";

        MapSqlParameterSource in = new MapSqlParameterSource();

        String sqlQuery = "Select i.name AS instrumentName, i.description AS instrumentDescription, ins.instrument_series_id AS instrumentSeriesId,\n" +
                "ins.instrument_series_qr_code  AS instrumentSeriesCode,  COUNT(name) as instrumentsCount \n" +
                sqlFromClause + sqlWhereClause + groupByClause;

        return namedParameterJdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(InstrumentSeriesDto.class));
    }
}
