package com.acme.billing;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/** Net-N payment terms. Weekends roll forward to Monday. */
public class DueDateCalculator {
    public Date dueDate(Date issuedAt, int netDays) {
        ZonedDateTime due = issuedAt.toInstant().atZone(ZoneOffset.UTC).plusDays(netDays);
        DayOfWeek dow = due.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY) due = due.plusDays(2);
        if (dow == DayOfWeek.SUNDAY) due = due.plusDays(1);
        return Date.from(due.toInstant());
    }

    public boolean isOverdue(Date dueAt, Date now) {
        return now.after(dueAt);
    }
}
