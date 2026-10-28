package com.acme.billing;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class InvoiceFormatter {
    // Immutable and thread-safe, so it can be shared by the batch exporter.
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);

    public String toCsvRow(Invoice invoice, Money tax) {
        StringBuilder sb = new StringBuilder();
        sb.append(invoice.getId()).append(',');
        sb.append(invoice.getCustomerId()).append(',');
        sb.append(format(invoice.getIssuedAt())).append(',');
        sb.append(format(invoice.getDueAt())).append(',');
        sb.append(invoice.getCurrency()).append(',');
        sb.append(invoice.subtotal().getAmount().toPlainString()).append(',');
        sb.append(tax.getAmount().toPlainString());
        return sb.toString();
    }

    private static String format(Date date) {
        return ISO.format(date.toInstant());
    }
}
