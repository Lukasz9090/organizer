package pl.com.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.validator.CorrectAmount;
import pl.com.organizer.validator.PastLocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    @DecimalMin(value = "0", message = "Amount should be greater than 0")
    @NotNull(message = "Please enter the amount")
    @CorrectAmount
    private String amount;

    @NotNull
    private ForWhatEnum forWhat;

    @NotNull(message = "Please select a date")
    @PastLocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Expense() {
    }

    public Expense(String amount, ForWhatEnum forWhat, LocalDate date) {
        this.amount = amount;
        this.forWhat = forWhat;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ForWhatEnum getForWhat() {
        return forWhat;
    }

    public void setForWhat(ForWhatEnum forWhat) {
        this.forWhat = forWhat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "amount=" + amount +
                ", forWhat=" + forWhat +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense spending = (Expense) o;
        return Objects.equals(date, spending.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
