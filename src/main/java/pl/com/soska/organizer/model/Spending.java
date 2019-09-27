package pl.com.soska.organizer.model;

import org.springframework.format.annotation.DateTimeFormat;
import pl.com.soska.organizer.enums.ForWhatEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Spending {

    private BigDecimal amount;
    private ForWhatEnum forWhat;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Spending() {
    }

    public Spending(BigDecimal amount, ForWhatEnum forWhat, LocalDate date) {
        this.amount = amount;
        this.forWhat = forWhat;
        this.date = date;
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
