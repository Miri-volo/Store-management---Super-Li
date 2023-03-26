package groupk.shared.service.Inventory.Objects;

import java.util.List;

public class ProductItemReport extends Report{

    private List<ProductItem> productItemList;

    public ProductItemReport(groupk.shared.business.Inventory.Report report) {
        super(report);
        List<groupk.shared.business.Inventory.ProductItem> BusinessProductItemList=report.getProductItemList();
        for (groupk.shared.business.Inventory.ProductItem p:BusinessProductItemList) {
            productItemList.add(new groupk.shared.service.Inventory.Objects.ProductItem(p));
        }
    }

    public String toString(){
        String s= "Id: "+id+"\n"+ "name: "+name+"\n"+ "date: "+date+"\n"+ "report_producer: "+report_producer+"\n";
        switch (reportType){
            case byProduct:
                return s + "The Items ids are" + productItemList.get(0).getProductName() + "product:" + "\n" + productItemList();
            case bySupplier:
                return s+ "The Items that "+ productItemList.get(0).getSupplier()+" is provides are:"+"\n" + productItemList();
            case Defective:
                return s+"The defective products are:"+"\n" + productItemList();
            case Expired:
                return s+ "The expired products items are:\n"+ productItemList();
        }
        return null;
    }
    private String productItemList(){
        String s="";
        for (ProductItem p:productItemList) {
            s=s+p.getId()+"\n";
        }
        return s;
    }
}
