package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

	public static final String USERNAME_FORMAT = "^[a-z]{2,}[0-9]*$";
	public static final String EMAIL_FORMAT = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; // Source: https://www.baeldung.com/java-email-validation-regex
	public static final String PASSWORD_FORMAT_LOWERCASE = "[^a-z]*[a-z][^a-z]*"; // At least one small letter
	public static final String PASSWORD_FORMAT_UPPERCASE = "[^A-Z]*[A-Z][^A-Z]*"; // At least one big letter

	@Bean
	public PasswordEncoder pswdEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
