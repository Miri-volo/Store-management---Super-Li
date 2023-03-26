package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ReportData;

import java.sql.Date;

public class ReportRecord extends BaseRecord<Integer> implements ReportData {
    private int id;
    private String report_producer;
    private String name;
    private Date date;
    private int type;
    private String query;

    public ReportRecord(int id, String report_producer, String name, Date date, int type, String query) {
        this.id = id;
        this.report_producer = report_producer;
        this.name = name;
        this.date = date;
        this.type = type;
        this.query = query;
    }

    @Override
    public Integer key() {
        return id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getReportProducer() {
        return report_producer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
