package org.example.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PageRequestDTOTest {
    @Autowired
    Validator validator;

    /**
     * Test success page request validation
     */
    @Test
    void testValidPageRequest() {
        PageRequestDTO pageRequest = PageRequestDTO.builder()
                .page(0)
                .pageSize(5)
                .build();
        Set<ConstraintViolation<PageRequestDTO>> errors = validator.validate(pageRequest);
        assertThat(errors).isEmpty();
    }

    /**
     * Test failed page request validation
     */
    @Test
    void testInvalidPageRequest() {
        PageRequestDTO pageRequest = PageRequestDTO.builder()
                .page(-1)
                .pageSize(0)
                .build();
        Set<ConstraintViolation<PageRequestDTO>> errors = validator.validate(pageRequest);
        assertThat(errors).hasSize(2);
    }
}
