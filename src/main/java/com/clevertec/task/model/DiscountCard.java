package com.clevertec.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "discount_card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiscountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number_of_card")
    private String numberOfCard;

    @Column(name = "percentage")
    private int percentage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountCard that = (DiscountCard) o;

        if (percentage != that.percentage) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(numberOfCard, that.numberOfCard);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numberOfCard != null ? numberOfCard.hashCode() : 0);
        result = 31 * result + percentage;
        return result;
    }
}
