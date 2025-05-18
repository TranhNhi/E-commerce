package com.Electro.Service;

import com.Electro.Entities.InvoiceDetails;

import java.util.List;

public interface InvoiceDetailsService {
    public List<InvoiceDetails> getInvoiceDetailsByUserId(Long userId);
}
