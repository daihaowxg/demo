package com.crhms.cdmp.ds.manager;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author wxg
 * @since 2025/2/5
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
