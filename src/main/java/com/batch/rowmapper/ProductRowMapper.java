package com.batch.rowmapper;

import com.batch.domain.ProductV0;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductV0> {
    @Override
    public ProductV0 mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductV0.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .type(rs.getString("type"))
                .build();
    }
}
