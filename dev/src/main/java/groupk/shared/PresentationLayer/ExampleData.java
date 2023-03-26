package groupk.shared.PresentationLayer;

import groupk.logistics.business.DLicense;
import groupk.shared.business.Facade;
import groupk.shared.service.Inventory.Objects.Product;
import groupk.shared.service.Inventory.Objects.ProductItem;
import groupk.shared.business.Suppliers.BussinessObject.Item;
import groupk.inventory_suppliers.dataLayer.dao.records.OrderType;
import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;
import groupk.shared.service.Service;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;
import groupk.shared.service.dto.Site;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class ExampleData {
    public static void loadExampleData(Service service
    ) {
        int ppn_office = service.createSupplier(1, 1, "OfficeLi",
                true, PaymentCondition.Credit, DayOfWeek.SUNDAY,
                "Ofir", "054-1234567", "Pencil 12, Pencilvannia").data.getPpn();
        int ppn_foods = service.createSupplier(2, 2, "FoodsAreUs",
                true, PaymentCondition.DirectDebit, DayOfWeek.THURSDAY,
                "Frank", "050-1234569", "Vegetable 12, Beersheba").data.getPpn();
        int ppn_lorem = service.createSupplier(3, 3, "Lorem Stuff",
                true, PaymentCondition.Credit, DayOfWeek.WEDNESDAY,
                "Lauren", "052-7654321", "Lorem 14, Ipsumia").data.getPpn();

        service.addCategory("office");
        service.addSubCategory("office", "writing");
        service.addSubSubCategory("office", "writing", "pens");
        service.addCategory("food");
        service.addSubCategory("food", "dairy");
        service.addSubSubCategory("food", "dairy", "10%");
        service.addSubSubCategory("food", "dairy", "5%");
        service.addSubCategory("food", "bread");
        service.addSubSubCategory("food", "bread", "rye");
        service.addSubSubCategory("food", "bread", "wheat");

        // Michael, fill some reasonable fields here
        Product pen = service.addProduct(
                "GelGrip Pen, 1.0mm, Blue",
                null,
                0,
                12,
                10,
                10, // Michael, fill some reasonable fields here
                "office",
                "writing",
                "pens"
        ).data;
        Product cheaperPen = service.addProduct(
                "GelGrip Pen, 1.0mm, Blue",
                null,
                0,
                6,
                10,
                10, // Michael, fill some reasonable fields here
                "office",
                "writing",
                "pens"
        ).data;
        Product milk5 = service.addProduct(
                "Mister milk",
                null,
                0,
                40,
                3,
                5,
                "food",
                "dairy",
                "5%"
        ).data; // same
        Product milk10 = service.addProduct(
                "Miss milk",
                null,
                0,
                40,
                3,
                5,
                "food",
                "dairy",
                "10%"
        ).data; // same

        ProductItem milk5_0 = service.addItem(
                milk5.getProduct_id(),
                "Yavne",
                "shelf 1",
                2,
                LocalDate.of(2022, 12, 29),
                true
        ).data;

        ProductItem milk5_1 = service.addItem(
                milk5.getProduct_id(),
                "Yehud",
                "shelf 1",
                2,
                LocalDate.of(2022, 12, 28),
                true
        ).data;

        ProductItem milk5_2 = service.addItem(
                milk5.getProduct_id(),
                "Yeruham",
                "shelf 1",
                2,
                LocalDate.of(2022, 12, 27),
                true
        ).data;

        ProductItem milk5_3 = service.addItem(
                milk5.getProduct_id(),
                "Yad Ha-Shmonah",
                "shelf 1",
                2,
                LocalDate.of(2022, 12, 26),
                true
        ).data;

        ProductItem milk5_4 = service.addItem(
                milk5.getProduct_id(),
                "Yad Binyamin",
                "shelf 1",
                2,
                LocalDate.of(2022, 12, 25),
                true
        ).data;

        service.updateProductCusDiscount(0.5f, LocalDate.of(2022, 3, 10), LocalDate.of(2022, 3, 20), milk5.getProduct_id());

        // Office items
        Item penItem = service.createItem(
                ppn_office,
                10,
                pen.getProduct_id(),
                11
        ).data;

        Item cheaperPenItem = service.createItem(
                ppn_lorem,
                10,
                cheaperPen.getProduct_id(),
                5
        ).data;

        // Food items
        Item milk5FromFoodsAreUs = service.createItem(
                ppn_foods,
                2,
                milk5.getProduct_id(),
                4
        ).data;
        Item milk10FromFoodsAreUs = service.createItem(
                ppn_foods,
                3,
                milk10.getProduct_id(),
                5
        ).data;
        Item milk5FromLorem = service.createItem(
                ppn_lorem,
                3,
                milk5.getProduct_id(),
                4
        ).data;
        Item milk10FromLorem = service.createItem(
                ppn_lorem,
                5,
                milk10.getProduct_id(),
                5.5f
        ).data;

        int[] discountAmounts = {10, 50, 100, 200};
        float[] discountPercents = {0.01f, 0.05f, 0.1f, 0.25f};

        for (int i = 0; i < discountAmounts.length; i++) {
            service.createDiscount(ppn_office, penItem.getCatalogNumber(),
                    discountAmounts[i], discountPercents[i]);
        }

        LocalDate date_jan_1 = LocalDate.of(2022, 1, 1);
        LocalDate date_jan_2 = LocalDate.of(2022, 1, 2);
        LocalDate date_feb_1 = LocalDate.of(2022, 2, 1);
        LocalDate date_feb_2 = LocalDate.of(2022, 2, 2);

        int order1 = service.createOrder(ppn_office, date_jan_1, date_jan_2, OrderType.Periodical).data.getId();
        int order2 = service.createOrder(ppn_foods, date_jan_1, date_jan_2, OrderType.Shortages).data.getId();
        int order3 = service.createOrder(ppn_lorem, date_jan_1, date_jan_2, OrderType.Shortages).data.getId();

        // order things
        service.orderItem(order1, ppn_office, penItem.getCatalogNumber(), 10);
        service.orderItem(order2, ppn_foods, milk5FromFoodsAreUs.getCatalogNumber(), 5);
        service.orderItem(order3, ppn_lorem, cheaperPenItem.getCatalogNumber(), 5);
        // fill other tables
        service.createByCategoryReport("my first report", "Naziff", "", "", "");
        service.createBySupplierReport("my second report", "Shariff", 2);

        try {
            Set<Employee.ShiftDateTime> all = new LinkedHashSet<Employee.ShiftDateTime>();
            for (Employee.ShiftDateTime s : Employee.ShiftDateTime.values())
                all.add(s);
            HashMap<Employee.Role, Integer> r1 = new HashMap<>();
            for (Employee.Role role : Employee.Role.values())
                r1.put(role, 0);
            r1.replace(Employee.Role.ShiftManager, 1);
            Site source = new Site("tamirHouse", "054-3397995", "center", "batYam", "tamirStr", 13, 2, 3);
            Site destination = new Site("idoHouse", "052-4321231", "center", "herzliya", "idoStr", 100, 1, 6);
            List<Site> sources = new LinkedList<>();
            List<Site> destinations = new LinkedList<>();
            sources.add(source);
            destinations.add(destination);
            List<Integer> orders = new LinkedList<>();
            orders.add(1);

            Site source2 = new Site("miri", "052-2226668", "north", "haifa", "miriSTR", 13, 2, 3);
            Site destination2 = new Site("lior", "053-6545648", "south", "beersheva", "liorSTR", 100, 1, 6);
            List<Site> sources2 = new LinkedList<>();
            List<Site> destinations2 = new LinkedList<>();
            sources2.add(source2);
            destinations2.add(destination2);
            List<Integer> orders2 = new LinkedList<>();
            orders.add(2);

            // HR.
            Employee HR1 = service.createEmployee(
                    "Avi",
                    "111",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.HumanResources,
                    all).getValue();
            Employee HR2 = service.createEmployee(
                    "Eli",
                    "112",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.HumanResources,
                    all).getValue();
            Employee HR3 = service.createEmployee(
                    "Dana",
                    "113",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.HumanResources,
                    all).getValue();
            Employee HR4 = service.createEmployee(
                    "Noa",
                    "114",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.HumanResources,
                    all).getValue();

            // Cashiers.
            Employee C1 = service.createEmployee(
                    "Eli",
                    "212",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Cashier,
                    all).getValue();
            Employee C2 = service.createEmployee(
                    "Noa",
                    "214",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Cashier,
                    all).getValue();

            // Drivers:
            Employee D1 = service.createEmployee(
                    "Avi",
                    "311",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Driver,
                    all).getValue();
            Employee D2 = service.createEmployee(
                    "Dana",
                    "313",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Driver,
                    all).getValue();

            // Logistics:
            Employee L1 = service.createEmployee(
                    "Eli",
                    "412",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Logistics,
                    all).getValue();
            Employee L2 = service.createEmployee(
                    "Noa",
                    "414",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Logistics,
                    all).getValue();

            // Logistics Managers:
            Employee LM1 = service.createEmployee(
                    "Eli",
                    "512",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.LogisticsManager,
                    all).getValue();
            Employee LM2 = service.createEmployee(
                    "Noa",
                    "514",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.LogisticsManager,
                    all).getValue();

            // Shift Managers:
            Employee SM1 = service.createEmployee(
                    "Eli",
                    "612",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.ShiftManager,
                    all).getValue();
            Employee SM2 = service.createEmployee(
                    "Noa",
                    "614",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.ShiftManager,
                    all).getValue();

            // Stockers:
            Employee S1 = service.createEmployee(
                    "Avi",
                    "711",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Stocker,
                    all).getValue();
            Employee S2 = service.createEmployee(
                    "Dana",
                    "713",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.Stocker,
                    all).getValue();

            // Store Managers:
            Employee StoreM1 = service.createEmployee(
                    "Avi",
                    "811",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.StoreManager,
                    all).getValue();
            Employee StoreM2 = service.createEmployee(
                    "Dana",
                    "813",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.StoreManager,
                    all).getValue();

            // Trucking Manger:
            Employee TM1 = service.createEmployee(
                    "Avi",
                    "911",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.LogisticsManager,
                    all).getValue();
            Employee TM2 = service.createEmployee(
                    "Dana",
                    "913",
                    "Example",
                    1,
                    1, new GregorianCalendar(),
                    30,
                    0,
                    0,
                    Employee.Role.LogisticsManager,
                    all).getValue();

            //staff
            LinkedList<Employee> staff = new LinkedList<>();
            staff.add(SM1);
            //create shifts
            Shift shift1 = service.createShift(
                    HR1.id,
                    new GregorianCalendar(2023, Calendar.APRIL, 21),
                    Shift.Type.Evening,
                    staff,
                    r1).getValue();

            Shift shift2 = service.createShift(
                    HR1.id,
                    new GregorianCalendar(2023, Calendar.APRIL, 23),
                    Shift.Type.Morning,
                    staff,
                    r1).getValue();
            service.addEmployeeToShift(HR1.id, shift1.getDate(), shift1.getType(), D1.id);
            service.addEmployeeToShift(HR1.id, shift2.getDate(), shift2.getType(), D2.id);
            service.addEmployeeToShift(HR1.id, shift1.getDate(), shift1.getType(), L1.id);
            service.addEmployeeToShift(HR1.id, shift2.getDate(), shift2.getType(), L2.id);

            String registrainPlate1 = "12315678";
            String registrainPlate2 = "12345678";
            //add vehicles
            service.createVehicle(
                    TM1.id,
                    "B",
                    registrainPlate1,
                    "mercedes",
                    4,
                    32);

            service.createVehicle(
                    TM1.id,
                    "C",
                    registrainPlate2,
                    "volvo",
                    8,
                    22);


            //add licences
            service.addLicenseForDriver(
                    D1.id,
                    DLicense.B.name());


            service.addLicenseForDriver(
                    D2.id,
                    DLicense.C.name());

            //add Truckings
            service.createDelivery(
                    TM1.id,
                    registrainPlate2,
                    LocalDateTime.of(2023, Month.APRIL, 23, 9, 0),
                    D2.id,
                    sources,
                    destinations,
                    orders,
                    2,
                    0);


            service.createDelivery(
                    TM1.id,
                    registrainPlate1,
                    LocalDateTime.of(2023, Month.APRIL, 21, 18, 0),
                    D1.id,
                    sources2,
                    destinations2,
                    orders2,
                    1,
                    0);
        }
        catch (Exception e){
            System.out.println("Already Loaded");
        }
    }
}
