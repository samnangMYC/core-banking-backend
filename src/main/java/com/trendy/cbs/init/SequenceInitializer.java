package com.trendy.cbs.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SequenceInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        String sql = """
            CREATE SEQUENCE IF NOT EXISTS ledger_txn_seq
            START WITH 1000
            INCREMENT BY 1
            NO CYCLE;
            """;

        jdbcTemplate.execute(sql);
        System.out.println("Sequence ledger_txn_seq initialized (if not exists)");
    }
}
