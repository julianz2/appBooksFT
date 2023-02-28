package com.distribuida.test;

import com.distribuida.config.DbConfig;
import com.distribuida.db.Book;
import com.distribuida.servicios.BookRepository;
import com.distribuida.servicios.BookRepositoryImpl;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.tests.junit5.*;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
@DisableDiscovery
@AddExtension(ConfigCdiExtension.class)
@AddBeans({
        @AddBean(DbConfig.class),
        @AddBean(BookRepositoryImpl.class)
})
//
//@AddConfigs({
//        @AddConfig(key = "db.user", value = "postgres"),
//})
public class DatabaseTest {

    @Inject BookRepository repo;

    @Test
    void testPrueba() {

        Optional<Book> book = repo.findById(10);

        assertEquals(true, book.isPresent(),"Book found");

        if(book.isPresent()) {
            assertEquals( 10, book.get().getId() );
        }
    }

    @Test
    void testPrueba2() {

        Optional<Book> book = repo.findById(100);

        assertEquals(true, book.isPresent(),"Book found");

        if(book.isPresent()) {
            assertEquals( 10, book.get().getId() );
        }
    }
}
