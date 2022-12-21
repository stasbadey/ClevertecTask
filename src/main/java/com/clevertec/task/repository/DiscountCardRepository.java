package com.clevertec.task.repository;

import com.clevertec.task.model.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Integer> {

    Optional<DiscountCard> findByNumberOfCard(String numberOfCard);
}
