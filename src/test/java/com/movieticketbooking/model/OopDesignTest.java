package com.movieticketbooking.model;

import com.movieticketbooking.model.pricing.DiscountedPricing;
import com.movieticketbooking.model.pricing.StandardPricing;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OopDesignTest {

    @Test
    void customerAndUserArePersons() {
        Person customer = TestFixtures.customer();
        Person user = new User("Bob Smith", "bob@example.com", null, "bob", "hash1");

        assertInstanceOf(Person.class, customer);
        assertInstanceOf(Person.class, user);
    }

    @Test
    void inheritedFieldsAreShared() {
        Person customer = TestFixtures.customer();

        assertEquals("Alice Johnson", customer.getFullName());
        assertEquals("alice@example.com", customer.getEmail());
    }

    @Test
    void polymorphicDescribeReturnsRoleSpecificText() {
        List<Person> people = List.of(
                TestFixtures.customer(),
                new User("Bob Smith", "bob@example.com", null, "bob", "hash1"));

        assertEquals("Customer: Alice Johnson", people.get(0).describe());
        assertEquals("User: bob", people.get(1).describe());
        assertNotEquals(people.get(0).describe(), people.get(1).describe());
    }

    @Test
    void pricingStrategyIsPolymorphic() {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(2, 3));
        List<Seat> seats = show.getSeats();

        Ticket standard = new Ticket(show, seats, new BigDecimal("12.00"), new StandardPricing());
        Ticket belowThreshold = new Ticket(show, seats, new BigDecimal("12.00"), new DiscountedPricing(9, new BigDecimal("10")));
        Ticket discounted = new Ticket(show, seats, new BigDecimal("12.00"), new DiscountedPricing(6, new BigDecimal("10")));

        assertEquals(new BigDecimal("72.00"), standard.getTotal());
        assertEquals(new BigDecimal("72.00"), belowThreshold.getTotal());
        assertEquals(new BigDecimal("64.80"), discounted.getTotal());
    }
}