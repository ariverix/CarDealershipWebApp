package org.example.cardealershiprest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.example.apicontract.dto.CustomerRequest;
import org.example.apicontract.dto.CustomerResponse;
import org.example.cardealershiprest.service.CustomerService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class CustomerDataFetcher {

    private final CustomerService customerService;

    public CustomerDataFetcher(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DgsQuery
    public List<CustomerResponse> customers() {
        return customerService.getAllCustomers();
    }

    @DgsQuery
    public CustomerResponse customerById(@InputArgument Long id) {
        return customerService.getCustomerById(id);
    }

    @DgsMutation
    public CustomerResponse createCustomer(@InputArgument("input") Map<String, String> input) {
        CustomerRequest request = new CustomerRequest(
                input.get("firstName"),
                input.get("lastName"),
                input.get("phone"),
                input.get("email")
        );
        return customerService.createCustomer(request);
    }

    @DgsMutation
    public Long deleteCustomer(@InputArgument Long id) {
        customerService.deleteCustomer(id);
        return id;
    }
}
