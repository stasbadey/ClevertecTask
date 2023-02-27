package com.clevertec.task.service;

import com.clevertec.task.exception.NotFoundException;
import com.clevertec.task.model.DiscountCard;
import com.clevertec.task.model.Product;
import com.clevertec.task.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Nested
    public class checkAllProducts {
        @Test
        public void getByIdNotFoundException() {
            // given
            when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

            // then
            assertThrows(NotFoundException.class, () -> productService.getById(anyInt()));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 10, 11})
        public void checkFindByIdAndNotFoundException(int num) {
            //given
            when(productRepository.findById(num)).thenReturn(Optional.empty());
            //then
            assertThrows(NotFoundException.class, () -> productService.getById(num));
            verify(productRepository).findById(num);
        }

        @Test
        public void getByIdArgumentCapture() {
            Product product = new Product(1, 1.00, "product1");

            when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
            ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
            verify(productRepository).findById(argumentCaptor.capture());

            assertThat(argumentCaptor.getValue()).isEqualTo(product.getId());
        }
    }



    @Test
    public void checkAllProductsShouldReturn10() {
        //prepare
        int expectedSize = 10;

        //action
        List<Product> actualProducts = productList();

        //check
        assertThat(actualProducts).hasSize(expectedSize);
    }

    @Test
    public void checkFindUserContain() {
        List<Product> actualProducts = productList();

        assertThat(actualProducts).contains(new Product(2, 2.00, "product2"));
    }

    @Test
    public void populateDBAndCheckSizeOfDBShouldReturn100() {
        int expectedSize = 100;

        productService.populateDb();
        List<Product> productRepositoryAll = productRepository.findAll();

        assertThat(productRepositoryAll).hasSize(expectedSize);
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
