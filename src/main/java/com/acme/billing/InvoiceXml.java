package com.acme.billing;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/** Minimal XML envelope for the ERP export. */
public class InvoiceXml {
    private static final HexFormat HEX = HexFormat.of().withUpperCase();

    public String envelope(Invoice invoice) {
        String checksum = HEX.formatHex(invoice.getId().getBytes(StandardCharsets.UTF_8));
        return "<invoice id=\"" + invoice.getId() + "\" customer=\"" + invoice.getCustomerId()
            + "\" checksum=\"" + checksum + "\"><subtotal>" + invoice.subtotal().getAmount().toPlainString()
            + "</subtotal></invoice>";
    }
}
