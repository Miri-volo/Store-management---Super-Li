package groupk.shared.service.Inventory.Objects;

public class SubSubCategory {
    String name;

    public SubSubCategory(groupk.shared.business.Inventory.Categories.SubSubCategory subSubCategory) {
        name= subSubCategory.getName();
    }
}
