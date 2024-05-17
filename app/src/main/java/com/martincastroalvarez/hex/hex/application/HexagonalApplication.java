package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.logging.Logger;
import java.util.List;

public class HexagonalApplication {
    protected static final Logger logger = Logger.getLogger(ProductApplication.class.getName());

    protected PageRequest getPageRequest(Integer page, Integer size, String sort, Boolean asc, List<String> sortKeys) throws HexagonalPaginationException {
        logger.info(String.format("Getting page request. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        if (page == null || page < 0)
            throw new InvalidPageNumberException();
        if (size == null || size <= 0)
            throw new InvalidPageSizeException();
        if (sort == null)
            throw new InvalidSortKeyException();
        if (!sortKeys.contains(sort))
            throw new InvalidSortKeyException();
        Sort.Direction direction = Sort.Direction.ASC;
        if (asc != null && !asc)
            direction = Sort.Direction.DESC;
        Sort sortBy = Sort.by(new Sort.Order(direction, sort));
        return PageRequest.of(page, size, sortBy);
    }
}