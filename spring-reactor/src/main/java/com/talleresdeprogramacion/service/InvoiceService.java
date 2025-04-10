package com.talleresdeprogramacion.service;

import com.talleresdeprogramacion.model.Invoice;
import reactor.core.publisher.Mono;

public interface InvoiceService extends GenericCrud<Invoice, String> {

    Mono<byte[]> generateReport(String idInvoice);
}
