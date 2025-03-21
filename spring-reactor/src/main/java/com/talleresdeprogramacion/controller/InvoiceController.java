package com.talleresdeprogramacion.controller;

import com.talleresdeprogramacion.dto.InvoiceDTO;
import com.talleresdeprogramacion.model.Invoice;
import com.talleresdeprogramacion.service.InvoiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Qualifier("invoiceMapper")
    private final ModelMapper modelMapper;

    public InvoiceController(InvoiceService invoiceService, @Qualifier("invoiceMapper") ModelMapper modelMapper) {
        this.invoiceService = invoiceService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<InvoiceDTO>> save(@RequestBody InvoiceDTO invoiceDTO,
                                                 ServerHttpRequest request){

        return invoiceService.save(modelMapper.map(invoiceDTO, Invoice.class))
                .map(this::converToDTO)
                .map(e -> ResponseEntity.created(
                        URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private InvoiceDTO converToDTO(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

}
