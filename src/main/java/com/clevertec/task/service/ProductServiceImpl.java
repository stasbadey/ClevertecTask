package com.clevertec.task.service;

import com.clevertec.task.exception.NotFoundException;
import com.clevertec.task.model.Product;
import com.clevertec.task.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + "not found"));
    }

    @Override
    @Transactional
    public void populateDb() {
        Random random = new Random();
        IntStream.range(1, 100)
                .forEach(
                        id -> {
                            if (!productRepository.existsById(id)) {
                                productRepository.save(
                                        new Product(id, roundNumber(10 * random.nextDouble()), "product" + id));
                            }
                        }
                );
    }

    private static double roundNumber(double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
