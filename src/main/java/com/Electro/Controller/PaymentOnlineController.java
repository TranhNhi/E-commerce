package com.Electro.Controller;

import com.Electro.Service.InvoiceService;
import com.Electro.config.OnlinePaymentConfig;
import com.Electro.payload.respone.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payment_online")
public class PaymentOnlineController {
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping("/create_payment/{money}")
    public ResponseEntity<?> createPayment(@PathVariable long money) throws UnsupportedEncodingException {
        long amount = money*100;
        String vnp_TxnRef = OnlinePaymentConfig.getRandomNumber(8);
        String vnp_TmnCode = OnlinePaymentConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", OnlinePaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", OnlinePaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo","Thanh toan don hang:"+vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", OnlinePaymentConfig.vnp_Returnurl);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = OnlinePaymentConfig.hmacSHA512(OnlinePaymentConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = OnlinePaymentConfig.vnp_PayUrl + "?" + queryUrl;
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully", paymentUrl)
        );
    }

    @GetMapping("payment_infor/{invoiceId}")
    public ResponseEntity<?> transaction(
            @PathVariable long invoiceId,
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_ResponseCode") String responseCode
    ){
        if(responseCode.equals("00")){
            invoiceService.updatePaymentStatus(invoiceId, "paid");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully", "")
            );
        }
        invoiceService.updatePaymentStatus(invoiceId, "paid");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("FAILED", "FAILED", "paymentUrl")
        );
    }

}