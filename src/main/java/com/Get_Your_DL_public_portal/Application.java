package com.Get_Your_DL_public_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
// /html/body/div/div/header/h1 <h1 class="text-3xl font-bold text-gray-800" style="
//    margin-top: -100px;
//">My Dashboard</h1>