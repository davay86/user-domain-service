package com.babcock.integration.asserter;

import com.noveria.assertion.asserter.WaitUntilAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.fail;

public class WaitForOneRowInDB extends WaitUntilAsserter {

    private static Logger logger = LoggerFactory.getLogger(WaitForOneRowInDB.class);

    private String query;
    private JdbcTemplate jdbcTemplate;

    public WaitForOneRowInDB(String query, JdbcTemplate restTemplate) {
        super();
        this.query = query;
        this.jdbcTemplate = restTemplate;
    }

    @Override
    protected boolean execute() {
        try {

            logger.info("waiting for db row from query {}",query);
            int count = jdbcTemplate.queryForObject(query, Integer.class);

            if (count == 1) {
                return true;
            } else if (count > 1) {
                fail("More than 1 row returned!");
                return false;
            } else {
                return false;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return false;
        }
    }

    @Override
    protected String getTaskName() {
        return "WaitForOneRowInDB";
    }

    @Override
    protected String getFailureMessage() {
        return "no rows found for : " + query;
    }
}
