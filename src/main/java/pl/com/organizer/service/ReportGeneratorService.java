package pl.com.organizer.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.Spending;
import pl.com.organizer.model.User;
import pl.com.organizer.exception.IncorrectDateException;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.ReportSettings;
import pl.com.organizer.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorService {

    private final UserRepository userRepository;
    private final SpringTemplateEngine springTemplateEngine;

    public ReportGeneratorService(UserRepository userRepository,
                                  SpringTemplateEngine springTemplateEngine) {
        this.userRepository = userRepository;
        this.springTemplateEngine = springTemplateEngine;
    }

    public byte[] generatePdfReport(String username, ReportSettings reportSettings) {
        Context ctx = addDataToPdfFile(username,
                reportSettings.getStartDate(),
                reportSettings.getEndDate(),
                reportSettings.getForWhat());

        String processedHtml = springTemplateEngine.process("report-generator-pdf-template", ctx);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        try {
            builder
                    .useFastMode()
                    .withHtmlContent(processedHtml, "")
                    .toStream(out)
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            byte[] pdfContents = out.toByteArray();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pdfContents;
        }
    }

    private Context addDataToPdfFile(String username, LocalDate fromDate, LocalDate toDate, ForWhatEnum forWhatEnum) {
        Context ctx = new Context();
        List<Spending> filteredList = filteredListWithData(username, fromDate, toDate, forWhatEnum);
        BigDecimal amountSum = calculateSumOfAmount(filteredList);
        LocalDate firstDateInListWithFilteredData = getFirstDate(filteredList);
        LocalDate lastDateInListWithFilteredData = getLastDate(filteredList);

        ctx.setVariable("currentDate", LocalDate.now());
        ctx.setVariable("startDate", firstDateInListWithFilteredData);
        ctx.setVariable("endDate", lastDateInListWithFilteredData);
        ctx.setVariable("listWithData", filteredList);
        ctx.setVariable("forWhat", forWhatEnum);
        ctx.setVariable("sum", amountSum);
        return ctx;
    }

    private List<Spending> filteredListWithData(String username, LocalDate fromDate, LocalDate toDate, ForWhatEnum forWhatEnum) {
        User user = getUserByEmail(username);
        List<Spending> listWithAllSpending = user.getSpending();

        return filterList(listWithAllSpending, fromDate, toDate, forWhatEnum);
    }

    private List<Spending> filterList(List<Spending> listWithAllSpending,
                                      LocalDate fromDate,
                                      LocalDate toDate,
                                      ForWhatEnum forWhatEnum) {
        return listWithAllSpending.stream()
                .filter(spending -> filterListByDate(spending, fromDate, toDate))
                .filter(spending -> filterListBySpendingType(spending, forWhatEnum))
                .sorted(Comparator.comparing(Spending::getDate))
                .collect(Collectors.toList());
    }

    private User getUserByEmail (String username){
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private boolean filterListByDate(Spending spending,
                                     LocalDate fromDate,
                                     LocalDate toDate) {
        return (spending.getDate().equals(fromDate) ||
                spending.getDate().isAfter(fromDate) && spending.getDate().isBefore(toDate) ||
                spending.getDate().equals(toDate));
    }

    private boolean filterListBySpendingType(Spending spending, ForWhatEnum forWhat) {
        return forWhat == ForWhatEnum.ALL || (spending.getForWhat() == forWhat);
    }

    private BigDecimal calculateSumOfAmount(List<Spending> filteredList){
        BigDecimal sum = new BigDecimal("0");
            for (Spending spending : filteredList){
                sum = sum.add(new BigDecimal(spending.getAmount()));
            }
            return sum;
    }

    private LocalDate getFirstDate(List<Spending> listWithFilteredData) {
        if (listWithFilteredData.isEmpty()) {
            throw new IncorrectDateException("You have selected the wrong dates");
        }
        return listWithFilteredData.get(0).getDate();
    }

    private LocalDate getLastDate(List<Spending> listWithFilteredData) {
        if (listWithFilteredData.isEmpty()) {
            throw new IncorrectDateException("You have selected the wrong dates");
        }
        return listWithFilteredData.get(listWithFilteredData.size() - 1).getDate();
    }
}