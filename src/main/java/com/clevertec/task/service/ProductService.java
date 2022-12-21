package com.clevertec.task.service;

import com.clevertec.task.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Integer id);

    void populateDb();
}
