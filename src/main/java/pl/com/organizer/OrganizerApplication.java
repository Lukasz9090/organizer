package pl.com.organizer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.model.Role;
import pl.com.organizer.repository.RoleRepository;

@SpringBootApplication 
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
}
