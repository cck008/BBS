package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws Exception {
        /**
         * @Description:
         * @date：2023/2/5
         */
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description：" + Constants.AUTHOR_COMMENT);
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * @author：cck");
        bw.newLine();
//        bw.write(" * @date：" + DateUtils.format(new Date(), DateUtils._YYYMMDD));
//        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {

        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + fieldComment);
        bw.newLine();
        bw.write("\t */");
        bw.newLine();

    }

    public static void createMethodComment(BufferedWriter bw) {

    }
}
