package com.acme.billing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Encodes opaque payment references passed to the legacy PSP bridge. */
public class PaymentTokenCodec {
    public String encode(String customerId, String invoiceId) {
        String raw = customerId + ":" + invoiceId;
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public String[] decode(String token) {
        try {
            // Tokens from the old sun.misc.BASE64Encoder were line-wrapped every 76 chars.
            byte[] bytes = Base64.getDecoder().decode(token.replaceAll("\\s", ""));
            return new String(bytes, StandardCharsets.UTF_8).split(":", 2);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("bad token", e);
        }
    }
}
