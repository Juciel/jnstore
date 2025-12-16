package br.com.jnstore.sboot.atom.vendas.util;

import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class DateCalculationUtil {

    /**
     * Retorna o par de LocalDateTime (data inicial, data final) para o período especificado,
     * com um offset para calcular períodos anteriores.
     *
     * @param periodo String representando o período (ex: "HOJE", "SEMANA", "MES", "ANO").
     * @param offset  Offset para o período (0 para o período atual, -1 para o anterior, etc.).
     * @return Pair de LocalDateTime com a data inicial e final do período.
     */
    public static Pair<LocalDateTime, LocalDateTime> getPeriodDates(String periodo, int offset) {
        LocalDate baseDate = LocalDate.now();

        // Ajusta a data base de acordo com o offset
        switch (periodo.toUpperCase()) {
            case "HOJE":
                baseDate = baseDate.plusDays(offset);
                break;
            case "SEMANA":
                baseDate = baseDate.plusWeeks(offset);
                break;
            case "MES":
                baseDate = baseDate.plusMonths(offset);
                break;
            case "ANO":
                baseDate = baseDate.plusYears(offset);
                break;
            default:
                baseDate = baseDate.plusDays(offset); // Padrão para "HOJE" se não reconhecido
                break;
        }

        LocalDateTime startDate;
        LocalDateTime endDate;

        if (!StringUtils.hasText(periodo)) {
            startDate = LocalDateTime.of(baseDate, LocalTime.MIN);
            endDate = LocalDateTime.of(baseDate, LocalTime.MAX);
        } else {
            switch (periodo.toUpperCase()) {
                case "HOJE":
                    startDate = LocalDateTime.of(baseDate, LocalTime.MIN);
                    endDate = LocalDateTime.of(baseDate, LocalTime.MAX);
                    break;
                case "SEMANA":
                    startDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)), LocalTime.MIN);
                    endDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)), LocalTime.MAX);
                    break;
                case "MES":
                    startDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
                    endDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
                    break;
                case "ANO":
                    startDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
                    endDate = LocalDateTime.of(baseDate.with(TemporalAdjusters.lastDayOfYear()), LocalTime.MAX);
                    break;
                default:
                    startDate = LocalDateTime.of(baseDate, LocalTime.MIN);
                    endDate = LocalDateTime.of(baseDate, LocalTime.MAX);
                    break;
            }
        }
        return Pair.of(startDate, endDate);
    }
}
