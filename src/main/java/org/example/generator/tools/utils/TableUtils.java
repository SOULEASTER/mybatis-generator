package org.example.generator.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.generator.tools.constants.TableTypeConstants;
import org.example.generator.tools.entity.ColumnEntity;
import org.example.generator.tools.entity.TableEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表生成工具类
 * 
 * @date 2021/4/29 16:34
 * @since
 * @version
 */
public class TableUtils {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.err.println("请输入Excel的路径：");
//        String excelPath = scanner.nextLine();
        String excelPath = "/Users/w.rajer/Development/IdeaProjects/Demo/mybatis-generator/src/main/resources/table.xlsx";
        TableUtils tableUtils = new TableUtils();
        List<TableEntity> tableEntities = tableUtils.readExcel(excelPath);
        tableUtils.convert2SQL(tableEntities);
    }


    private List<TableEntity> readExcel(String excelPath) {
        List<TableEntity> tableEntities = new ArrayList<>();
        try {
            // String encoding = "GBK";
            File file = new File(excelPath);
            if (file.isFile() && file.exists()) {
                // 判断文件是否存在
                String[] split = file.getName().split("\\.");

                Workbook wb;
                // 根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(file);
                    // 文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(file);
                } else {
                    System.out.println("文件类型错误!");
                    return null;
                }

                int numberOfSheets = wb.getNumberOfSheets();
                for (int i = 0; i < numberOfSheets; i++) {
                    // 开始解析
                    Sheet sheet = wb.getSheetAt(i);

                    // 第一、二行是列名，所以不读，从第三行开始读
                    int firstRowIndex = sheet.getFirstRowNum() + 2;
                    int lastRowIndex = sheet.getLastRowNum();


                    // 解析模板对象List
                    List<ColumnEntity> entities = new ArrayList<>();
                    // 数据库名
                    String databaseName = sheet.getRow(0).getCell(1).getStringCellValue();
                    // 表物理名
                    String physicalTableName = sheet.getRow(0).getCell(3).getStringCellValue();
                    // 表逻辑名
                    String logicalTableName = sheet.getRow(0).getCell(5).getStringCellValue();

                    for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                        ColumnEntity entity = new ColumnEntity();
                        Row row = sheet.getRow(rIndex);
                        if (row != null) {
                            short firstCellNum = row.getFirstCellNum();
                            short lastCellNum = row.getLastCellNum();
                            for (int index = firstCellNum; index < lastCellNum; index++) {
                                Cell cell = row.getCell(index);
                                if (cell == null) {
                                    continue;
                                }

                                String cellValue = "";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        cell.setCellType(CellType.STRING);
                                        cellValue = cell.getStringCellValue();
                                        break;
                                    case STRING:
                                        cellValue = cell.getStringCellValue();
                                        break;
                                    case FORMULA:
                                        cellValue = cell.getCellFormula();
                                        break;
                                    case BOOLEAN:
                                        cellValue = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
                                        break;
                                    case BLANK:
                                    case ERROR:
                                    case _NONE:
                                    default:
                                }

                                switch (index) {
                                    case 0:
                                        // 列物理名
                                        entity.setPhysicalColumnName(cellValue);
                                        break;
                                    case 1:
                                        entity.setLogicalColumnName(cellValue);
                                        break;
                                    case 2:
                                        // 类型
                                        entity.setType(cellValue);
                                        break;
                                    case 3:
                                        // 长度
                                        entity.setLength(cellValue);
                                        break;
                                    case 4:
                                        // 精度
                                        entity.setDecimal(cellValue);
                                        break;
                                    case 5:
                                        // 是否主键
                                        entity.setPrimaryKey(Boolean.parseBoolean(cellValue));
                                        break;
                                    case 6:
                                        // 是否非空
                                        entity.setNotNull(Boolean.parseBoolean(cellValue));
                                        break;
                                    default:

                                }
                            }

//
//                            if (row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue() == null || row
//                                    .getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().isEmpty()) {
//                                continue;
//                            }
//                            // 列逻辑名
//                            entity.setLogicalColumnName(
//                                    row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
//                            // 类型
//                            entity.setType(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
//                            // 长度
//                            entity.setLength(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
//                            // 精度
//                            entity.setDecimal(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
//                            // 是否主键
//                            entity.setPrimaryKey("*"
//                                    .equals(row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
//                            // 是否非空
//                            entity.setNotNull("*"
//                                    .equals(row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
                            // 存入list
                            entities.add(entity);
                        }
                    }

                    TableEntity tableEntity = new TableEntity();
                    tableEntity.setEntities(entities);
                    tableEntity.setDatabaseName(databaseName);
                    tableEntity.setLogicalTableName(logicalTableName);
                    tableEntity.setPhysicalTableName(physicalTableName);

                    tableEntities.add(tableEntity);
                }
            } else {
                System.out.println("找不到指定的文件");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tableEntities;

    }


    private void convert2SQL(List<TableEntity> tableEntityList) {
        for (TableEntity tableEntity : tableEntityList) {
            StringBuffer sql = new StringBuffer();
            sql.append("CREATE TABLE `");
            sql.append(tableEntity.getDatabaseName());
            sql.append("`.`");
            sql.append(tableEntity.getPhysicalTableName());
            sql.append("` (\n\t");
            // CREATE TABLE `databaseName`.`tablePhysicalName` (
            List<ColumnEntity> cellEnties = tableEntity.getEntities();
            // 主键
            String primaryKey = null;
            // 获取主键
            for (ColumnEntity item : cellEnties) {
                // 将pk为true的设为主键
                if (item.isPrimaryKey()) {
                    primaryKey = item.getPhysicalColumnName();
                    break;
                }
            }
            // 循环列
            for (ColumnEntity item : cellEnties) {
                sql.append(" `");
                sql.append(item.getPhysicalColumnName().trim());
                sql.append("` ");

                // 根据NOT NULL 来拼接
                if (item.isNotNull()) {
                    // 如果不允许为空，则拼接NOT NULL
                    //类型
                    String type = item.getType().toLowerCase();
                    //类型转换
                    if(type.contains("varchar")) {
                        type = TableTypeConstants.VARCHAR;
                    }else if (type.contains("number")) {
                        type = TableTypeConstants.INT;
                        //默认长度
                        if(item.getLength() == null||item.getLength().isEmpty()) {
                            item.setLength("11");
                        }
                    }else if(type.contains("char")) {
                        type = TableTypeConstants.CHAR;
                    }


                    // 根据类型选择是否拼接长度
                    if (TableTypeConstants.CHAR.equals(type) || TableTypeConstants.VARCHAR.equals(type)) {
                        // 文本需要拼接长度
                        sql.append(type);
                        sql.append("(");
                        sql.append(item.getLength());
                        sql.append(") CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '");
                    } else if (TableTypeConstants.TEXT.equals(type) || TableTypeConstants.LONG_TEXT.equals(type)) {
                        // 文本不需要拼接长度
                        sql.append(type);
                        sql.append(" CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '");
                    } else if (TableTypeConstants.DATE.equals(type)) {
                        // 时间不需要拼接长度
                        sql.append(type);
                        sql.append(" NOT NULL COMMENT '");
                    } else if (TableTypeConstants.DATE_TIME.equals(type)||TableTypeConstants.TIME_STAMP.equals(type)) {
                        // 时间需要拼接长度 并且长度为0
                        sql.append(type);
                        sql.append("(0) NOT NULL COMMENT '");
                    } else if (TableTypeConstants.INT.equals(type)) {
                        // 数字需要拼接长度
                        sql.append(type);
                        sql.append("(");
                        sql.append(item.getLength());
                        sql.append(")  NOT NULL COMMENT '");
                    }
                } else {
                    // 如果允许为空，则拼接 NULL DEFAULT NULL
                    String type = item.getType().toLowerCase();
                    //类型转换
                    if(type.contains("varchar")) {
                        type = TableTypeConstants.VARCHAR;
                    }else if (type.contains("number")) {
                        type = TableTypeConstants.INT;
                        //默认长度
                        if(item.getLength() == null||item.getLength().isEmpty()) {
                            item.setLength("11");
                        }
                    }else if(type.contains("char")) {
                        type = TableTypeConstants.CHAR;
                    }

                    // 根据类型选择是否拼接长度
                    if (TableTypeConstants.CHAR.equals(type) || TableTypeConstants.VARCHAR.equals(type)) {
                        // 文本需要拼接长度
                        sql.append(type);
                        sql.append("(");
                        sql.append(item.getLength());
                        sql.append(") CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '");
                    } else if (TableTypeConstants.TEXT.equals(type) || TableTypeConstants.LONG_TEXT.equals(type)) {
                        // 文本不需要拼接长度
                        sql.append(type);
                        sql.append(" CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '");
                    } else if (TableTypeConstants.DATE.equals(type)) {
                        // 时间不需要拼接长度
                        sql.append(type);
                        sql.append(" NULL DEFAULT NULL COMMENT '");
                    } else if (TableTypeConstants.DATE_TIME.equals(type)||TableTypeConstants.TIME_STAMP.equals(type)) {
                        // 时间需要拼接长度 并且长度为0
                        sql.append(type);
                        sql.append("(0) NULL DEFAULT NULL COMMENT '");
                    } else if (TableTypeConstants.INT.equals(type) || TableTypeConstants.LONG.equals(type)) {
                        // 数字需要拼接长度
                        sql.append(type);
                        sql.append("(");
                        sql.append(item.getLength());
                        sql.append(")  NULL DEFAULT 0 COMMENT '");
                    }
                }

                // 拼接逻辑列名
                sql.append(item.getLogicalColumnName());
                sql.append("',\n\t");
            }

            // 拼接主键
            sql.append(" PRIMARY KEY (`");
            sql.append(primaryKey);
            sql.append("`) USING BTREE \n) ");
            // 拼接引擎和逻辑表名
            sql.append("ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '");
            sql.append(tableEntity.getLogicalTableName());
            sql.append("'  ROW_FORMAT = Compact;");
            System.err.println(sql);

        }

//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream();
//            fileOutputStream.write();
//            writeTXT("", sql.toString(), tableEntity.getPhysicalTableName() + tableEntity.getLogicalTableName());
//            System.out.println("已导出:" + tableEntity.getPhysicalTableName() + tableEntity.getLogicalTableName() + ".txt!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("导出文件失败");
//        }
    }

    public void writeTXT(String path, String value, String fileName) throws Exception {
        File f = new File(path + fileName + ".txt");
        FileOutputStream fos1 = new FileOutputStream(f);
        OutputStreamWriter dos1 = new OutputStreamWriter(fos1);
        dos1.write(value);
        dos1.close();
    }
}
