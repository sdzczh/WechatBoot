package com.zh.program.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sysparams")
public class Sysparams {
    private static final long serialVersionUID = -3703491852417733717L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String keyName;
    private String keyValue;
    private String remark;
    private Date createTime;
}
