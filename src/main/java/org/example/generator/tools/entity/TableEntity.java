package org.example.generator.tools.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据库表entity
 * 
 * @date 2021/4/29 16:26
 * @since
 * @version
 */
@Data
@NoArgsConstructor
public class TableEntity {
    /**
     * 列信息
     */
    private List<ColumnEntity> entities;
    /**
     * 表物理名
     */
    private String physicalTableName;
    /**
     * 表逻辑名
     */
    private String logicalTableName;
    /**
     * 数据库名
     */
    private String databaseName;

}
