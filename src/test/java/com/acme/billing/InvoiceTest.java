package com.acme.billing;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceTest {
    private SimpleDateFormat iso;

    @BeforeEach void setUp() {
        iso = new SimpleDateFormat("yyyy-MM-dd");
        iso.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Date d(String s) throws Exception { return iso.parse(s); }

    private Invoice sample() throws Exception {
        Invoice inv = new Invoice("INV-1", "CUST-9", "USD", d("2025-01-31"), d("2025-03-03"));
        inv.addItem(new LineItem("SKU-1", 2, Money.of("19.99", "USD")));
        inv.addItem(new LineItem("SKU-2", 1, Money.of("5.00", "USD")));
        return inv;
    }

    @Test void subtotal() throws Exception {
        assertEquals(Money.of("44.98", "USD"), sample().subtotal());
    }

    @Test void taxByRegion() throws Exception {
        TaxCalculator tax = new TaxCalculator();
        assertEquals(Money.of("3.26", "USD"), tax.taxFor(sample(), "US-CA"));
        assertEquals(Money.of("0.00", "USD"), tax.taxFor(sample(), "US-OR"));
    }

    @Test void dueDateRollsWeekendToMonday() throws Exception {
        DueDateCalculator due = new DueDateCalculator();
        // 2025-01-31 + 30 = 2025-03-02 (Sunday) -> Monday 2025-03-03
        assertEquals(d("2025-03-03"), due.dueDate(d("2025-01-31"), 30));
        assertTrue(due.isOverdue(d("2025-03-03"), d("2025-03-04")));
        assertFalse(due.isOverdue(d("2025-03-03"), d("2025-03-02")));
    }

    @Test void csvRow() throws Exception {
        String row = new InvoiceFormatter().toCsvRow(sample(), Money.of("3.26", "USD"));
        assertEquals("INV-1,CUST-9,2025-01-31,2025-03-03,USD,44.98,3.26", row);
    }

    @Test void tokenRoundTrip() {
        PaymentTokenCodec codec = new PaymentTokenCodec();
        String[] parts = codec.decode(codec.encode("CUST-9", "INV-1"));
        assertEquals("CUST-9", parts[0]);
        assertEquals("INV-1", parts[1]);
    }

    @Test void tokenWireFormatUnchanged() {
        // Value produced by sun.misc.BASE64Encoder on Java 8.
        assertEquals("Q1VTVC05OklOVi0x", new PaymentTokenCodec().encode("CUST-9", "INV-1"));
    }

    @Test void decodesLegacyLineWrappedToken() {
        String id = "CUST-0123456789012345678901234567890123456789";
        String legacy = "Q1VTVC0wMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5OklOVi0wMTIzNDU2\n"
            + "Nzg5";
        assertArrayEquals(new String[] {id, "INV-0123456789"}, new PaymentTokenCodec().decode(legacy));
    }

    @Test void rejectsMalformedToken() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentTokenCodec().decode("@@@@Q1VT"));
    }

    @Test void xmlEnvelope() throws Exception {
        assertEquals("<invoice id=\"INV-1\" customer=\"CUST-9\" checksum=\"494E562D31\"><subtotal>44.98</subtotal></invoice>",
            new InvoiceXml().envelope(sample()));
    }

    @Test void agingBuckets() throws Exception {
        Invoice a = new Invoice("A", "C", "USD", d("2025-01-01"), d("2025-02-01"));
        Invoice b = new Invoice("B", "C", "USD", d("2025-01-01"), d("2025-01-01"));
        int[] buckets = new AgingReport().bucket(Arrays.asList(a, b), d("2025-02-15"));
        assertEquals(0, buckets[0]);
        assertEquals(1, buckets[1]);
        assertEquals(1, buckets[2]);
    }

    @Test void billingRunIsDeterministic() {
        assertEquals(BillingRun.run(50, 1L), BillingRun.run(50, 1L));
        assertEquals(50, BillingRun.run(50, 1L).size());
    }
}
