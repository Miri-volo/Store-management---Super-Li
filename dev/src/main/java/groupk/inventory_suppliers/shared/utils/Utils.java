package groupk.inventory_suppliers.shared.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static String table(int columns, int columnWidth, boolean dashesUnderFirstLine, Object... args) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String cellValue = args[i].toString();
            if(args[i] instanceof Float) {
                cellValue = String.format("%.2f", args[i]);
            }
            sb.append("|").append(cellValue);

            int spaces = columnWidth - cellValue.length();
            for (int j = 0; j < spaces; j++) {
                sb.append(" ");
            }



            if((i+1) % columns == 0) {
                sb.append("|\n");

                if(i + 1 == columns && dashesUnderFirstLine) {
                    sb.append("|");
                    for (int j = 0; j < columns; j++) {
                        for(int k = 0; k < columnWidth; k++) {
                            sb.append("-");
                        }
                        sb.append("|");
                    }
                    sb.append("\n");
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
