package com.app;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.persistence.entities.PermisionEntity;
import com.app.persistence.entities.RoleEnum;
import com.app.persistence.entities.RolesEntity;
import com.app.persistence.entities.UserEntity;
import com.app.persistence.repository.UserRepository;

@SpringBootApplication
public class SpringSecuirtyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecuirtyAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {

			// CREATE PERMISSIONS
			PermisionEntity createPermission = PermisionEntity.builder()
					.name("CREATE")
					.build();

			PermisionEntity readPermission = PermisionEntity.builder()
					.name("READ")
					.build();

			PermisionEntity updatePermission = PermisionEntity.builder()
					.name("UPDATE")
					.build();

			PermisionEntity deletePermission = PermisionEntity.builder()
					.name("DELETE")
					.build();

			PermisionEntity refactorPermission = PermisionEntity.builder()
					.name("REFACTOR")
					.build();

			// CREATE ROLES

			RolesEntity roleAdmin = RolesEntity
					.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			RolesEntity roleUser = RolesEntity
					.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			RolesEntity roleInvited = RolesEntity
					.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(readPermission))
					.build();

			RolesEntity roleDeveloper = RolesEntity
					.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			// CREATE USERS

			UserEntity userMateo = UserEntity
					.builder()
					.username("mateo")
					.password("$2a$10$JaL00CGH3CyVpINQVQqBN.3KDaQPQ3.98hQuV34N/NJ3v8j2sGuxe")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userDaniel = UserEntity
					.builder()
					.username("daniel")
					.password("$2a$10$JaL00CGH3CyVpINQVQqBN.3KDaQPQ3.98hQuV34N/NJ3v8j2sGuxe")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userAndrea = UserEntity
					.builder()
					.username("andrea")
					.password("$2a$10$JaL00CGH3CyVpINQVQqBN.3KDaQPQ3.98hQuV34N/NJ3v8j2sGuxe")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			UserEntity userDeveloper = UserEntity
					.builder()
					.username("developer")
					.password("$2a$10$JaL00CGH3CyVpINQVQqBN.3KDaQPQ3.98hQuV34N/NJ3v8j2sGuxe")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			userRepository.saveAll(List.of(userAndrea, userDaniel, userDeveloper, userMateo));		
		};
	}
}
