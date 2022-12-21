package com.clevertec.task;

import com.clevertec.task.model.CheckItem;
import com.clevertec.task.model.Product;
import com.clevertec.task.service.DiscountCardService;
import com.clevertec.task.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckGenerator {

    private final ProductService productService;
    private final DiscountCardService discountCardService;

    private static final List<CheckItem> checkItems = new ArrayList<>();
    private static final String TXT_FORMAT = ".txt";
    private static final String CARD_PREFIX = "card-";

    private static double sumOfCheck;
    private static double sumOfCheckWithDiscount;

    public void generate(String[] args) throws IOException {
        if (args[0].endsWith(TXT_FORMAT)) {
            String argsFromFile = Files.readString(Path.of(Paths.get("src", "main", "resources") + "\\" + args[0]));
            args = argsFromFile.split(" ");
        }

        if (checkDiscountCard(args[args.length - 1])) {
            calculateCheckItems(Arrays.copyOf(args, args.length - 1));
            calculateSumOfCheck();
            String numberOfCard = args[args.length - 1].substring(5);
            int percentage = discountCardService.getByNumberOfCard(numberOfCard).getPercentage();

            sumOfCheckWithDiscount = (sumOfCheck / 100) * (100 - percentage);
            sumOfCheckWithDiscount = roundNumber(sumOfCheckWithDiscount);
        } else {
            calculateCheckItems(args);
            calculateSumOfCheck();
            sumOfCheckWithDiscount = sumOfCheck;
        }

        generateCheckToFile();
    }

    private static void calculateSumOfCheck() {
        checkItems.forEach(
                checkItem -> sumOfCheck += checkItem.getSum()
        );
        sumOfCheck = roundNumber(sumOfCheck);
    }

    private static boolean checkDiscountCard(String arg) {
        return arg.startsWith(CARD_PREFIX);
    }

    private void calculateCheckItems(String[] args) {
        Arrays.stream(args)
                .forEach(arg -> {
                    String[] splittedArgument = arg.split("-");
                    int id = Integer.parseInt(splittedArgument[0]);
                    int quantity = Integer.parseInt(splittedArgument[1]);
                    Product currentProduct = productService.getById(id);

                    Double productPrice = productService.getById(id).getPrice();
                    double productPriceSum = productPrice * quantity;
                    if (quantity >= 5) {
                        checkItems.add(new CheckItem(currentProduct, quantity, roundNumber(productPriceSum * 0.9), true));
                    } else {
                        checkItems.add(new CheckItem(currentProduct, quantity, productPriceSum, false));
                    }

                });
    }

    private static double roundNumber(double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void generateCheckToFile() {
        try (FileWriter writer = new FileWriter(Path.of(Paths.get("src", "main", "resources") + "\\output.txt" ).toFile())) {
            writer.write("CASH RECEIPT");
            writer.append('\n');
            writer.append("Date: ").append(LocalDate.now().toString());

            writer.append('\n').append('\n').append('\n');
            writer.write(String.format("%-5s%-25s%-10s%-10s%-15s\n", "QTY", "DESCRIPTION", "PRICE", "TOTAL", "DISCOUNT"));
            checkItems.forEach(
                    checkItem -> {
                        try {
                            writer.write(String.format("%-5s%-25s%-10s%-10s%-15s\n", checkItem.getQuantity(), checkItem.getProduct().getName(),
                                    "$" + roundNumber(checkItem.getProduct().getPrice()), "$" + roundNumber(checkItem.getSum()), (checkItem.isDiscount() ? "%10" : "")));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            writer.append('\n').append('\n');

            writer.write("Total without discount: $" + sumOfCheck);
            writer.append('\n').append('\n');
            writer.write("TOTAL: $" + sumOfCheckWithDiscount);

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
