package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.security.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.security.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.repository.UserRepository;
import com.pedroacbg.rest_with_spring_boot.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

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
}
