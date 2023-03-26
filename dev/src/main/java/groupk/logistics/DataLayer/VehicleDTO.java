package groupk.logistics.DataLayer;

public class VehicleDTO {
    private String lisence;
    private String registationPlate;
    private String model;
    private int weight;
    private int maxWeight;

    public VehicleDTO(String lisence, String registationPlate, String model, int weight, int maxWeight) {
        this.lisence = lisence;
        this.registationPlate = registationPlate;
        this.maxWeight = maxWeight;
        this.weight = weight;
        this.model = model;
    }

    public String getLisence() {
        return lisence;
    }

    public String getRegistationPlate() {
        return registationPlate;
    }

    public String getModel() {
        return model;
    }

    public int getWeight() {
        return weight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setLisence(String lisence) {
        this.lisence = lisence;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRegistationPlate(String registationPlate) {
        this.registationPlate = registationPlate;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

