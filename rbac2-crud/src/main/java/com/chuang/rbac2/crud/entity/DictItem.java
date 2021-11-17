package com.chuang.rbac2.crud.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.chuang.rbac2.crud.entity.api.TreeModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典项;
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_item")
public class DictItem implements Serializable, TreeModel {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("label")
    private String label;

    /**
     * 值
     */
    @TableField("val")
    private Byte val;

    /**
     * 排序
     */
    @TableField("sort_rank")
    private Integer sortRank;

    /**
     * 类型编码
     */
    @TableField("dict_type_code")
    private String dictTypeCode;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 路径
     */
    @TableField("parents")
    private String parents;

    /**
     * 备注
     */
    @TableField("description")
    private String description;

    /**
     * 是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 创建人
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updater", fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;


}
