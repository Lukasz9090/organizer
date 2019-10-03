package pl.com.soska.organizer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.com.soska.organizer.enums.RoleEnum;
import pl.com.soska.organizer.model.Role;
import pl.com.soska.organizer.repository.RoleRepository;

import javax.validation.Validator;


@SpringBootApplication // @Configuration, @EnableAutoConfiguration, @ComponentScan
public class OrganizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizerApplication.class, args);
    }

    @Bean
    public CommandLineRunner addUserRole(RoleRepository roleRepository){
        return args -> {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            if (userRole == null){
                Role roleUserRole = new Role();
                roleUserRole.setRole(RoleEnum.ROLE_USER);
                roleRepository.save(roleUserRole);
            }
        };
    }

    @Bean
    public Validator validator(){
        return new LocalValidatorFactoryBean();
    }

}
