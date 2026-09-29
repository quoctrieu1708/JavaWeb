package com.ecom.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import com.ecom.service.UserService;
import com.ecom.model.UserDtls;

@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Autowired
	@org.springframework.context.annotation.Lazy
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
		
		if(authentication.getPrincipal() instanceof CustomUser) {
			CustomUser customUser = (CustomUser) authentication.getPrincipal();
			UserDtls user = customUser.getUser();
			if (user != null) {
				userService.resetAttempt(user.getId());
			}
		} else if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            // In case a different UserDetails is used
        }
		
		if(roles.contains("ROLE_ADMIN"))
		{
			response.sendRedirect("/admin/");
		}else {
			response.sendRedirect("/");
		}
		
	}

}
