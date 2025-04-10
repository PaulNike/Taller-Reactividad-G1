package com.talleresdeprogramacion.controller;

import com.talleresdeprogramacion.dto.InvoiceDTO;
import com.talleresdeprogramacion.model.Invoice;
import com.talleresdeprogramacion.service.InvoiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping
    public Mono<ResponseEntity<Flux<InvoiceDTO>>> findAll(){
        Flux<InvoiceDTO> fx = invoiceService.findAll()
                .map(this::converToDTO);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<InvoiceDTO>> findById(@PathVariable("id") String id){
        return invoiceService.findById(id)
                .map(this::converToDTO)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
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

    @PutMapping("/{id}")
    public Mono<ResponseEntity<InvoiceDTO>> update(@PathVariable("id") String id,
                                                 @RequestBody InvoiceDTO invoiceDTO){

        return Mono.just(invoiceDTO)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> invoiceService.update(id, convertToDocument(invoiceDTO)))
                .map(this::converToDTO)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/generateReport/{id}")
    public Mono<ResponseEntity<byte[]>> generateReport(@PathVariable("id") String  id){
        return invoiceService.generateReport(id)
                .map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(bytes)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    private InvoiceDTO converToDTO(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    private Invoice convertToDocument(InvoiceDTO invoiceDTO){
        return modelMapper.map(invoiceDTO, Invoice.class);
    }
}
