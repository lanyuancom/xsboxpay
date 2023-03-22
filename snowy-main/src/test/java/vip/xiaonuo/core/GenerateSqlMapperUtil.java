package vip.xiaonuo.core;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Jeff on 2018/12/5.
 */
public class GenerateSqlMapperUtil {
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        generateSqlMapper(JsonRootBean.class, null);
    }

    public static void generateSqlMapper(Class obj,String tableName) throws IOException{
        Field[] fields = obj.getDeclaredFields();
        String param = null;
        String cameCaseColumn = null;
        String underScoreCaseColumn = null;
        StringBuilder sql = new StringBuilder();
        if(tableName==null||tableName.equals("")){
            // 未传表明默认用类名
            tableName = obj.getName().substring(obj.getName().lastIndexOf(".")+1);
        }
        /**
         * 以下部分生成建表Sql
         */
        sql.append("drop table if exists ").append(tableName).append(";\r\n");
        sql.append("create table ").append(tableName).append("( \r\n");
        boolean firstId = true;
        for(Field f : fields){
            cameCaseColumn = f.getName();
            underScoreCaseColumn = cameCaseColumn;
            for(int i = 0; i < cameCaseColumn.length(); i++)
                if(Character.isUpperCase(cameCaseColumn.charAt(i)))
                    // 将javabean中小驼峰命名变量的“大写字母”转换为“_小写字母”
                    underScoreCaseColumn = cameCaseColumn.substring(0, i) + '_' + cameCaseColumn.substring(i, i+1).toLowerCase() + cameCaseColumn.substring(i+1, cameCaseColumn.length());
            sql.append(underScoreCaseColumn).append(" ");
            param = f.getType().getTypeName();
            if(param.equals("java.lang.Integer")){
                sql.append("INTEGER");
            }else{
                // 根据需要自行修改
                sql.append("VARCHAR(20)");
            }
            if(firstId){
                // 默认第一个字段为ID主键
                sql.append(" PRIMARY KEY AUTO_INCREMENT");
                firstId = false;
            }
            sql.append(",\n");
        }
        sql.delete(sql.lastIndexOf(","), sql.length()).append("\n)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;\r\n");
        System.out.println(sql);
        /**
         * 以下部分生成Mapper
         */
        firstId = true;
        StringBuilder mapper = new StringBuilder();
        StringBuilder resultMap = new StringBuilder();
        StringBuilder insert = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();
        StringBuilder update = new StringBuilder();
        StringBuilder updateWhere = new StringBuilder();
        StringBuilder delete = new StringBuilder();
        StringBuilder deleteWhere = new StringBuilder();
        mapper.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        mapper.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
        mapper.append("<mapper namespace=\"com..mapper.").append(tableName).append("Mapper\">\r\n");
        resultMap.append("    <resultMap id=\"Base").append(tableName).append("\" type=\"").append(obj.getName()).append("\">\r\n");
        insert.append("    <insert id=\"save").append(tableName).append("\" parameterType=\"").append(obj.getName()).append("\">\r\n");
        insert.append("        INSERT INTO ").append(tableName.toLowerCase()).append(" (");
        update.append("    <update id=\"update").append(tableName).append("\" parameterType=\"").append(obj.getName()).append("\">\r\n");
        update.append("        UPDATE ").append(tableName.toLowerCase()).append(" SET ");
        delete.append("    <delete id=\"delete").append(tableName).append("\" parameterType=\"int\">\r\n");
        delete.append("        DELETE FROM ").append(tableName.toLowerCase());
        for(Field f : fields){
            cameCaseColumn = f.getName();
            underScoreCaseColumn = cameCaseColumn;
            for(int i = 0; i < cameCaseColumn.length(); i++)
                if(Character.isUpperCase(cameCaseColumn.charAt(i)))
                    // 将javabean中小驼峰命名变量的“大写字母”转换为“_小写字母”
                    underScoreCaseColumn = cameCaseColumn.substring(0, i) + '_' + cameCaseColumn.substring(i, i+1).toLowerCase() + cameCaseColumn.substring(i+1, cameCaseColumn.length());
            resultMap.append("         ");
            if(firstId){
                resultMap.append("<id column=\"").append(underScoreCaseColumn).append("\" property=\"").append(cameCaseColumn).append("\" jdbcType=\"");
                updateWhere.append("         WHERE ").append(underScoreCaseColumn).append(" = #{").append(cameCaseColumn).append("}\r\n");
                deleteWhere.append(" WHERE ").append(underScoreCaseColumn).append(" = #{").append(cameCaseColumn).append("}\r\n");
                firstId = false;
            } else {
                resultMap.append("<result column=\"").append(underScoreCaseColumn).append("\" property=\"").append(cameCaseColumn).append("\" jdbcType=\"");
                insert.append(underScoreCaseColumn).append(", ");
                insertValues.append("#{").append(cameCaseColumn).append("},");
                update.append(underScoreCaseColumn).append(" = #{").append(cameCaseColumn).append("}, ");
            }
            param = f.getType().getTypeName();
            if(param.equals("java.lang.Integer")){
                resultMap.append("INTEGER\" />\r\n");
            }else{
                // 根据需要自行修改
                resultMap.append("VARCHAR\" />\r\n");
            }
        }
        resultMap.append("    </resultMap>\r\n");
        insert.delete(insert.lastIndexOf(","), insert.length()).append(")\r\n");
        insertValues.delete(insertValues.lastIndexOf(","), insertValues.length());
        insert.append("              VALUES (");
        insert.append(insertValues).append(")\r\n");
        insert.append("    </insert>\r\n");
        update.delete(update.lastIndexOf(","), update.length()).append("\r\n");
        update.append(updateWhere);
        update.append("    </update>\r\n");
        delete.append(deleteWhere);
        delete.append("    </delete>\r\n");
        mapper.append(resultMap).append(insert).append(update).append(delete);
        mapper.append("</mapper>");
        System.out.println(mapper);
    }
}