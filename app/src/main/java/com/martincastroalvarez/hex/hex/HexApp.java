package com.martincastroalvarez.hex.hex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class HexApp {
    protected static final Logger logger = Logger.getLogger(HexApp.class.getName());

	public static void main(String[] args) {
        logger.info("Starting web server!");
		SpringApplication.run(HexApp.class, args);
	}

}
