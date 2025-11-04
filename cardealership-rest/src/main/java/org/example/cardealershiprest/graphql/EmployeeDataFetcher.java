package org.example.cardealershiprest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.example.apicontract.dto.EmployeeRequest;
import org.example.apicontract.dto.EmployeeResponse;
import org.example.cardealershiprest.service.EmployeeService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class EmployeeDataFetcher {

    private final EmployeeService employeeService;

    public EmployeeDataFetcher(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @DgsQuery
    public List<EmployeeResponse> employees() {
        return employeeService.getAllEmployees();
    }

    @DgsQuery
    public EmployeeResponse employeeById(@InputArgument Long id) {
        return employeeService.getEmployeeById(id);
    }

    @DgsMutation
    public EmployeeResponse createEmployee(@InputArgument("input") Map<String, String> input) {
        EmployeeRequest request = new EmployeeRequest(
                input.get("firstName"),
                input.get("lastName"),
                input.get("position"),
                input.get("email")
        );
        return employeeService.createEmployee(request);
    }

    @DgsMutation
    public Long deleteEmployee(@InputArgument Long id) {
        employeeService.deleteEmployee(id);
        return id;
    }
}
