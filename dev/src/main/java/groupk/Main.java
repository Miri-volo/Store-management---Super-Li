package groupk;

import groupk.shared.PresentationLayer.App;
public class Main {
    public static void main(String[] args) {System.out.println("Welcome!");
        App app = new App("database.db", false);
        app.main();
    }
}
