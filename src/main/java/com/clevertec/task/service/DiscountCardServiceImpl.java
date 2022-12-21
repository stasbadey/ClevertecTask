package com.clevertec.task.service;

import com.clevertec.task.exception.NotFoundException;
import com.clevertec.task.model.DiscountCard;
import com.clevertec.task.repository.DiscountCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    @Override
    public List<DiscountCard> getAll() {
        return discountCardRepository.findAll();
    }

    @Override
    public DiscountCard getByNumberOfCard(String numberOfCard) {
        return discountCardRepository.findByNumberOfCard(numberOfCard)
                .orElseThrow(() ->
                        new NotFoundException("Discound card with number of card: " + numberOfCard + " not found"));
    }

    @Override
    @Transactional
    public void populateDb() {
        IntStream.range(1, 999).forEach(
                id -> {
                    if (!discountCardRepository.existsById(id)) {
                        if (id >= 0 && id < 10) {
                            discountCardRepository.save(new DiscountCard(id, "00" + id, 0));
                        } else if (id >= 10 && id < 100) {
                            discountCardRepository.save(new DiscountCard(id, "0" + id, 0));
                        } else {
                            discountCardRepository.save(new DiscountCard(id, String.valueOf(id), 0));
                        }
                    }
                }
        );

        discountCardRepository.findAll().forEach(
                discountCard -> {
                    int numberOfCard = Integer.parseInt(discountCard.getNumberOfCard());
                    if (numberOfCard >= 0 && numberOfCard <= 500) {
                        discountCard.setPercentage(5);
                    } else {
                        discountCard.setPercentage(10);
                    }
                }
        );
    }
}
