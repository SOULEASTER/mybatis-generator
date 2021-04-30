package org.example.generator.tools.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表行entity
 * 
 * @date 2021/4/29 16:28
 * @since
 * @version
 */
@Data
@NoArgsConstructor
public class ColumnEntity {
    /**
     * 物理列名
     */
    private String physicalColumnName;
    /**
     * 逻辑列名
     */
    private String logicalColumnName;
    /**
     * 类型
     */
    private String type;
    /**
     * 长度
     */
    private String length;
    /**
     * 精度
     */
    private String decimal;
    /**
     * 是否主键
     */
    private boolean isPrimaryKey;
    /**
     * 是否非空
     */
    private boolean isNotNull;

}
