package pl.com.soska.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.soska.organizer.enums.ForWhatEnum;
import pl.com.soska.organizer.validator.PastLocalDate;

import java.time.LocalDate;

public class ReportSettings {

    @PastLocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @PastLocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private ForWhatEnum forWhat;

    public ReportSettings() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ForWhatEnum getForWhat() {
        return forWhat;
    }

    public void setForWhat(ForWhatEnum forWhat) {
        this.forWhat = forWhat;
    }
}
