package com.clevertec.task.service;

import com.clevertec.task.exception.NotFoundException;
import com.clevertec.task.model.DiscountCard;
import com.clevertec.task.model.Product;
import com.clevertec.task.repository.DiscountCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DiscountCardServiceTest {

    @Mock
    DiscountCardRepository discountCardRepository;

    @InjectMocks
    DiscountCardServiceImpl discountCardService;

    @Test
    public void getAllWorksFine() {
        // given
        when(discountCardRepository.findAll()).thenReturn(discountCardList());

        // when
        List<DiscountCard> result = discountCardService.getAll();

        // then
        assertEquals(result.size(), discountCardList().size());
    }

    @Test
    public void getAllReturnsNothing() {
        // given
        when(discountCardRepository.findAll()).thenReturn(List.of());

        // when
        List<DiscountCard> result = discountCardService.getAll();

        // then
        assertEquals(result.size(), 0);
    }

    @Test
    public void getByIdWorksFine() {
        // given
        when(discountCardRepository.findByNumberOfCard(anyString()))
                .thenReturn(Optional.ofNullable(discountCardList().get(0)));

        // when
        DiscountCard result = discountCardService.getByNumberOfCard(anyString());

        // then
        assertEquals(result.getId(), 1);
        assertEquals(result.getNumberOfCard(), "001");
    }

    @Nested
    public class checkAllDiscountCards {
        @Test
        public void getByIdNotFoundException() {
            // given
            when(discountCardRepository.findByNumberOfCard(anyString())).thenReturn(Optional.empty());

            // then
            assertThrows(NotFoundException.class, () -> discountCardService.getByNumberOfCard(anyString()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234", "1111", "001"})
        public void getByIdNotFoundException(String numberOfCard) {
            // given
            when(discountCardRepository.findByNumberOfCard(numberOfCard)).thenReturn(Optional.empty());

            // then
            assertThrows(NotFoundException.class, () -> discountCardService.getByNumberOfCard(numberOfCard));
        }
    }



    @Test
    public void checkAllDiscountCardsShouldReturn10() {
        //prepare
        int expectedSize = 10;

        //action
        List<DiscountCard> actualDiscountCards = discountCardList();

        //check
        assertThat(actualDiscountCards).hasSize(expectedSize);
    }

    @Test
    public void checkFindUserContain() {
        List<DiscountCard> actualDiscountCards = discountCardList();

        assertThat(actualDiscountCards).contains(new DiscountCard(2, "002", 5));
    }

    @Test
    public void populateDBAndCheckSizeOfDBShouldReturn0() {
        int expectedSize = 0;

        discountCardService.populateDb();
        List<DiscountCard> discountCardRepositoryAll = discountCardRepository.findAll();

        assertThat(discountCardRepositoryAll).hasSize(expectedSize);
    }


    public List<DiscountCard> discountCardList() {
        return List.of(
                new DiscountCard(1, "001", 5),
                new DiscountCard(2, "002", 5),
                new DiscountCard(3, "003", 5),
                new DiscountCard(4, "004", 5),
                new DiscountCard(5, "005", 5),
                new DiscountCard(506, "506", 10),
                new DiscountCard(507, "507", 10),
                new DiscountCard(508, "508", 10),
                new DiscountCard(509, "509", 10),
                new DiscountCard(510, "510", 10)
        );
    }
}

