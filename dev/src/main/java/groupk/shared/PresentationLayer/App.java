package groupk.shared.PresentationLayer;

import groupk.logistics.DataLayer.myDataBase;
import groupk.shared.PresentationLayer.EmployeesLogistics.MainEmployeesAndDelivery;
import groupk.shared.PresentationLayer.Inventory.InventoryPresentationFacade;
import groupk.shared.PresentationLayer.Suppliers.SupplierPresentationFacade;
import groupk.shared.PresentationLayer.Suppliers.UserInput;
import groupk.shared.PresentationLayer.Suppliers.UserOutput;
import groupk.inventory_suppliers.SchemaInit;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.shared.service.Response;
import groupk.shared.service.Service;
import groupk.shared.service.dto.Employee;
import groupk.workers.data.DalController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class App {
    public final PersistenceController dal;
    public final SupplierPresentationFacade supplierPresentation;
    public final InventoryPresentationFacade inventoryPresentationFacade;
    public final Connection conn;
    public final Service service;
    private Employee subject;

    public App(String dbPath, boolean tests) {
        boolean isNew = !new java.io.File(dbPath).exists();
        boolean shouldLoadExample = false;
        conn = connect(dbPath);
        DalController employeeDalController = new DalController(conn);
        myDataBase logisitcsDalController = new myDataBase(conn);
        SchemaInit.init(conn);
        if (isNew && !tests) {
            UserOutput.getInstance().println(
                    "You don't have a previous database file stored, so we'll create a new one for you " +
                            "from scratch.");
            shouldLoadExample = UserInput.getInstance().nextBoolean("Would you like to start with some example data?");
        }
        dal =  new PersistenceController(conn);
        service = new Service(dal, employeeDalController, logisitcsDalController);
        inventoryPresentationFacade = new InventoryPresentationFacade(service);
        supplierPresentation = new SupplierPresentationFacade(service);
        if(!tests)
            service.loadEmployeeDB();
        if (shouldLoadExample) {
            ExampleData.loadExampleData(service);
        }
    }

    public void main() {
        int in = 0;
        while (true) {
            in = UserInput.getInstance().nextInt("Which module do you need?\n" +
                    "1. Supplier module\n" +
                    "2. Inventory module\n" +
                    "3. Employees and Logistics modules\n" +
                    "4. Login or change user\n" +
                    "5. Exit\n");
            switch (in) {
                case (1): {
                    supplierPresentation.startSupplierMenu(subject);
                    break;
                }
                case (2): {
                    startInventoryMenu(subject);
                    break;
                }
                case (3) : {
                    MainEmployeesAndDelivery.mainEmployeesAndDelivery(service, conn, subject);
                    break;
                }
                case (4): {
                    String id = UserInput.getInstance().nextString("What is your ID?\n");
                    Response<Employee> subjectResponse = service.readEmployee(id, id);
                    if (subjectResponse.isError()) {
                        System.out.println("Error: Must be valid employee ID.");
                        break;
                    }
                    this.subject = subjectResponse.getValue();
                    break;

                }
                case (5): {
                    UserOutput.getInstance().println("Goodbye!");
                    return;
                }
                default:
                    UserOutput.getInstance().println("Please try again.");
            }

        }
    }


    private void startInventoryMenu(Employee currentUser) {
        Scanner scan = new Scanner(System.in);
        String input = "";
        do {
            input = scan.nextLine();
            inventoryPresentationFacade.execute(input, currentUser);
        }
        while (!input.equals("exit"));
        System.out.println("thank you");
    }


    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }


    private Connection connect(String dbPath) {
        try {
            return DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

}
