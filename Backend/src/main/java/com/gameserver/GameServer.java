package com.gameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is the main entry point for the game server.
 */
@SpringBootApplication
public class GameServer {

	public static void main(String[] args) {
		SpringApplication.run(GameServer.class, args);
	}

	/**
	 * This method configures the cross-origin resource sharing (CORS) settings for
	 * the game server.
	 *
	 * @return A WebMvcConfigurer that configures CORS.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

}
