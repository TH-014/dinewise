package com.example.dinewise.controller;

import com.example.dinewise.repo.DueRepository;
import com.example.dinewise.model.Due;

import java.util.Map;

/**
 * This class handles the Response parameters redirected from payment success page.
 * Validates those parameters fetched from payment page response and returns true for successful transaction
 * and false otherwise.
 */
public class TransactionResponseValidator {
    String storeId = "testbox";
    String storePassword = "qwerty";
    DueRepository dueRepository;
    /**
     *
     * @param request
     * @return
     * @throws Exception
     * Send Received params from your success resoponse (POST ) in this Map</>
     */
    public boolean receiveSuccessResponse(Map<String, String> request) throws Exception {

        String trxId = request.get("tran_id");
        /**
         *Get your AMOUNT and Currency FROM DB to initiate this Transaction
         */
        Due due = dueRepository.findByStdId(request.get("cus_id")).orElseThrow(() -> new Exception("Due not found for student ID: " + request.get("cus_id")));
        if (due == null) {
            throw new Exception("Due not found for student ID: " + request.get("cus_id"));
        }
        String amount = String.valueOf(due.getTotalDue()); // bring data from DB
        String currency = "BDT";
        // Set your store Id and store password and define TestMode
        SSLCommerz sslcz = new SSLCommerz(storeId, storePassword, true);

        /**
         * If following order validation returns true, then process transaction as success.
         * if this following validation returns false , then query status if failed of canceled.
         *      Check request.get("status") for this purpose
         */
        return sslcz.orderValidate(trxId, amount, currency, request);

    }
}
