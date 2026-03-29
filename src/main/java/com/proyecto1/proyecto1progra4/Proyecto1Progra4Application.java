package com.proyecto1.proyecto1progra4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Proyecto1Progra4Application {

    public static void main(String[] args) {
        SpringApplication.run(Proyecto1Progra4Application.class, args);
    }
    public class HashTest {
        public static void main(String[] args) {
            System.out.println(new BCryptPasswordEncoder().encode("111"));
        }
    }

}

//http://localhost:8080/
