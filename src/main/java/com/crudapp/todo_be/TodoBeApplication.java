package com.crudapp.todo_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TodoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoBeApplication.class, args);
    }

}
