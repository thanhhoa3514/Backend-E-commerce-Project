package com.project.e_commerce.utils;

import com.github.javafaker.Faker;
import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.models.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.ZoneId;

@Component
public class FakeDataGeneratorUtil {
    private final Faker faker;


    public FakeDataGeneratorUtil() {
        this.faker = new Faker();
    }

    // Generate a single user
    public User generateFakeUser() {
        User user = new User();
        user.setFullName(faker.name().fullName());
//        user.setEmail(faker.internet().emailAddress());
        user.setPhoneNumber(faker.phoneNumber().cellPhone());
        user.setAddress(faker.address().fullAddress());
        user.setDateOfBirth(faker.date().birthday().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate());
        user.setFacebookAccountId(faker.random().nextInt(100));
        user.setGoogleAccountId(faker.random().nextInt(100));

        return user;
    }

    public ProductDTO generateFakeProductDTO() {
        return ProductDTO.builder()
                .name(faker.commerce().productName())
                .price(faker.number().numberBetween(10, 90_000_000))
                .description(faker.lorem().sentence())
                .thumbnail("")
                .categoryId((long) faker.number().numberBetween(1,6))
                .quantity(faker.number().numberBetween(1, 1000))
                .build();
    }

    // Generate a list of fake users
    public List<User> generateFakeUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateFakeUser())
                .collect(Collectors.toList());
    }



    // More specialized fake data generation
    public String generateFakeProductBarcode() {
        return faker.code().isbn10();
    }

    public String generateFakeCompanyName() {
        return faker.company().name();
    }

    public String generateFakeCreditCardNumber() {
        return faker.finance().creditCard();
    }
}
