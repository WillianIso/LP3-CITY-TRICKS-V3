package com.CityTricks.citytricks.controller;

import com.CityTricks.citytricks.dto.EstadoDTO;
import com.CityTricks.citytricks.dto.UsuarioDTO;
import com.CityTricks.citytricks.exception.RegraNegocioException;
import com.CityTricks.citytricks.model.entity.Estado;
import com.CityTricks.citytricks.model.entity.Usuario;
import com.CityTricks.citytricks.service.EstadoService;
import com.CityTricks.citytricks.service.UsuarioService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3688)
@RequestMapping("/estado")
public class EstadoController {

    private final EstadoService estadoService;

    @Autowired
    public EstadoController(EstadoService estadoService) {

        this.estadoService = estadoService;
    }

    @PostMapping(path="/salvar")
    public ResponseEntity<Object> saveEstado(@RequestBody @Valid EstadoDTO estadoDTO)
    {
        var estado = new Estado();
        BeanUtils.copyProperties(estadoDTO, estado);
        estado.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        estadoService.save(estadoDTO);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity get() {
        List<Estado> estado = estadoService.getEstado();
        return ResponseEntity.ok(estado.stream().map(EstadoDTO::create).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Estado> estado = estadoService.getEstadoById(id);
        if (!estado.isPresent()) {
            return new ResponseEntity("Estado não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estado.map(EstadoDTO::create));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Estado> estado = estadoService.getEstadoById(id);
        if (!estado.isPresent()) {
            return new ResponseEntity("Estado não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            estadoService.excluir(estado.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody @Valid EstadoDTO estadoDTO) {
        Optional<Estado> estado = estadoService.getEstadoById(id);
        if(!estado.isPresent())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado não encontrado!");
        }
        var estadoEntity = new Estado();
        BeanUtils.copyProperties(estadoDTO, estadoEntity);
        estadoEntity.setId(estado.get().getId());
        estadoEntity.setRegistrationDate(estado.get().getRegistrationDate());
        estadoDTO.setId(estado.get().getId());
        estadoService.save(estadoDTO);
        return new ResponseEntity(HttpStatus.OK);
    }






}
