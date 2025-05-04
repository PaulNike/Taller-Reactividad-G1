package com.talleresdeprogramacion.service.impl;

import com.talleresdeprogramacion.pagination.PageSupport;
import com.talleresdeprogramacion.repository.GenericRepository;
import com.talleresdeprogramacion.service.GenericCrud;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class GenericCrudImpl<T, ID> implements GenericCrud<T, ID> {

    protected abstract GenericRepository<T, ID> getRepository();
    @Override
    public Mono<T> save(T t) {
        //Metodo base de guardado

        return getRepository().save(t);
    }

    @Override
    public Mono<T> update(ID id, T t) {
        return getRepository().findById(id)
                .flatMap(e -> getRepository().save(t));
    }

    @Override
    public Flux<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepository().findById(id)
                .hasElement()
                .flatMap(result -> {
                    if(result){
                        return getRepository().deleteById(id).thenReturn(true);
                    }else {
                        return Mono.just(false);
                    }
                });
    }

    @Override
    public Mono<PageSupport<T>> getPage(Pageable pageable) {
        return getRepository().findAll()
                .collectList()
                .map(list -> new PageSupport<>(
                        list.stream()
                                .skip(pageable.getPageNumber() * pageable.getPageSize())
                                .limit(pageable.getPageSize())
                                .toList(),
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        list.size()
                ));

        /*
        List<String> nombres = List.of("Ana","Carla","Rosa","Maria","Ingrid","Eunice","Flor");
        pageNumber = 1, PageSize = 3
                .skip(1 * 3) -> Saltare 2 elementos : "Ana","Carla","Rosa",
            Limit(3) -> Toma "Maria","Ingrid","Eunice"*/
    }
}
