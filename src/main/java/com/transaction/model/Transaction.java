package com.transaction.model;


import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "desc")
    private String desc;

    @Column(name = "amount")
    private float amount;

    public Transaction() {
    }

    public Transaction(String desc, float amount, LocalDate date) {
        this.desc = desc;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "id=" + id + ", date=" + date + ",  amount=" + amount + ", desc=" + desc + ",]";
    }

}