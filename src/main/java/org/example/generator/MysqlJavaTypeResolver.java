package org.example.generator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * 数据库类型到JAVA类型映射
 * 通过标签指定，如：
 * <javaTypeResolver type="org.example.generator.MysqlJavaTypeResolver"/>
 * @author 魏荣杰
 * @date 2020/9/5 20:29
 * @since
 * @version
 */
public class MysqlJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    public MysqlJavaTypeResolver() {
        super();
        this.typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Integer.class.getName())));
        this.typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
    }
}
