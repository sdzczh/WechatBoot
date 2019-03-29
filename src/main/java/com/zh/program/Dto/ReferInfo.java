package com.zh.program.Dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class ReferInfo {
    @Id
    private Integer id;
    private String title;
    private BigDecimal price;
    private BigDecimal total;
    private Integer state;
    private String stateStr;
}
