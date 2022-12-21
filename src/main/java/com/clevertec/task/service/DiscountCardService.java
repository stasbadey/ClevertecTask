package com.clevertec.task.service;

import com.clevertec.task.model.DiscountCard;

import java.util.List;

public interface DiscountCardService {

    List<DiscountCard> getAll();

    DiscountCard getByNumberOfCard(String numberOfCard);

    void populateDb();
}
