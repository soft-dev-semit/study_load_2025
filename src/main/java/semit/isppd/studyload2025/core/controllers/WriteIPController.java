package semit.isppd.studyload2025.core.controllers;

import semit.isppd.studyload2025.core.entities.CreationMetric;
import semit.isppd.studyload2025.core.entities.Formulary;
import semit.isppd.studyload2025.core.entities.Teacher;
import semit.isppd.studyload2025.core.entities.views.PersonalLoadView;
import semit.isppd.studyload2025.core.general.Dictionary;
import semit.isppd.studyload2025.core.general.utils.cyrToLatin.UkrainianToLatin;
import semit.isppd.studyload2025.core.services.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@Log4j2
public class WriteIPController {
    private final PersonalLoadViewService plsVmService;
    private final CreationMetricService metricService;
    private final TeacherService teacherService;
    private final FormularyService formularyService;
    private double totalSum;
    private static final String TIMES_NEW_ROMAN = "Times New Roman";

    public WriteIPController(PersonalLoadViewService plsVmService, TeacherService teacherService,
                             CreationMetricService metricService, FormularyService formularyService) {
        this.plsVmService = plsVmService;
        this.teacherService = teacherService;
        this.formularyService = formularyService;
        this.metricService = metricService;
    }

    @PostMapping("/uploadIp")
    public String uploadAgain(@RequestParam("file") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path path = Paths.get(Dictionary.INDIVIDUAL_PLANS_FOLDER + fileName);
            try {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                log.error(e);
                throw new Exception(e);
            }
        }
        return "success/ipToDB";
    }

    @RequestMapping("/IP")
    public String createExcel() throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            List<Teacher> teachers = teacherService.listAll();
            XSSFRow row;
            XSSFCell cell;
            int lastVertCellSum;
            int rownum;
            for (Teacher teacher : teachers) {
                if (!teacher.getName().equals("")) {
                    totalSum = 0;
                    List<PersonalLoadView> personalLoadViewList;
                    try (InputStream iS = Files.newInputStream(new File("src/main/resources/IndPlanExample.xlsx").toPath())) {

                        XSSFWorkbookFactory wbF = new XSSFWorkbookFactory();
                        try (XSSFWorkbook workbook = wbF.create(iS)) {

                            XSSFFont defaultFont = workbook.createFont();
                            defaultFont.setFontHeightInPoints((short) 12);
                            defaultFont.setFontName(TIMES_NEW_ROMAN);
                            defaultFont.setBold(false);
                            defaultFont.setItalic(false);

                            XSSFFont font10 = workbook.createFont();
                            font10.setFontHeightInPoints((short) 10);
                            font10.setFontName(TIMES_NEW_ROMAN);
                            font10.setBold(false);
                            font10.setItalic(false);

                            XSSFFont font12 = workbook.createFont();
                            font12.setFontHeightInPoints((short) 12);
                            font12.setFontName(TIMES_NEW_ROMAN);
                            font12.setBold(false);
                            font12.setItalic(false);

                            XSSFFont font12Bold = workbook.createFont();
                            font12Bold.setFontHeightInPoints((short) 12);
                            font12Bold.setFontName(TIMES_NEW_ROMAN);
                            font12Bold.setBold(true);
                            font12Bold.setItalic(false);

                            XSSFFont font12I = workbook.createFont();
                            font12I.setFontHeightInPoints((short) 12);
                            font12I.setFontName(TIMES_NEW_ROMAN);
                            font12I.setBold(false);
                            font12I.setItalic(true);

                            XSSFFont font12BI = workbook.createFont();
                            font12BI.setFontHeightInPoints((short) 12);
                            font12BI.setFontName(TIMES_NEW_ROMAN);
                            font12BI.setBold(true);
                            font12BI.setItalic(true);

                            XSSFFont font14 = workbook.createFont();
                            font14.setFontHeightInPoints((short) 14);
                            font14.setFontName(TIMES_NEW_ROMAN);
                            font14.setBold(false);
                            font14.setItalic(false);

                            XSSFFont font16 = workbook.createFont();
                            font16.setFontHeightInPoints((short) 16);
                            font16.setFontName(TIMES_NEW_ROMAN);
                            font16.setBold(false);
                            font16.setItalic(false);

                            XSSFFont font20 = workbook.createFont();
                            font16.setFontHeightInPoints((short) 20);
                            font16.setFontName(TIMES_NEW_ROMAN);
                            font16.setBold(true);
                            font16.setItalic(false);

                            CellStyle style = workbook.createCellStyle();
                            style.setVerticalAlignment(VerticalAlignment.CENTER);
                            style.setAlignment(HorizontalAlignment.CENTER);
                            style.setFont(font10);
                            style.setWrapText(true);
                            style.setBorderBottom(BorderStyle.THIN);
                            style.setBorderLeft(BorderStyle.THIN);
                            style.setBorderRight(BorderStyle.THIN);
                            style.setBorderTop(BorderStyle.THIN);

                            CellStyle style10 = workbook.createCellStyle();
                            style10.setVerticalAlignment(VerticalAlignment.CENTER);
                            style10.setAlignment(HorizontalAlignment.LEFT);
                            style10.setFont(font10);
                            style10.setWrapText(true);
                            style10.setBorderBottom(BorderStyle.THIN);
                            style10.setBorderLeft(BorderStyle.THIN);
                            style10.setBorderRight(BorderStyle.THIN);
                            style10.setBorderTop(BorderStyle.THIN);

                            CellStyle style12 = workbook.createCellStyle();
                            style12.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12.setAlignment(HorizontalAlignment.LEFT);
                            style12.setFont(font12);
                            style12.setWrapText(true);
                            style12.setBorderBottom(BorderStyle.THIN);
                            style12.setBorderLeft(BorderStyle.THIN);
                            style12.setBorderRight(BorderStyle.THIN);
                            style12.setBorderTop(BorderStyle.THIN);

                            CellStyle style12I = workbook.createCellStyle();
                            style12I.setFont(font12I);
                            style12I.setWrapText(true);


                            CellStyle style12Bold = workbook.createCellStyle();
                            style12Bold.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12Bold.setAlignment(HorizontalAlignment.LEFT);
                            style12Bold.setFont(font12Bold);
                            style12Bold.setWrapText(true);
                            style12Bold.setBorderBottom(BorderStyle.THIN);
                            style12Bold.setBorderLeft(BorderStyle.THIN);
                            style12Bold.setBorderRight(BorderStyle.THIN);
                            style12Bold.setBorderTop(BorderStyle.THIN);

                            CellStyle style12BI = workbook.createCellStyle();
                            style12BI.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12BI.setAlignment(HorizontalAlignment.CENTER);
                            style12BI.setFont(font12Bold);
                            style12BI.setWrapText(true);
                            style12BI.setBorderBottom(BorderStyle.THIN);
                            style12BI.setBorderLeft(BorderStyle.THIN);
                            style12BI.setBorderRight(BorderStyle.THIN);
                            style12BI.setBorderTop(BorderStyle.THIN);

                            CellStyle style12ThickBotTop = workbook.createCellStyle();
                            style12ThickBotTop.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12ThickBotTop.setAlignment(HorizontalAlignment.CENTER);
                            style12ThickBotTop.setFont(font12Bold);
                            style12ThickBotTop.setWrapText(true);
                            style12ThickBotTop.setBorderBottom(BorderStyle.THICK);
                            style12ThickBotTop.setBorderLeft(BorderStyle.THIN);
                            style12ThickBotTop.setBorderRight(BorderStyle.THIN);
                            style12ThickBotTop.setBorderTop(BorderStyle.THICK);

                            CellStyle style12ThickBotTopRight = workbook.createCellStyle();
                            style12ThickBotTopRight.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12ThickBotTopRight.setAlignment(HorizontalAlignment.CENTER);
                            style12ThickBotTopRight.setFont(font12Bold);
                            style12ThickBotTopRight.setWrapText(true);
                            style12ThickBotTopRight.setBorderBottom(BorderStyle.THICK);
                            style12ThickBotTopRight.setBorderLeft(BorderStyle.THIN);
                            style12ThickBotTopRight.setBorderRight(BorderStyle.THICK);
                            style12ThickBotTopRight.setBorderTop(BorderStyle.THICK);

                            CellStyle style12ThickBotTopRal = workbook.createCellStyle();
                            style12ThickBotTopRal.setVerticalAlignment(VerticalAlignment.CENTER);
                            style12ThickBotTopRal.setAlignment(HorizontalAlignment.RIGHT);
                            style12ThickBotTopRal.setFont(font12Bold);
                            style12ThickBotTopRal.setWrapText(true);
                            style12ThickBotTopRal.setBorderBottom(BorderStyle.THICK);
                            style12ThickBotTopRal.setBorderLeft(BorderStyle.THIN);
                            style12ThickBotTopRal.setBorderRight(BorderStyle.THIN);
                            style12ThickBotTopRal.setBorderTop(BorderStyle.THICK);

                            CellStyle style14B = workbook.createCellStyle();
                            style14B.setVerticalAlignment(VerticalAlignment.CENTER);
                            style14B.setAlignment(HorizontalAlignment.CENTER);
                            style14B.setFont(font14);
                            style14B.setWrapText(true);
                            style14B.setBorderBottom(BorderStyle.THIN);
                            style14B.setBorderLeft(BorderStyle.THIN);
                            style14B.setBorderRight(BorderStyle.THIN);
                            style14B.setBorderTop(BorderStyle.THIN);

                            CellStyle style14Bot = workbook.createCellStyle();
                            style14Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                            style14Bot.setAlignment(HorizontalAlignment.CENTER);
                            style14Bot.setFont(font14);
                            style14Bot.setWrapText(true);
                            style14Bot.setBorderBottom(BorderStyle.MEDIUM);
                            style14Bot.setBorderLeft(BorderStyle.THIN);
                            style14Bot.setBorderRight(BorderStyle.THIN);
                            style14Bot.setBorderTop(BorderStyle.THIN);

                            CellStyle style16Bot = workbook.createCellStyle();
                            style16Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                            style16Bot.setAlignment(HorizontalAlignment.CENTER);
                            style16Bot.setFont(font16);
                            style16Bot.setWrapText(true);
                            style16Bot.setBorderBottom(BorderStyle.THIN);

                            CellStyle style20Bot = workbook.createCellStyle();
                            style20Bot.setVerticalAlignment(VerticalAlignment.CENTER);
                            style20Bot.setAlignment(HorizontalAlignment.CENTER);
                            style20Bot.setFont(font20);
                            style20Bot.setWrapText(true);
                            style20Bot.setBorderBottom(BorderStyle.THIN);

                            XSSFCellStyle rowAutoHeightStyle = workbook.createCellStyle();
                            rowAutoHeightStyle.setWrapText(true);
                            if (!plsVmService.getPSLVMData("1", teacher.getName()).isEmpty()
                                    || !plsVmService.getPSLVMData("2", teacher.getName()).isEmpty()) {
                                if (!plsVmService.getPSLVMData("1", teacher.getName()).isEmpty()) {
                                    personalLoadViewList = plsVmService.getPSLVMData("1", teacher.getName());
                                } else {
                                    personalLoadViewList = plsVmService.getPSLVMData("2", teacher.getName());
                                }
                                Formulary formulary = formularyService.listAll().get(0);
                                XSSFSheet sheet = workbook.getSheetAt(0);
                                row = sheet.getRow(8);
                                cell = row.getCell(1);
                                cell.setCellValue(personalLoadViewList.get(0).getInstitute());
                                cell.setCellStyle(style16Bot);
                                row = sheet.getRow(11);
                                cell = row.getCell(1);
                                cell.setCellValue(personalLoadViewList.get(0).getDepNameGc());
                                cell.setCellStyle(style16Bot);
                                row = sheet.getRow(23);
                                cell = row.getCell(0);
                                cell.setCellValue(teacher.getFullName());
                                row = sheet.getRow(33);
                                cell = row.getCell(0);
                                cell.setCellValue(personalLoadViewList.get(0).getYear());
                                cell.setCellStyle(style12BI);
                                cell = row.getCell(1);
                                cell.setCellValue(teacher.getPosada());
                                cell.setCellStyle(style12BI);
                                cell = row.getCell(2);
                                cell.setCellValue(teacher.getNaukStupin());
                                cell.setCellStyle(style12BI);
                                cell = row.getCell(3);
                                cell.setCellValue(teacher.getVchZvana());
                                cell.setCellStyle(style12BI);
                                cell = row.getCell(4);
                                cell.setCellValue(teacher.getStavka());
                                cell.setCellStyle(style12BI);
                                cell = row.getCell(5);
                                cell.setCellValue(teacher.getNote());
                                cell.setCellStyle(style12BI);

                                sheet = workbook.getSheetAt(4);
                                row = sheet.getRow(12);
                                cell = row.getCell(1);
                                String sb = "Звіт про виконання індивідуального плану за " + formulary.getAcademicYear() + " навчальний рік викладача " +
                                        getCellValue(workbook, 23, 0) + " розглянуто " + formulary.getProtocolDate() +
                                        " на засіданні кафедри " + getCellValue(workbook, 11, 1) + " й ухвалено рішення ( протокол № "
                                        + formulary.getProtocolNumber() + "):" +
                                        " ): Індивідуальний план виконано в повному обсязі.";
                                cell.setCellValue(sb);
                                cell.setCellStyle(style10);

                                rownum = 4;
                                sheet = workbook.getSheetAt(2);
                                if (!plsVmService.getPSLVMData("1", teacher.getName()).isEmpty()) {
                                    personalLoadViewList.clear();
                                    personalLoadViewList = plsVmService.getPSLVMData("1", teacher.getName());
                                    rownum = writeHours(cell, rownum, personalLoadViewList, style, rowAutoHeightStyle, sheet);
                                }
                                String[] ends1 = {"КЕРІВНИЦТВО"};
                                rownum = writeKerivnictvo(rownum, teacher, style, style12Bold, sheet, true, ends1);
                                String[] ends2 = {"              Аспіранти, докторанти", "                  Магістри професійні",
                                        "                  Бакалаври", "                  Курсові 5 курс"};
                                rownum = writeKerivnictvo(rownum, teacher, style, style12, sheet, true, ends2);
                                row = sheet.createRow(rownum++);
                                int autumnSum = rownum;
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                                sheet.addMergedRegion(cellRangeAddress);
                                cell = row.createCell(1);
                                cell.setCellValue("РАЗОМ ЗА I СЕМЕСТР");
                                cell.setCellStyle(style12ThickBotTopRal);
                                RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);

                                String[] sums = {"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                                        "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD",
                                        "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO",
                                        "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW"};
                                int cellCount = 7;
                                lastVertCellSum = rownum - 1;
                                for (String sum : sums) {
                                    cell = row.createCell(cellCount++);
                                    cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + "5:" + sum + lastVertCellSum + "),0)");
                                    cell.setCellStyle(style12ThickBotTop);
                                }
                                cell = row.createCell(cellCount++);
                                cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                        "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                        "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                        "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                                cell.setCellStyle(style12ThickBotTop);
                                cell = row.createCell(cellCount);
                                cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                        "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                        "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                        "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);

                                if (!plsVmService.getPSLVMData("2", teacher.getName()).isEmpty()) {
                                    personalLoadViewList.clear();
                                    personalLoadViewList = plsVmService.getPSLVMData("2", teacher.getName());

                                    rownum = writeHours(cell, rownum, personalLoadViewList, style, rowAutoHeightStyle, sheet);
                                }
                                ends2 = new String[]{"              Аспіранти, докторанти", "                  Магістри наукові",
                                        "                  Бакалаври", "                  Курсові 5 курс"};
                                rownum = writeKerivnictvo(rownum, teacher, style, style12Bold, sheet, false, ends1);
                                rownum = writeKerivnictvo(rownum, teacher, style, style12, sheet, false, ends2);
                                row = sheet.createRow(rownum++);
                                int springSum = rownum;
                                cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                                sheet.addMergedRegion(cellRangeAddress);
                                cell = row.createCell(1);
                                cell.setCellValue("РАЗОМ ЗА II СЕМЕСТР");
                                cell.setCellStyle(style12ThickBotTopRal);
                                RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                                cellCount = 7;
                                lastVertCellSum = rownum - 1;
                                for (String sum : sums) {
                                    cell = row.createCell(cellCount++);
                                    cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + (autumnSum + 1) + ":" + sum + lastVertCellSum + "),0)");
                                    cell.setCellStyle(style12ThickBotTop);
                                }
                                cell = row.createCell(cellCount++);
                                cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                        "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                        "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                        "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                                cell.setCellStyle(style12ThickBotTop);
                                cell = row.createCell(cellCount);
                                cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                        "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                        "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                        "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
                                cell.setCellStyle(style12ThickBotTopRight);

                                row = sheet.createRow(rownum++);
                                cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 6);
                                sheet.addMergedRegion(cellRangeAddress);
                                cell = row.createCell(1);
                                cell.setCellValue("УСЬОГО за навчальний рік");
                                cell.setCellStyle(style12ThickBotTopRal);
                                RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), cellRangeAddress, sheet);
                                cellCount = 7;
                                for (String sum : sums) {
                                    cell = row.createCell(cellCount++);
                                    cell.setCellFormula(Dictionary.ROUND_SUM_START_OF_THE_FORMULA + sum + autumnSum + "+" + sum + springSum + "),0)");
                                    cell.setCellStyle(style12ThickBotTop);
                                }
                                cell = row.createCell(cellCount++);
                                cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                                        "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                                        "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                                        "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
                                cell.setCellStyle(style12ThickBotTop);
                                cell = row.createCell(cellCount);
                                cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                                        "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                                        "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                                        "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
                                cell.setCellStyle(style12ThickBotTopRight);

                                row = sheet.createRow(rownum++);
                                for (int c = 0; c < 51; c++) {
                                    row.createCell(c);
                                }
                                row = sheet.createRow(rownum++);
                                cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 1, 9);
                                sheet.addMergedRegion(cellRangeAddress);
                                cell = row.createCell(1);
                                cell.setCellValue("Затвердженно на засіданні кафедри" + formulary.getProtocolDate() + " Протокол № " + formulary.getProtocolNumber());
                                cell.setCellStyle(style12I);
                                for (int c = 10; c < 33; c++) {
                                    row.createCell(c);
                                }
                                cellRangeAddress = new CellRangeAddress(rownum - 1, rownum - 1, 33, 44);
                                sheet.addMergedRegion(cellRangeAddress);
                                cell = row.createCell(33);
                                cell.setCellValue("Затвідувач кафедри " + formulary.getDepartmentHeadFullName());
                                cell.setCellStyle(style12I);

                                row = sheet.createRow(rownum++);
                                for (int c = 0; c < 51; c++) {
                                    row.createCell(c);
                                }
                                row = sheet.createRow(rownum);
                                cell = row.createCell(2);
                                cell.setCellValue(teacher.getName());
                                cell.setCellStyle(style12I);
                                for (int c = 3; c < 51; c++) {
                                    row.createCell(c);
                                }
                                sheet.setFitToPage(true);
                                sheet.getPrintSetup().setFitWidth((short) 1);
                                sheet.getPrintSetup().setFitHeight((short) 0);
                                sheet.getPrintSetup().setLandscape(true);
                                sheet = workbook.getSheetAt(1);
                                row = sheet.getRow(14);
                                cell = row.createCell(3);
                                cell.setCellValue((int) totalSum);
                                cell.setCellStyle(style14B);
                                sheet.setFitToPage(true);
                                sheet.getPrintSetup().setLandscape(true);
                                row = sheet.getRow(18);
                                cell = row.createCell(3);
                                cell.setCellFormula("SUM(D15:D18)");
                                cell.setCellStyle(style14Bot);
                            }
                            File someFile = new File(Dictionary.INDIVIDUAL_PLANS_FOLDER + UkrainianToLatin.generateLat(teacher.getName()) + " ind_plan.xlsx");
                            try (FileOutputStream outputStream = new FileOutputStream(someFile)) {

                                workbook.write(outputStream);

                                teacher.getTeacherHours().setIpFilename(someFile.getName());
                                teacherService.save(teacher);
                            }
                        }
                    }
                }
            }
            Formulary formulary = formularyService.listAll().get(0);
            formulary.setIndPlanZipFilename("someFileName");
            formularyService.save(formulary);

            CreationMetric cr = new CreationMetric();
            cr.setTeacherNumber(teachers.size());
            cr.setTimeToForm((System.currentTimeMillis() - startTime));
            log.info("Number of teachers: [{}]    Creation time: [{}]", cr.getTeacherNumber() + 200, cr.getTimeToForm());
            metricService.save(cr);
        } catch (Exception e) {
            log.error(e);
            throw new Exception(e);
        }

        return "redirect:/";
    }

    private String getCellValue(XSSFWorkbook workbook, int rowNum, int cellNum) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(rowNum);
        return row.getCell(cellNum).getStringCellValue();
    }

    private int writeKerivnictvo(int rownum, Teacher teacher, CellStyle style, CellStyle style12Bold, XSSFSheet sheet, boolean autumn, String[] ends1) {
        XSSFRow row;
        XSSFCell cell;
        for (String end : ends1) {
            row = sheet.createRow(rownum++);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(end);
            cell.setCellStyle(style12Bold);
            for (int l = 3; l < 49; l++) {
                cell = row.createCell(l);
                cell.setCellValue(0);
                cell.setCellStyle(style);
            }
            for (int l = 3; l < 49; l++) {
                switch (end.trim()) {
                    case (Dictionary.ASPIRANTS_DOCTORANTS):
                        if (l == 5) {
                            cell = row.createCell(l);
                            cell.setCellStyle(style);
                        }
                        if (l == 29) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*25");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Магістри професійні"):
                        if (l == 5 && teacher.getTeacherHours().getMasterProfNum() != null
                                && !teacher.getTeacherHours().getMasterProfNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getMasterProfNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 23) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case ("Магістри наукові"):
                        if (l == 5 && teacher.getTeacherHours().getMasterScNum() != null
                                && !teacher.getTeacherHours().getMasterScNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getMasterScNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 23) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*27");
                            cell.setCellStyle(style);
                        }
                        break;
                    case (Dictionary.BACHELORS):
                        if (l == 5 && teacher.getTeacherHours().getBachNum() != null
                                && !teacher.getTeacherHours().getBachNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getBachNum()));
                            cell.setCellStyle(style);
                        }
                        if (autumn) {
                            if (l == 17) {
                                cell = row.createCell(l);
                                cell.setCellFormula("F" + rownum + "*3");
                                cell.setCellStyle(style);
                            }
                        } else {
                            if (l == 23) {
                                cell = row.createCell(l);
                                cell.setCellFormula("F" + rownum + "*14");
                                cell.setCellStyle(style);
                            }
                        }
                        break;
                    case (Dictionary.COURSE_PROJECTS_5_COURSE):
                        if (l == 5 && teacher.getTeacherHours().getFifthCourseNum() != null
                                && !teacher.getTeacherHours().getFifthCourseNum().isEmpty()) {
                            cell = row.createCell(l);
                            cell.setCellValue((int) Double.parseDouble(teacher.getTeacherHours().getFifthCourseNum()));
                            cell.setCellStyle(style);
                        }
                        if (l == 17) {
                            cell = row.createCell(l);
                            cell.setCellFormula("F" + rownum + "*3");
                            cell.setCellStyle(style);
                        }
                        break;
                    default:
                        cell = row.createCell(l);
                        cell.setCellStyle(style);
                        break;
                }
            }
            cell = row.createCell(49);
            cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                    "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                    "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                    "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
            cell.setCellStyle(style);
            cell = row.createCell(50);
            cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                    "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                    "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                    "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
            cell.setCellStyle(style);
        }
        return rownum;
    }

    private int writeHours(XSSFCell cell, int rownum, List<PersonalLoadView> personalLoadViewList, CellStyle style, XSSFCellStyle rowAutoHeightStyle, XSSFSheet sheet) {
        XSSFRow row;
        for (PersonalLoadView personalLoadView : personalLoadViewList) {
            row = sheet.createRow(rownum++);
            row.createCell(0);
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(personalLoadView.getDisciplineName());
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellStyle(style);


            cell = row.createCell(4);
            if (!personalLoadView.getCourse().isEmpty()) {
                cell.setCellValue((int) Double.parseDouble(personalLoadView.getCourse()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(5);
            if (!personalLoadView.getStudentsNumber().isEmpty()) {
                cell.setCellValue((int) Double.parseDouble(personalLoadView.getStudentsNumber()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue(personalLoadView.getGroupNames());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            if (!personalLoadView.getLecHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getLecHours()));
                totalSum += Double.parseDouble(personalLoadView.getLecHours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(9);
            if (!personalLoadView.getConsultHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getConsultHours()));
                totalSum += Double.parseDouble(personalLoadView.getConsultHours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(11);
            if (!personalLoadView.getLabHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getLabHours()));
                totalSum += Double.parseDouble(personalLoadView.getLabHours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(13);
            if (!personalLoadView.getPractHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getPractHours()));
                totalSum += Double.parseDouble(personalLoadView.getPractHours());
            }
            cell.setCellStyle(style);

            cell = row.createCell(15);
            if (!personalLoadView.getIndTaskHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getIndTaskHours()));
                totalSum += Double.parseDouble(personalLoadView.getIndTaskHours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(17);
            if (!personalLoadView.getCpHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getCpHours()));
                totalSum += Double.parseDouble(personalLoadView.getCpHours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(19);
            if (!personalLoadView.getZalikHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getZalikHours()));
                totalSum += Double.parseDouble(personalLoadView.getZalikHours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(21);
            if (!personalLoadView.getExamHours().isEmpty()) {
                cell.setCellValue((int) Math.round(Double.parseDouble(personalLoadView.getExamHours())));
                totalSum += Math.round(Double.parseDouble(personalLoadView.getExamHours()));
            }
            cell.setCellStyle(style);
            cell = row.createCell(23);
            if (!personalLoadView.getDiplomaHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getDiplomaHours()));
                totalSum += Double.parseDouble(personalLoadView.getDiplomaHours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(25);
            if (!personalLoadView.getDecCell().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getDecCell()));
                totalSum += Double.parseDouble(personalLoadView.getDecCell());
            }
            cell.setCellStyle(style);
            cell = row.createCell(27);
            if (!personalLoadView.getNdrs().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getNdrs()));
                totalSum += Double.parseDouble(personalLoadView.getNdrs());
            }
            cell.setCellStyle(style);
            cell = row.createCell(29);
            if (!personalLoadView.getAspirantHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getAspirantHours()));
                totalSum += Double.parseDouble(personalLoadView.getAspirantHours());
            }
            cell.setCellStyle(style);
            cell = row.createCell(31);
            if (!personalLoadView.getPractice().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getPractice()));
                totalSum += Double.parseDouble(personalLoadView.getPractice());
            }
            cell.setCellStyle(style);
            cell = row.createCell(33);
            cell.setCellStyle(style);
            cell = row.createCell(35);
            if (!personalLoadView.getOtherFormsHours().isEmpty()) {
                cell.setCellValue(Double.parseDouble(personalLoadView.getOtherFormsHours()));
                totalSum += Double.parseDouble(personalLoadView.getOtherFormsHours());
            }
            cell.setCellStyle(style);

            for (int c = 8; c < 37; c += 2) {
                cell = row.createCell(c);
                cell.setCellStyle(style);
            }
            for (int c = 37; c < 49; c++) {
                cell = row.createCell(c);
                cell.setCellStyle(style);
            }

            cell = row.createCell(49);
            cell.setCellFormula("H" + rownum + "+J" + rownum + "+L" + rownum + "+N" + rownum + "+P" + rownum +
                    "+R" + rownum + "+T" + rownum + "+V" + rownum + "+X" + rownum + "+Z" + rownum +
                    "+AB" + rownum + "+AD" + rownum + "+AF" + rownum + "+AH" + rownum + "+AL" + rownum + "+AJ" + rownum +
                    "+AN" + rownum + "+AP" + rownum + "+AR" + rownum + "+AT" + rownum + "+AV" + rownum);
            cell.setCellStyle(style);
            cell = row.createCell(50);
            cell.setCellFormula("I" + rownum + "+K" + rownum + "+M" + rownum + "+O" + rownum + "+Q" + rownum +
                    "+S" + rownum + "+U" + rownum + "+W" + rownum + "+Y" + rownum + "+AA" + rownum + "+AC" + rownum +
                    "+AE" + rownum + "+AG" + rownum + "+AI" + rownum + "+AK" + rownum + "+AM" + rownum + "+AO" + rownum +
                    "+AQ" + rownum + "+AS" + rownum + "+AU" + rownum + "+AW" + rownum);
            cell.setCellStyle(style);
            row.setRowStyle(rowAutoHeightStyle);
        }
        return rownum;
    }
}
