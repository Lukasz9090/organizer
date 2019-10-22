package pl.com.organizer.controllermvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.com.organizer.exception.IncorrectDateException;
import pl.com.organizer.model.ReportSettings;
import pl.com.organizer.service.ReportGeneratorService;

import java.security.Principal;

@Controller
public class ReportGeneratorController {

    private final ReportGeneratorService reportGeneratorService;

    public ReportGeneratorController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @GetMapping("/user/report")
    public String createReport(Model model) {
        ReportSettings reportSettings = new ReportSettings();
        model.addAttribute(reportSettings);
        return "report-generator-page";
    }

    @PostMapping("/user/report")
    @ResponseBody
    public ResponseEntity<byte[]> setDataToCreateReport(@ModelAttribute ReportSettings reportSettings,
                                          Principal principal) {
        byte[] pdfContents = reportGeneratorService.generatePdfReport(principal.getName(), reportSettings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline; filename=" + "Expenses report.pdf");
        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }

    @ExceptionHandler(IncorrectDateException.class)
    public String exceptionHandlerForIncorrectDate (Model model){
        model.addAttribute("message", "You have selected the wrong dates");
        return "report-error-page";
    }

    @ExceptionHandler(NullPointerException.class)
    public String exceptionHandlerForNullDate (Model model){
        model.addAttribute("message", "You have to select dates");
        return "report-error-page";
    }
}
