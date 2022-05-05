package com.waa.waasecurity;

import com.waa.waasecurity.model.AppRole;
import com.waa.waasecurity.model.AppUser;
import com.waa.waasecurity.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = { "com.waa.waasecurity" })

public class WaaSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaaSecurityApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	@Transactional
	CommandLineRunner start( AppUserRepository appUserRepository,
							 BCryptPasswordEncoder bCryptPasswordEncoder) {


		return args -> {
			AppRole admin = new AppRole(1L,"ADMIN");
			AppRole user = new AppRole(2L,"USER");

			appUserRepository.save(new AppUser(1,"admin",bCryptPasswordEncoder.encode("admin"), List.of(admin)));
			appUserRepository.save(new AppUser(2,"user",bCryptPasswordEncoder.encode("user"), List.of(user)));

		};
	}

}
