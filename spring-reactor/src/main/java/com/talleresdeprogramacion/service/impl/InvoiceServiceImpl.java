package com.talleresdeprogramacion.service.impl;

import com.talleresdeprogramacion.model.Invoice;
import com.talleresdeprogramacion.model.InvoiceDetail;
import com.talleresdeprogramacion.repository.ClientRepository;
import com.talleresdeprogramacion.repository.DishRepository;
import com.talleresdeprogramacion.repository.GenericRepository;
import com.talleresdeprogramacion.repository.InvoiceRepository;
import com.talleresdeprogramacion.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl extends GenericCrudImpl<Invoice, String> implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final DishRepository dishRepository;
    @Override
    protected GenericRepository<Invoice, String> getRepository() {
        return invoiceRepository;
    }

    private Mono<Invoice> populateClient(Invoice invoice){
        return clientRepository.findById(invoice.getClient().getId())
                .map(client -> {
                    invoice.setClient(client);
                    return invoice;
                });
    }

    private Mono<Invoice> populateItems(Invoice invoice){
        List<Mono<InvoiceDetail>> list = invoice.getItems().stream()
                .map(item -> dishRepository.findById(item.getDish().getId())
                        .map(dish -> {
                            item.setDish(dish);
                            return item;
                        })).toList();

        return Mono.when(list).then(Mono.just(invoice));
    }


}
