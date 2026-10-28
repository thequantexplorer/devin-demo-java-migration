package com.acme.billing;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AgingReport {
    /** Buckets: 0 = current, 1 = 1-30, 2 = 31-60, 3 = 61-90, 4 = 90+ days past due. */
    public int[] bucket(List<Invoice> invoices, Date asOf) {
        int[] buckets = new int[5];
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = asOf.toInstant().atZone(zone);
        for (Invoice inv : invoices) {
            long days = ChronoUnit.DAYS.between(inv.getDueAt().toInstant().atZone(zone), now);
            if (days <= 0) buckets[0]++;
            else if (days <= 30) buckets[1]++;
            else if (days <= 60) buckets[2]++;
            else if (days <= 90) buckets[3]++;
            else buckets[4]++;
        }
        return buckets;
    }
}
