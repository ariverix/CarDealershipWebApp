package org.example.cardealershiprest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.example.apicontract.dto.CarResponse;
import org.example.apicontract.dto.CustomerResponse;
import org.example.apicontract.dto.EmployeeResponse;
import org.example.apicontract.dto.SaleRequest;
import org.example.apicontract.dto.SaleResponse;
import org.example.cardealershiprest.service.SaleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@DgsComponent
public class SaleDataFetcher {

    private final SaleService saleService;

    public SaleDataFetcher(SaleService saleService) {
        this.saleService = saleService;
    }

    @DgsQuery
    public List<SaleResponse> sales() {
        return saleService.getAllSales();
    }

    @DgsQuery
    public SaleResponse saleById(@InputArgument Long id) {
        return saleService.getSaleById(id);
    }

    @DgsMutation
    public SaleResponse createSale(@InputArgument("input") Map<String, Object> input) {
        SaleRequest request = new SaleRequest(
                Long.parseLong(input.get("carId").toString()),
                Long.parseLong(input.get("customerId").toString()),
                Long.parseLong(input.get("employeeId").toString()),
                ((Number) input.get("salePrice")).doubleValue(),
                LocalDate.parse((String) input.get("saleDate"))
        );
        return saleService.createSale(request);
    }

    @DgsMutation
    public Long deleteSale(@InputArgument Long id) {
        saleService.deleteSale(id);
        return id;
    }

    @DgsData(parentType = "Sale", field = "car")
    public CarResponse car(DataFetchingEnvironment dfe) {
        SaleResponse sale = dfe.getSource();
        return sale.getCar();
    }

    @DgsData(parentType = "Sale", field = "customer")
    public CustomerResponse customer(DataFetchingEnvironment dfe) {
        SaleResponse sale = dfe.getSource();
        return sale.getCustomer();
    }

    @DgsData(parentType = "Sale", field = "employee")
    public EmployeeResponse employee(DataFetchingEnvironment dfe) {
        SaleResponse sale = dfe.getSource();
        return sale.getEmployee();
    }
}
