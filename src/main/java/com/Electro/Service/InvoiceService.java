package com.Electro.Service;

import com.Electro.Entities.Cart;
import com.Electro.Entities.Invoice;

import java.util.List;

public interface InvoiceService {
    Invoice createInvoice();
    void saveInvoice(Invoice invoice);
    void createInvoiceDetails(Invoice invoice, Cart cart);
    public void updatePaymentStatus(Long invoiceId, String paymentStatus);
    public List<Invoice> getInvoicesByUserId(Long userId);
}
