package com.proyecto1.proyecto1progra4.Logic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo{
    private String tipo;
    @Id
    @NotBlank
    private String id;
    @NotBlank
    private String nombre;
    @NumberFormat(style = NumberFormat.Style.DEFAULT)
    @Positive
    private double monto;
    @Positive
    private double tasa;
    @Positive
    private double plazo;

    public double getCuota(){
        double cuota;
        cuota   = monto * tasa/100 /(1-Math.pow(1+tasa/100,-plazo));
        return cuota;
    }
    public double getTotal(){
        double total;
        total = this.getCuota() * this.getPlazo();
        return total;
    }

}