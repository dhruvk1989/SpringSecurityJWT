package com.dhruv.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JWTUtil jwtTokenUtil;

    @GetMapping("/")
    public String getGreeting(){
        return "Hello world";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest auth) throws Exception {
        //authenticate
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        //fetch user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getUsername());

        //fetch jwt token
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);

        //return the damn response
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }

}
