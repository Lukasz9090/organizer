package pl.com.soska.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.soska.organizer.enums.ForWhatEnum;
import pl.com.soska.organizer.validator.AmountValidator;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Spending {


    @DecimalMin(value = "0", message = "Amount should be greater than 0")
    @NotNull(message = "Please enter the amount")
    @AmountValidator
    private String amount;
    @NotNull
    private ForWhatEnum forWhat;
    @NotNull(message = "Please select a date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Spending() {
    }

    public Spending(String amount, ForWhatEnum forWhat, LocalDate date) {
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
        return "Spending{" +
                "amount=" + amount +
                ", forWhat=" + forWhat +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spending spending = (Spending) o;
        return Objects.equals(date, spending.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
