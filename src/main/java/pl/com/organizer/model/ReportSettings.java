package pl.com.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.validator.PastLocalDate;

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

    public ReportSettings(LocalDate startDate, LocalDate endDate, ForWhatEnum forWhat) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.forWhat = forWhat;
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
