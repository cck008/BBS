package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.builder.BuildPo;
import com.easyjava.builder.BuilderTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
       List<TableInfo> tableInfoList = BuilderTable.getTables();
       for (TableInfo tableInfo : tableInfoList){
           BuildPo.execute(tableInfo);
       }
    }
}
