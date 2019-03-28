package com.zh.program.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 浏览记录
 */
@Data
@Entity
@Table(name = "browse_record")
public class BrowseRecord implements Serializable {

    private static final long serialVersionUID = -3703491852417733717L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer msg_id; // 广告ID
    private String user_open_id;//用户open_id
    private String invite_open_id;//邀请人open_id
    private Integer number; // 点击数量
    private Integer state; // 订单状态 0未完成 1已完成
    private String createTime;
}
