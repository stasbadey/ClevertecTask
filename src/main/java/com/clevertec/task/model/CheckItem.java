package com.clevertec.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckItem {

    private Product product;

    private int quantity;

    private double sum;

    private boolean isDiscount;
}
