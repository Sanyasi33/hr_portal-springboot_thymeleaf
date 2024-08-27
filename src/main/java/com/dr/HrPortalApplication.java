package com.dr;

import com.dr.models.db.Credentials;
import com.dr.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class HrPortalApplication {

	public static void main(String[] args) {

		SpringApplication.run(HrPortalApplication.class, args);

		/* REMOVE THE COMMENT WHEN U WANTS TO ADD NEW HR DETAILS IN CREDENTIALS TABLE
		ApplicationContext ctx=SpringApplication.run(HrPortalApplication.class, args);
		CredentialsService cs=ctx.getBean(CredentialsService.class);

		Credentials c=new Credentials();
		c.setUsername("rajesh@gmail.com");
		c.setPassword("rajesh@123");
		c.setRoles("ADMIN");

		cs.addCredential(c);
		*/

		System.out.println(LocalDate.now()+" **************************** "+ LocalTime.now());
	}

}
