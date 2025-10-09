package com.sosyalmedia.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//TODO
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//DOWNLOAD    DOWNLOAD    DOWNLOAD    DOWNLOAD    DOWNLOAD    DOWNLOAD    DOWNLOAD
//        __________________________________________________________________________________________________________________________________________________________________________________________________________________________________
//
//bunlar kontrol edilmeli. aynı isimde tekrar indirince problem oluyor falan
//        _________________________________________________________________________________________________________________
//DOWNLOAD FILE
//
//curl -O -J http://localhost:8081/api/customers/17/files/download/70
//
//_________________________________________________________________________________________________________________
//
//DOWNLOAD PDF
//curl -O -J http://localhost:8081/api/customers/17/files/download/70

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
