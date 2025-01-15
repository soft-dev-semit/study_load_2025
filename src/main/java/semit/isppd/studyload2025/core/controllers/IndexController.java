package semit.isppd.studyload2025.core.controllers;


import semit.isppd.studyload2025.core.entities.Formulary;
import semit.isppd.studyload2025.core.entities.Teacher;
import semit.isppd.studyload2025.core.general.Dictionary;
import semit.isppd.studyload2025.core.services.DisciplineService;
import semit.isppd.studyload2025.core.services.FormularyService;
import semit.isppd.studyload2025.core.services.StudyloadRowService;
import semit.isppd.studyload2025.core.services.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static semit.isppd.studyload2025.core.general.utils.email.Sender.rfc5987_encode;

@Log4j2
@Controller
public class IndexController {
    private final StudyloadRowService studyloadRowService;
    private final DisciplineService disciplineService;
    private final TeacherService teacherService;
    private final FormularyService formularyService;


    public IndexController(StudyloadRowService studyloadRowService, DisciplineService disciplineService,
                           TeacherService teacherService, FormularyService formularyService) {
        this.studyloadRowService = studyloadRowService;
        this.disciplineService = disciplineService;
        this.teacherService = teacherService;
        this.formularyService = formularyService;
    }

    @GetMapping(path = "/")
    public String viewIndexPage(Model model) {
        String easfn = "";
        String pslfn = "";
        String ipzipfn = "";
        try {
            easfn = formularyService.listAll().get(0).getEasFilename();
            pslfn = formularyService.listAll().get(0).getPslFilename();
            ipzipfn = formularyService.listAll().get(0).getIndPlanZipFilename();
        } catch (IndexOutOfBoundsException ex) {
            log.info("no files yet");
        }
        model.addAttribute("eas", easfn != null && !easfn.isEmpty());
        model.addAttribute("psl", new File(Dictionary.PERSONAL_STUDYLOAD_FOLDER + pslfn).isFile());
        model.addAttribute("ip", ipzipfn != null && !ipzipfn.isEmpty());
        return "index";
    }

    @GetMapping(path = "/help")
    public String viewHelpPage() {
        return "help";
    }

    @GetMapping(path = "/delete")
    public String viewDeletePage() {
        return "confirmation/deleteAllConf";
    }

    @GetMapping(path = "/deleteWithoutTeachers")
    public String viewDeleteWOprofsPage() {
        return "confirmation/deleteWithoutTeachersConf";
    }

    @GetMapping(path = "/deleteAll")
    public String deleteAll(Model model) {
        studyloadRowService.deleteAll();
        disciplineService.deleteAll();
        teacherService.deleteAll();
        formularyService.deleteAll();
        return "success/deleteAllSuc";
    }


    @GetMapping(path = "/deleteWithoutTeachersMethod")
    public String deleteWithoutTeachersMethod() {
        studyloadRowService.deleteAll();
        disciplineService.deleteAll();
        formularyService.deleteAll();
        return "success/deleteWithouTeachersSuc";
    }

    @GetMapping("/downloadEAS")
    public ResponseEntity downloadEAS() throws IOException {
        Formulary formulary = formularyService.listAll().get(0);
        File file = new File(Dictionary.ED_AS_ST_FOLDER + formulary.getEasFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(formulary.getEasFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping("/downloadPSL")
    public ResponseEntity downloadPSL() throws IOException {

        Formulary formulary = formularyService.listAll().get(0);
        File file = new File(Dictionary.PERSONAL_STUDYLOAD_FOLDER + formulary.getPslFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(formulary.getPslFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping("/downloadSummary")
    public ResponseEntity downloadSummary() throws IOException {

        File file = new File(Dictionary.PERSONAL_STUDYLOAD_FOLDER + Dictionary.STUDYLOAD_SUMMARY_FILENAME_XLSX);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + Dictionary.STUDYLOAD_SUMMARY_FILENAME_XLSX + "\"")
                .body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping(value = "/downloadIp", produces = "application/zip")
    public ResponseEntity downloadIpZip() throws IOException {
        File zipFile = new File(Dictionary.INDIVIDUAL_PLANS_FOLDER + "Ind_plans.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile)) {
            try (ZipOutputStream zipOS = new ZipOutputStream(fos)) {
                List<String> fileNames = new ArrayList<>();
                List<Teacher> teachers = teacherService.listAll();
                for (Teacher teacher : teachers) {
                    fileNames.add(teacher.getTeacherHours().getIpFilename());
                }
                for (String fileName : fileNames) {
                    File someFile = new File(Dictionary.INDIVIDUAL_PLANS_FOLDER + fileName);
                    writeToZipFile(Dictionary.INDIVIDUAL_PLANS_FOLDER + someFile.getName(), zipOS);
                }
            }
        }
        Formulary formulary = formularyService.listAll().get(0);
        formulary.setIndPlanZipFilename(zipFile.getName());
        formularyService.save(formulary);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, Dictionary.ATTACHMENT_FILENAME + rfc5987_encode(formularyService.listAll().get(0).getIndPlanZipFilename()) + "\"")
                .body(FileUtils.readFileToByteArray(zipFile));
    }

    public static void writeToZipFile(String path, ZipOutputStream zipStream) {
        File aFile = new File(path);
        try (FileInputStream fis = new FileInputStream(aFile)) {
            ZipEntry zipEntry = new ZipEntry(path);
            zipStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipStream.write(bytes, 0, length);
            }

            zipStream.closeEntry();
        } catch (IOException e) {
            log.error(e);
        }
    }
}
