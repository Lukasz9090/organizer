package pl.com.soska.organizer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
public class Spending {

    @Id
    private String id;

    private BigDecimal amount;

    private ForWhat forWhat;

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

    public ForWhat getForWhat() {
        return forWhat;
    }

    public void setForWhat(ForWhat forWhat) {
        this.forWhat = forWhat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
