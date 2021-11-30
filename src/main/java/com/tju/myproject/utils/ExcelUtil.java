package com.tju.myproject.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExcelUtil {
    /**
     * 用户信息导出类
     * @param response 响应
     * @param fileName 文件名
     * @param columnList 每列的标题名
     * @param dataList 导出的数据
     */
    public static void uploadExcelAboutUser(String fileName,List<String> columnList,List<List<String>> dataList){
        //声明输出流
        FileOutputStream output = null;
        //设置响应头
        //setResponseHeader(response, fileName);
        try {
            //获取输出流
            output = new FileOutputStream(fileName);
            //内存中保留1000条数据，以免内存溢出，其余写入硬盘
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);
            //获取该工作区的第一个sheet
            Sheet sheet1 = wb.createSheet("sheet1");
            int excelRow = 0;
            //创建标题行
            Row titleRow = sheet1.createRow(excelRow++);
            for (int i = 0; i < columnList.size(); i++) {
                //创建该行下的每一列，并写入标题数据
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columnList.get(i));
            }
            //设置内容行
            if (dataList != null && dataList.size() > 0) {
                //序号是从1开始的
                //int count =0;
                //外层for循环创建行
                for (int i = 0; i < dataList.size(); i++) {
                    Row dataRow = sheet1.createRow(excelRow++);
                    //内层for循环创建每行对应的列，并赋值
                    for (int j = 0; j < dataList.get(0).size(); j++) {//由于多了一列序号列所以内层循环从-1开始
                        Cell cell = dataRow.createCell(j);
                        cell.setCellValue(dataList.get(i).get(j));
                            /*if(j==0){//第一列是序号列，不是在数据库中读取的数据，因此手动递增赋值
                                //cell.setCellValue(count++);
                            }else{//其余列是数据列，将数据库中读取到的数据依次赋值
                                cell.setCellValue(dataList.get(i).get(j));
                            }*/
                    }
                }
            }/*

            //准备将Excel的输出流通过response输出到页面下载
            //八进制输出流
            response.setContentType("application/octet-stream");

            //这后面可以设置导出Excel的名称，此例中名为student.xls
            response.setHeader("Content-disposition", "attachment;filename=employee.xls");

            //刷新缓冲
            response.flushBuffer();*/

            //workbook将Excel保存到指定路径
            wb.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}