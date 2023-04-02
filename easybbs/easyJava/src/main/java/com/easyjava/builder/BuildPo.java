package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 自动生存 PO 文件
 */
public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo) {
        System.out.println("");
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out,"utf8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();


            bw.write("import java.io.Serializable;");
            bw.newLine();

            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()){
                bw.write("import java.util.Date;");
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
            }

            bw.newLine();
            bw.newLine();

            // 构造类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());

            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();

            for (FieldInfo field:tableInfo.getFieldList()){
                BuildComment.createFieldComment(bw,field.getComment());
                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }
            bw.write("}");
            bw.flush();

        } catch (Exception e) {
            logger.error("创建 po 失败！",e);

        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File file = new File(folder, tableInfo.getBeanName() + ".java");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
