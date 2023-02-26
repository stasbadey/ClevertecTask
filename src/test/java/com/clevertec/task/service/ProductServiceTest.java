package com.clevertec.task.service;

import com.clevertec.task.exception.NotFoundException;
import com.clevertec.task.model.Product;
import com.clevertec.task.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    public void getAllWorksFine() {
        // given
        when(productRepository.findAll()).thenReturn(productList());

        // when
        List<Product> result = productService.getAll();

        // then
        assertEquals(result.size(), productList().size());
    }

    @Test
    public void getAllReturnsNothing() {
        // given
        when(productRepository.findAll()).thenReturn(List.of());

        // when
        List<Product> result = productService.getAll();

        // then
        assertEquals(result.size(), 0);
    }

    @Test
    public void getByIdWorksFine() {
        // given
        when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(productList().get(0)));

        // when
        Product result = productService.getById(anyInt());

        // then
        assertEquals(result.getId(), 1);
        assertEquals(result.getPrice(), 1.00);
    }

    @Test
    public void getByIdNotFoundException() {
        // given
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> productService.getById(anyInt()));
    }

    public List<Product> productList() {
        return List.of(
                new Product(1, 1.00, "product1"),
                new Product(2, 2.00, "product2"),
                new Product(3, 3.00, "product3"),
                new Product(4, 4.00, "product4"),
                new Product(5, 5.00, "product5"),
                new Product(6, 6.00, "product6"),
                new Product(7, 7.00, "product7"),
                new Product(8, 8.00, "product8"),
                new Product(9, 9.00, "product9"),
                new Product(10, 10.00, "product10")
        );
    }
}
