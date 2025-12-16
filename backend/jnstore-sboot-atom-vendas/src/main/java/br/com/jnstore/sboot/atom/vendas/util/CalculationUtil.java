package br.com.jnstore.sboot.atom.vendas.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtil {

    /**
     * Calcula a variação percentual entre o valor atual e o valor anterior.
     *
     * @param currentValue  O valor atual.
     * @param previousValue O valor do período anterior.
     * @return A variação percentual, ou null se o valor anterior for zero e o atual também for zero.
     */
    public static BigDecimal calculateVariation(BigDecimal currentValue, BigDecimal previousValue) {
        if (previousValue == null || previousValue.compareTo(BigDecimal.ZERO) == 0) {
            if (currentValue == null || currentValue.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO; // Ambos zero, variação zero
            } else {
                return BigDecimal.valueOf(100); // Aumento infinito (de zero para um valor positivo)
            }
        }

        if (currentValue == null) {
            currentValue = BigDecimal.ZERO;
        }

        BigDecimal difference = currentValue.subtract(previousValue);
        return difference.divide(previousValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }
}
