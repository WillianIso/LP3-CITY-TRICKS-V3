package com.CityTricks.citytricks.controller;

import com.CityTricks.citytricks.dto.PaisDTO;
import com.CityTricks.citytricks.exception.RegraNegocioException;
import com.CityTricks.citytricks.model.entity.Pais;
import com.CityTricks.citytricks.service.PaisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3688)
@RequestMapping("/pais")
public class PaisController {

    private final PaisService paisService;

    @Autowired
    public PaisController(PaisService paisService) {

        this.paisService = paisService;
    }

    @PostMapping(path="/salvar")
    public ResponseEntity<Object> saveCidade(@RequestBody @Valid PaisDTO paisDTO)
    {
        var pais = new Pais();
        BeanUtils.copyProperties(paisDTO, pais);
        pais.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        paisService.save(paisDTO);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity get() {
        List<Pais> pais = paisService.getPais();
        return ResponseEntity.ok(pais.stream().map(PaisDTO::create).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Pais> pais = paisService.getPaisById(id);
        if (!pais.isPresent()) {
            return new ResponseEntity("Pais não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pais.map(PaisDTO::create));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Pais> pais = paisService.getPaisById(id);
        if (!pais.isPresent()) {
            return new ResponseEntity("Pais não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            paisService.excluir(pais.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody @Valid PaisDTO paisDTO) {
        Optional<Pais> pais = paisService.getPaisById(id);
        if(!pais.isPresent())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
        var paisEntity = new Pais();
        BeanUtils.copyProperties(paisDTO, paisEntity);
        paisEntity.setId(pais.get().getId());
        paisEntity.setRegistrationDate(pais.get().getRegistrationDate());
        paisDTO.setId(pais.get().getId());
        paisService.save(paisDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}