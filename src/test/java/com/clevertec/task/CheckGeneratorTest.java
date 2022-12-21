package com.clevertec.task;

import com.clevertec.task.model.DiscountCard;
import com.clevertec.task.model.Product;
import com.clevertec.task.repository.DiscountCardRepository;
import com.clevertec.task.repository.ProductRepository;
import com.clevertec.task.service.DiscountCardServiceImpl;
import com.clevertec.task.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CheckGeneratorTest {

    @Mock
    private DiscountCardServiceImpl discountCardService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private DiscountCardRepository discountCardRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CheckGenerator checkGenerator;

    @Test
    public void allWorksFine() throws IOException {
        when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(buildProduct()));
        when(discountCardRepository.findByNumberOfCard(anyString())).thenReturn(Optional.ofNullable(buildDiscountCard()));
        when(productService.getById(anyInt())).thenReturn(buildProduct());
        when(discountCardService.getByNumberOfCard(anyString())).thenReturn(buildDiscountCard());
        checkGenerator.generate(args());
    }

    @Test
    public void emptyParameters() throws IOException {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(discountCardRepository.findByNumberOfCard(anyString())).thenReturn(Optional.empty());
        when(productService.getById(anyInt())).thenReturn(buildProduct());
        when(discountCardService.getByNumberOfCard(anyString())).thenReturn(buildDiscountCard());
        checkGenerator.generate(args());
    }

    private String[] args() throws IOException {
        return Files.readString(Path.of(Paths.get("src","test","resources") + "\\data.txt"))
                .split(" ");
    }

    private Product buildProduct() {
        return Product.builder()
                .id(1)
                .name("product1")
                .price(1.00)
                .build();
    }

    private DiscountCard buildDiscountCard() {
        return DiscountCard.builder()
                .id(1)
                .numberOfCard("001")
                .percentage(5)
                .build();
    }
}
