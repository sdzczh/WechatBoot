package com.zh.program.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhaohe
 * @since 2019-03-19
 */
@Data
@Entity
@Table(name = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = -3703491852417733717L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /**
     * 商户id
     */
    private Integer mid;
    /**
     * 标题
     */
    private String title;
    /**
     * 富文本信息
     */
    private String info;
    /**
     * 广告类型
     */
    private Integer type;
    /**
     * 推广数量
     */
    private BigDecimal amount;
    /**
     * 剩余推广数量
     */
    private BigDecimal remain;
    /**
     * 创建时间
     */
    private Date createTime;


}
