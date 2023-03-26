package groupk.logistics.business;

public enum DLicense {
    B,C,C1,C_E;

    public static DLicense castStringToDlLicense(String dlicense) {
        if(dlicense.equals("B")) return DLicense.B;
        else if(dlicense.equals("C")) return DLicense.C;
        else if (dlicense.equals("C1")) return DLicense.C1;
        else if (dlicense.equals("C_E")) return DLicense.C_E;
        else throw new IllegalArgumentException("The driver's license is illegal");
    }

    public static String[] getDLicenseList() {
        return new String[]{"B","C","C1","C_E"};
    }
}
