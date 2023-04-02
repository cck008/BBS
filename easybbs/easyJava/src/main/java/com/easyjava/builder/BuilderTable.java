package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderTable {

    private static final Logger logger = LoggerFactory.getLogger(BuilderTable.class);

    // 查询数据库中的表
    private static String SQL_SHOW_TABLE_STATUS = "show table status";

    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";

    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";


    // 连接数据库
    private static Connection conn = null;

    // 初始化
    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("数据库连接失败！", e);
        }
    }

    // 获取表信息
    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;

        List<TableInfo> tableInfoList = new ArrayList();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            // 遍历查询结果
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
//                logger.info("tableName:{},comment:{}", tableName, comment);


                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PERFIX) {
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }

                beanName = processFiled(beanName, true);
//                logger.info("bean:{}" + beanName);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);

//                logger.info("表:{},备注:{},JavaBean:{},JavaParam:{}" , tableInfo.getTableName(),tableInfo.getComment(),tableInfo.getBeanName(),tableInfo.getBeanParamName());
                readFieldInfo(tableInfo);

//                logger.info("tableInfo:{}" ,JsonUtils.convertObj2Json(tableInfo));
                getKeyIndexInfo(tableInfo);
//                logger.info("tableInfo:{}", JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }

        } catch (Exception e) {
            logger.error("读取表失败！", e);
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableInfoList;
    }

    // 读取表的 FieldInfo
    private static void readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList();

        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();

            Boolean haveDateTime = false;
            Boolean haveDate = false;
            Boolean haveBigDecimal = false;
            // 遍历查询结果
            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");

//                logger.info(field,type,extra, comment);
                if (type.indexOf("(") > 0) {       // 去掉字段中的（）及里面的内容
                    type = type.substring(0, type.indexOf("("));
                }
                String propertyName = processFiled(field, false);       // 将 field 转换为第二个字母大写

                FieldInfo fieldInfo = new FieldInfo();

                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra) ? true : false);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));
                fieldInfoList.add(fieldInfo);


                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
                    haveDateTime = true;
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    haveDate = true;
                }

                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
                    haveBigDecimal = true;
                }

//                logger.info("javaType:{}", fieldInfo.getJavaType());

//                logger.info("field:{},type:{},extra:{},comment:{},propertyName:{}", field,type,extra, comment,propertyName);


            }
            tableInfo.setHaveDateTime(haveDateTime);
            tableInfo.setHaveDate(haveDate);
            tableInfo.setHaveBigDecimal(haveBigDecimal);
            tableInfo.setFieldList(fieldInfoList);

        } catch (Exception e) {
            logger.error("读取表失败！", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // 解析唯一索引
    private static List<FieldInfo> getKeyIndexInfo(com.easyjava.bean.TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList();
        try {
            Map<String, FieldInfo> tempMap = new HashMap();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                tempMap.put(fieldInfo.getFieldName(),fieldInfo);
            }

            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            // 遍历查询结果
            while (fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");

                if (nonUnique == 1) {
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList) {
                    keyFieldList = new ArrayList();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }

                keyFieldList.add(tempMap.get(columnName));

//                tableInfo.getFieldList();

            }
        } catch (Exception e) {
            logger.error("读取索引失败！", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return fieldInfoList;
    }

    // 转为驼峰式
    private static String processFiled(String field, Boolean uperCaseFirstLetter) {
        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");     // 以 _ 进行分割
        sb.append(uperCaseFirstLetter ? StringUtils.uperCaseFirstLetter(fields[0]) : fields[0]);
        for (int i = 1, len = fields.length; i < len; i++) {
            sb.append(StringUtils.uperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    // java 类型转换
    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别的类型：" + type);
        }
    }

}




















