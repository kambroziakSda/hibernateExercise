package sda.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvoiceData {

    InvoiceData() {
    }

    public InvoiceData(String nip) {
        this.nip = nip;
    }

    @Id
    private String nip;

    @Override
    public String toString() {
        return "InvoiceData{" +
                "nip='" + nip + '\'' +
                '}';
    }
}
