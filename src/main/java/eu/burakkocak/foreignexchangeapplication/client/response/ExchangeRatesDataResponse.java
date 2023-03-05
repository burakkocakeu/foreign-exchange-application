package eu.burakkocak.foreignexchangeapplication.client.response;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Sample Response:
 * {
 *   "base": "USD",
 *   "date": "2023-03-02",
 *   "rates": {
 *     "AED": 3.673099,
 *     "AUD": 1.485125,
 *     ...
 *     "USD": 1
 *   },
 *   "success": true,
 *   "timestamp": 1677785463
 * }
 */
@Data
public class ExchangeRatesDataResponse {
    private String base;
    private String date;
    private HashMap<String, BigDecimal> rates;
    private boolean success;
    private Timestamp timestamp;
}
