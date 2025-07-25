package br.gov.ce.arce.spgc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.With;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Base page response
 *
 * @param totalElements         total of elements from all search
 * @param totalElementsThisPage total of elements from this page
 * @param pageNumber            page number (page index + 1)
 * @param pageIndex             page index
 * @param totalPages            total os pages from the search
 * @param isFirst               is first page
 * @param isLast                is last page
 * @param hasNext               has next page
 * @param hasPrevious           has previous page
 * @param data                  information
 * @param <T>                   generic type representing data information
 * @author Eduarda
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@With
public record BasePageResponse<T>(
        long totalElements,
        long totalElementsThisPage,
        int pageNumber,
        int pageIndex,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious,
        List<T> data
) {

    /**
     * Create a {@link BasePageResponse} by {@link Page}
     *
     * @param result the {@link Page} with the data information
     * @param <T>    generic type representing the data in the result
     * @return BasePageResponse with all information
     * @author Eduarda
     */
    public static <T> BasePageResponse<T> createBySpringPage(Page<T> result) {
        return new BasePageResponse<>(
                result.getTotalElements(),
                result.getSize(),
                result.getNumber() + 1, // for user page number starts from 1
                result.getNumber(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast(),
                result.hasNext(),
                result.hasPrevious(),
                result.getContent()
        );
    }

}
