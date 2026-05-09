package com.appointmentsystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AppointmentSchemaInitializer {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentSchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void ensureSubjectColumnExists() {
        if (!columnExists("appointments", "subject")) {
            jdbcTemplate.execute("ALTER TABLE appointments ADD COLUMN subject TEXT NOT NULL DEFAULT ''");
        }
        if (!columnExists("availability_slots", "subject")) {
            jdbcTemplate.execute("ALTER TABLE availability_slots ADD COLUMN subject TEXT NOT NULL DEFAULT ''");
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        List<Map<String, Object>> columns = jdbcTemplate.queryForList("PRAGMA table_info(" + tableName + ")");
        for (Map<String, Object> column : columns) {
            Object name = column.get("name");
            if (columnName.equals(name)) {
                return true;
            }
        }
        return false;
    }
}