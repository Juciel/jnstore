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
            for (String sortParam : sortParams) {
                String[] parts = sortParam.split(",");
                if (parts.length == 2) {
                    Sort.Direction direction = Sort.Direction.fromString(parts[1].trim().toUpperCase());
                    orders.add(new Sort.Order(direction, parts[0].trim()));
                } else if (parts.length == 1) {
                    // Default to ascending if direction is not specified
                    orders.add(new Sort.Order(Sort.Direction.ASC, parts[0].trim()));
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
