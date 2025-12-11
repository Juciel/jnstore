package br.com.jnstore.sboot.atom.vendas.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    private PaginationUtil() {
        // Utility class
    }

    public static Sort createSortFromStrings(List<String> sortParams) {
        Sort sortObject = Sort.unsorted();
        if (sortParams != null && !sortParams.isEmpty()) {
            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < sortParams.size(); i++) {
                String sortParam = sortParams.get(i).trim();
                String[] parts = sortParam.split(",");

                if (parts.length == 2) {
                    // Caso padrão: "propriedade,direcao" (ex: "id,desc")
                    Sort.Direction direction = Sort.Direction.fromString(parts[1].trim().toUpperCase());
                    orders.add(new Sort.Order(direction, parts[0].trim()));
                } else if (parts.length == 1) {
                    // Caso: "propriedade" ou "direcao" (se o Spring dividiu "propriedade,direcao" em dois itens)
                    String property = parts[0].trim();
                    Sort.Direction direction = Sort.Direction.ASC; // Direção padrão

                    // Verifica se o próximo parâmetro é uma direção
                    if (i + 1 < sortParams.size()) {
                        String nextParam = sortParams.get(i + 1).trim();
                        if (nextParam.equalsIgnoreCase("asc") || nextParam.equalsIgnoreCase("desc")) {
                            direction = Sort.Direction.fromString(nextParam.toUpperCase());
                            i++; // Consome o próximo parâmetro, pois ele é a direção do atual
                        }
                    }
                    // Adiciona a ordem, garantindo que não estamos adicionando "asc" ou "desc" como nome de propriedade
                    if (!property.equalsIgnoreCase("asc") && !property.equalsIgnoreCase("desc")) {
                        orders.add(new Sort.Order(direction, property));
                    }
                }
            }
            if (!orders.isEmpty()) {
                sortObject = Sort.by(orders);
            }
        }
        return sortObject;
    }

    public static Pageable applyDefaultSortIfUnsorted(Pageable pageable, String defaultSortProperty) {
        if (pageable.getSort().isUnsorted()) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, defaultSortProperty));
        }
        return pageable;
    }
}
