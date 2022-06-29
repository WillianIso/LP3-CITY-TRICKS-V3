package com.CityTricks.citytricks.controller;

import com.CityTricks.citytricks.dto.UsuarioDTO;
import com.CityTricks.citytricks.model.entity.Usuario;
import com.CityTricks.citytricks.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3688)
@RequestMapping("/usuario")

public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @PostMapping(path="/salvar")
    public ResponseEntity<Object> saveUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO)
    {
        var usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        usuario.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioService.save(usuarioDTO);

        return new ResponseEntity<Object>(HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity get() {
        List<Usuario> usuario = usuarioService.getUsuario();
        return ResponseEntity.ok(usuario.stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario.map(UsuarioDTO::create));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
            usuarioService.excluir(usuario.get());
            return new ResponseEntity(HttpStatus.OK);

    }

}