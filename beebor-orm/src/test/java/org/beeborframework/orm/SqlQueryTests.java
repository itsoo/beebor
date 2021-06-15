package org.beeborframework.orm;

import lombok.Data;
import lombok.SneakyThrows;
import org.beeborframework.core.converter.BasicConvertChain;
import org.beeborframework.core.util.ObjectUtils;
import org.beeborframework.core.util.StringUtils;
import org.junit.Test;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * SqlQueryTests
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/28 21:56
 */
public class SqlQueryTests {

    @Test
    @SneakyThrows({SQLException.class, ReflectiveOperationException.class})
    public void testQuery() {
        Connection conn = DriverManager.getConnection("jdbc:mysql://wnojrdevmysql.service.dev.consul:6033/sd_marketing?characterEncoding=utf8", "sdmarketingadmin", "zq");
        PreparedStatement ps = conn.prepareStatement("SELECT  * FROM t_nps_task_question WHERE is_valid = 1 LIMIT 2");
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();

        int count = metaData.getColumnCount();
        String[] columnNames = new String[count];
        String[] fieldNames = new String[count];
        for (int i = 0; i < count; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
            fieldNames[i] = StringUtils.sneakCaseToCamelCase(columnNames[i]);
        }

        List<TaskConfigEntity> list = new LinkedList<>();
        while (rs.next()) {
            TaskConfigEntity entity = TaskConfigEntity.class.newInstance();
            for (int i = 0; i < columnNames.length; i++) {
                Class<?> fieldType = ObjectUtils.getFieldType(entity, fieldNames[i]);
                Object value = BasicConvertChain.convert(fieldType, rs.getObject(columnNames[i]));
                ObjectUtils.setFieldValue(entity, fieldNames[i], value);
            }

            list.add(entity);
        }

        System.out.println(list);
    }


    @Data
    public static class TaskConfigEntity {

        private Long id;

        private Long creator;

        private Long updater;

        private Date createTime;

        private Date updateTime;

        private Boolean isValid;

        private Long taskConfigId;

        private Long questionId;

        private String questionDescribe;

        private String answer;

        private Integer questionType;

        private String questionKind;

        private Boolean isRequire;

        private String subQuestions;

        private String tips;

        private String leftValue;

        private String rightValue;

    }
}
