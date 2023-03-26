package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

import java.sql.Date;

/**
 * "CREATE TABLE IF NOT EXISTS ProductReport
 * id INTEGER NOT NULL,
 * reportProducer TEXT NOT NULL,
 * name TEXT NOT NULL,
 * date DATETIME NOT NULL,
 * type INTEGER NOT NULL,
 * query TEXT NOT NULL,
 * PRIMARY KEY( id )
 * <p>
 * "CREATE TABLE IF NOT EXISTS ItemReport
 * id INTEGER NOT NULL,
 * reportProducer TEXT NOT NULL,
 * name TEXT NOT NULL,
 * date DATETIME NOT NULL,
 * type INTEGER NOT NULL,
 * query TEXT NOT NULL,
 * "PRIMARY KEY( id )
 *
 */
public interface ReportData {
    int getId();
    String getReportProducer();
    String getName();
    Date getDate();
    int getType();
    String getQuery();
}
