package groupk.logistics.business;

public enum Area {
    south,north,center;

    public static Area castStringToArea(String area) {
        if(area.equals("center")) return Area.center;
        else if(area.equals("north")) return Area.north;
        else  if(area.equals("south")) return Area.south;
        else throw new IllegalArgumentException(area + " is invalid area");
    }

    public static String[] getAreasList() {
        return new String[]{"center", "north", "south"};
    }
}
