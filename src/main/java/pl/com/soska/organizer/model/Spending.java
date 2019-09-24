package pl.com.soska.organizer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.soska.organizer.enums.ForWhatEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
public class Spending {

    @Id
    private String id;

    private BigDecimal amount;

    private ForWhatEnum forWhat;

    private LocalDate date;

    public Spending() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
