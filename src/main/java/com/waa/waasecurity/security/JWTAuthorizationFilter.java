package com.waa.waasecurity.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.waa.waasecurity.SecurityConstants;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.addHeader("Access-Control-Expose-Headers",
				"set,Content-Type,Access-Control-Allow-Origin,Access-Control-Allow-Credentials, authorization");
		response.setHeader("Access-Control-Allow-Headers",
				"set,Content-Type, Accept, X-Requested-With, remember-me, Authorization");
		
		
		String jwtToken = request.getHeader(SecurityConstants.HEADER_STRING);
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		if (jwtToken == null || !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET)
				.parseClaimsJws(jwtToken.replace(SecurityConstants.TOKEN_PREFIX, "")).getBody();

		String username = claims.getSubject();

		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		ArrayList<Map<String, Object>> roles = (ArrayList<Map<String, Object>>) claims.get("roles");

		roles.forEach(l -> {
			authorities.add(new SimpleGrantedAuthority((String) l.get("authority")));
		});

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, null, authorities );
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);

	}
}