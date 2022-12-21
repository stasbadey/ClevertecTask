package com.clevertec.task;

import com.clevertec.task.service.DiscountCardService;
import com.clevertec.task.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;


@SpringBootApplication
public class CheckRunner {

    public static void main(String[] args) {
        SpringApplication.run(CheckRunner.class);
    }

    @Bean
    public CommandLineRunner generate(CheckGenerator generator,
                                      ProductService productService,
                                      DiscountCardService discountCardService) {
        return (args) -> {
            productService.populateDb();
            discountCardService.populateDb();

            Scanner in = new Scanner(System.in);
            System.out.print("Input args: ");
            args = in.nextLine().split(" ");

            generator.generate(args);
        };
    }
}
