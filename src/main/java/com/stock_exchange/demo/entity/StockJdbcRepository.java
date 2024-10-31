package com.stock_exchange.demo.entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Stock> findAll() {
         return jdbcTemplate.query("SELECT * FROM stock", new StockRowMapper());
    }

    public void save(Stock stock) {
        String sql = "INSERT INTO stock (company_name, total_shares) VALUES (?, ?)";
        jdbcTemplate.update(sql, stock.getCompanyName(), stock.getTotalShares());
    }

    private static class StockRowMapper implements RowMapper<Stock> {
        @Override
        public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Stock(rs.getString("company_name"), rs.getInt("total_shares"));
        }
    }
}
