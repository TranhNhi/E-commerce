package com.Electro.Service.Impl;

import com.Electro.Entities.Invoice;
import com.Electro.Entities.InvoiceDetails;
import com.Electro.Entities.User;
import com.Electro.Exception.NotFoundException;
import com.Electro.Responsitory.InvoiceDetailsRepository;
import com.Electro.Responsitory.UserRepository;
import com.Electro.Service.InvoiceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceDetailsServiceImpl implements InvoiceDetailsService {
    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    private UserRepository userRepository;
    public List<InvoiceDetails> getInvoiceDetailsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Invoice> invoices = user.getInvoices();
        List<InvoiceDetails> purchasedProducts = new ArrayList<>();
        for (Invoice invoice : invoices) {
            purchasedProducts.addAll(invoice.getInvoiceDetails());
        }
        return purchasedProducts;
    }

}
