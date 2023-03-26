package groupk.inventory_and_suppliers;

import groupk.inventory_suppliers.SchemaInit;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.shared.PresentationLayer.App;
import groupk.shared.service.Service;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

public class InventorySuppliersTestsBase {
    protected PersistenceController pc;
    protected Connection conn;
    protected Service service;

    @BeforeEach
    public void setService() {
        App app = new App(":memory:", true);
        this.conn = app.conn;
        SchemaInit.init(this.conn);
        this.pc = app.dal;
        this.service = app.service;
    }
}
