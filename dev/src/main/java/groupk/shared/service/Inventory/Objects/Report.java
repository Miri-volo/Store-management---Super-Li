package groupk.shared.service.Inventory.Objects;

import java.time.LocalDate;

public class Report {

    Integer id;
    String name;
    LocalDate date;
    String report_producer;
    groupk.shared.business.Inventory.Report.report_type reportType;


    public Report(groupk.shared.business.Inventory.Report report) {
        id = report.getId();
        name = report.getName();
        date = report.getDate();
        report_producer = report.getReport_producer();
        reportType = report.getReportType();
    }

    public String getName() {
        return name;
    }


    public Integer getId() {
        return id;
    }
}
