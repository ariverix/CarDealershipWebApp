package org.example.cardealershiprest.controller;

import org.example.apicontract.dto.SaleRequest;
import org.example.apicontract.dto.SaleResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.endpoints.SaleApi;
import org.example.cardealershiprest.assemblers.SaleModelAssembler;
import org.example.cardealershiprest.service.SaleService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleController implements SaleApi {

    private final SaleService saleService;
    private final SaleModelAssembler saleAssembler;

    public SaleController(SaleService saleService, SaleModelAssembler saleAssembler) {
        this.saleService = saleService;
        this.saleAssembler = saleAssembler;
    }

    @Override
    public CollectionModel<EntityModel<SaleResponse>> getAllSales() {
        return saleAssembler.toCollectionModel(saleService.getAllSales());
    }

    @Override
    public EntityModel<SaleResponse> getSaleById(Long id) {
        return saleAssembler.toModel(saleService.getSaleById(id));
    }

    @Override
    public ResponseEntity<EntityModel<SaleResponse>> createSale(SaleRequest request) {
        var created = saleService.createSale(request);
        var entity = saleAssembler.toModel(created);
        return ResponseEntity.created(entity.getRequiredLink("self").toUri()).body(entity);
    }

    @Override
    public StatusResponse deleteSale(Long id) {
        return saleService.deleteSale(id);
    }
}
