package cli;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Formats double values consistently for command output.
 */
public class NumberFormatter {
    /**
     * Prevents construction because the class only contains static helpers.
     */
    private NumberFormatter() {
    }

    /**
     * Rounds for display, strips long tails, and keeps at least one decimal digit.
     */
    public static String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return String.valueOf(value);
        }
        double normalized = Math.abs(value) < 0.0000000001 ? 0.0 : value;
        BigDecimal decimal = BigDecimal.valueOf(normalized).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String text = decimal.toPlainString();
        return text.indexOf('.') >= 0 ? text : text + ".0";
    }
}
