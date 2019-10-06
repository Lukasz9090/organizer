package pl.com.organizer.controllermvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.com.organizer.model.ReportSettings;
import pl.com.organizer.service.ReportGeneratorService;

import java.security.Principal;

@Controller
public class ReportGeneratorController {

    private final ReportGeneratorService reportGeneratorService;

    public ReportGeneratorController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @GetMapping("/report")
    public String createReport(Model model) {
        ReportSettings reportSettings = new ReportSettings();
        model.addAttribute(reportSettings);
        return "report-generator-page";
    }

    @PostMapping("/report/created")
    @ResponseBody
    public ResponseEntity<byte[]> addDate(@ModelAttribute ReportSettings reportSettings,
                                          Principal principal) {
        byte[] pdfContents = reportGeneratorService.generatePdfReport(principal.getName(), reportSettings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline; filename=" + "Spending report.pdf");
        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
}
