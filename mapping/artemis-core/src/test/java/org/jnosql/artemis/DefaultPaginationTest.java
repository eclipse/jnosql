package org.jnosql.artemis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultPaginationTest {


    @Test
    public void shouldReturnErrorWhenPageIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(0));
    }

    @Test
    public void shouldReturnErrorWhenPageIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(-1));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(2).of(0));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(2).of(-1));
    }


    @Test
    public void shouldCreatePaginationInstance() {
        Pagination pagination = Pagination.page(1).of(2);
        assertEquals(1, pagination.getPageNumber());
        assertEquals(2L, pagination.getPageSize());
        assertEquals(2L, pagination.getLimit());
        assertEquals(0L, pagination.getSkip());
    }


    @Test
    public void shouldNext() {
        Pagination pagination = Pagination.page(1).of(2);

        checkPagination(pagination, 1, 0, 2, 2);
        Pagination secondPage = pagination.next();

        assertNotNull(secondPage);
        checkPagination(secondPage, 2, 2, 2, 2);

        Pagination thirdPage = secondPage.next();
        checkPagination(thirdPage, 3, 4, 2, 2);

        Pagination fourthPage = thirdPage.next();
        checkPagination(fourthPage, 4, 6, 2, 2);

        Pagination fifthPage = fourthPage.next();
        checkPagination(fifthPage, 5, 8, 2, 2);

    }

    private void checkPagination(Pagination pagination, long pageNumber, long skip, long limit, long size) {
        assertEquals(pageNumber, pagination.getPageNumber(), "The number page is wrong " + pagination);
        assertEquals(skip, pagination.getSkip(), "The skip is wrong " + pagination);
        assertEquals(limit, pagination.getLimit(), "The limit is wrong " + pagination);
        assertEquals(size, pagination.getPageSize(), "The page size is wrong " + pagination);
    }
}