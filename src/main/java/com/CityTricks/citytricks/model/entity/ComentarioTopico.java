package com.CityTricks.citytricks.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ComentarioTopico extends Comentario {

    @ManyToOne
    private Topico topico;


    public void setRegistrationDate(LocalDateTime utc) {
    }
}
