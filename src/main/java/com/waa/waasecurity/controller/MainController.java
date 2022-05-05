package com.waa.waasecurity.controller;

import com.waa.waasecurity.model.AppUser;
import com.waa.waasecurity.model.RegistrationForm;
import com.waa.waasecurity.repository.AppUserRepository;
import com.waa.waasecurity.security.dto.JwtResponse;
import com.waa.waasecurity.security.dto.LoginUser;
import com.waa.waasecurity.service.UserDetailsServiceImpl;
import com.waa.waasecurity.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myapp")
public class MainController {

    @Autowired
    JWTUtility jwtUtility;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserRepository userRepository;

    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody LoginUser jwtRequest) throws Exception{

        userRepository.findAll().stream().forEach(u->System.out.println(u.getUsername() +" "+u.getPassword()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            System.out.println(e);
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return  new JwtResponse(token);
    }


    @GetMapping
    public String getHelloWord(){
        return "Hello Word!";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin(){
        return "Hello ADMIN!";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String getUser(){
        return "Hello USER!";
    }
}
