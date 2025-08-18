package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.security.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.security.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.pedroacbg.rest_with_spring_boot.model.Person;
import com.pedroacbg.rest_with_spring_boot.model.User;
import com.pedroacbg.rest_with_spring_boot.repository.UserRepository;
import com.pedroacbg.rest_with_spring_boot.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    public ResponseEntity<TokenDTO> singIn(AccountCredentialsDTO credentials){

        // faz a autenticacao do usuario de acordo com as credenciais do usuario na requisição
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        // busca o usuario de acordo com o username
        var user = userRepository.findByUsername(credentials.getUsername());
        // se o usuario nao existir lança exception
        if(user == null) throw new UsernameNotFoundException("Username "+credentials.getUsername()+" not found");

        // cria o token de acordo com o username e as roles
        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if (user != null){
            token = tokenProvider.refreshAccessToken(refreshToken);
        }else{
            throw new UsernameNotFoundException("Username "+username+ " not found");
        }
        return ResponseEntity.ok(token);
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user){
        if(user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new User!");
        var entity = new User();
        entity.setFullName(user.getFullName());
        entity.setUsername(user.getUsername());
        entity.setPassword(generatedHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        return parseObject(userRepository.save(entity), AccountCredentialsDTO.class);
    }

    private String generatedHashedPassword(String password) {
		PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8,
				185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);

	}

}
