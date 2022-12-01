package com.example.springsecurityapplication.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	private UserDetailsService userDetailsService;
	private JWTTokenHelper jwtTokenHelper;
	
	public JWTAuthenticationFilter(UserDetailsService userDetailsService,JWTTokenHelper jwtTokenHelper) {
		this.userDetailsService=userDetailsService;
		this.jwtTokenHelper=jwtTokenHelper;
		
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println("doFilterInternal");
		String authToken=jwtTokenHelper.getToken(request);

		if(null!=authToken) {
			System.out.println("1");
			String userName=jwtTokenHelper.getUsernameFromToken(authToken);
			if(null!=userName) {
				System.out.println("2");
				UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
				if(jwtTokenHelper.validateToken(authToken, userDetails)) {
					System.out.println("3");
					UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
