package pl.com.soska.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.soska.organizer.enums.ForWhatEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Spending {

    private BigDecimal amount;
    private ForWhatEnum forWhat;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Spending() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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
}
