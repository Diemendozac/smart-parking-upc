/**
 * 
 */
package com.smartparkingupc.security;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SecurityPrincipal {

	private final Authentication principal = SecurityContextHolder.getContext().getAuthentication();

	private static IUserService userService;

	@Autowired
	private SecurityPrincipal(IUserService userService) {
		this.userService = userService;
	}

	public static SecurityPrincipal getInstance() {
		return new SecurityPrincipal(userService);
	}

	public UserEntity getLoggedInPrincipal() {
		if (principal != null) {
			UserDetails loggedInPrincipal = (UserDetails) principal.getPrincipal();
			Optional<UserEntity> optUser = userService.findUserByEmail(loggedInPrincipal.getUsername());
			if (optUser.isPresent()) {
				return optUser.get();
			}
		}
		return null;
	}

	public Collection<?> getLoggedInPrincipalAuthorities() {
		return ((UserDetails) principal.getPrincipal()).getAuthorities();
	}

}
