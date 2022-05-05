package com.waa.waasecurity.service;


import com.waa.waasecurity.model.AppUser;
import com.waa.waasecurity.repository.AppUserRepository;
import com.waa.waasecurity.security.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser u = userRepository.findByUsername(username);

		if (u == null)
			throw new UsernameNotFoundException(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		u.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRole()));
		});
		UserDto uu = new UserDto(u.getUsername(), u.getPassword() ,authorities);

		return uu;
	}
}