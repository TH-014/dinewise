package com.example.dinewise.controller;


import com.example.dinewise.controller.Utility.ParameterBuilder;

import java.util.Map;

/**
 * This class initiates a transaction request to SSL Commerz
 * required parameters to hit SSL Commerz payment page are constructed in a Map of String as key value pair
 * Its method initTrnxnRequest returns JSON list or String with Session key which then used to select payment option
 */
public class TransactionInitiator {
    String storeId = "testbox";
    String storePassword = "qwerty";
    public String initTrnxnRequest(String total_amount, String tran_id, String std_id,
                                   String cus_name, String cus_email, String cus_phone) {
        String response = "";
        try {
            /**
             * All parameters in payment order should be constructed in this follwing postData Map
             * keep an eye on success fail url correctly.
             * insert your success and fail URL correctly in this Map
             */
            Map<String, String> postData = ParameterBuilder.constructRequestParameters();
            postData.put("total_amount", total_amount);
            postData.put("tran_id", tran_id);
            String link = "http://52.184.83.81:8080/payment/success/"+tran_id;
            postData.put("success_url", link);
            postData.put("cus_id", std_id);
            postData.put("cus_name", cus_name);
            postData.put("cus_email", cus_email);
            postData.put("cus_phone", cus_phone);
            /**
             * Provide your SSL Commerz store Id and Password by this following constructor.
             * If Test Mode then insert true and false otherwise.
             */
            SSLCommerz sslcz = new SSLCommerz(storeId, storePassword, true);

            /**
             * If user want to get Gate way list then pass isGetGatewayList parameter as true
             * If user want to get URL as returned response, pass false.
             */
            response = sslcz.initiateTransaction(postData, false);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
