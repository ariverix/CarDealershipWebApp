package org.example.cardealershiprest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.example.apicontract.dto.CarRequest;
import org.example.apicontract.dto.CarResponse;
import org.example.cardealershiprest.service.CarService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class CarDataFetcher {

    private final CarService carService;

    public CarDataFetcher(CarService carService) {
        this.carService = carService;
    }

    @DgsQuery
    public List<CarResponse> cars(@InputArgument(name = "categoryFilter") String categoryFilter) {
        return carService.getAllCars(categoryFilter);
    }

    @DgsQuery
    public CarResponse carById(@InputArgument Long id) {
        return carService.getCarById(id);
    }

    @DgsMutation
    public CarResponse createCar(@InputArgument("input") Map<String, Object> input) {
        CarRequest request = new CarRequest(
                (String) input.get("brand"),
                (String) input.get("model"),
                ((Number) input.get("year")).intValue(),
                ((Number) input.get("price")).doubleValue(),
                (String) input.get("vin")
        );
        return carService.createCar(request);
    }

    @DgsMutation
    public CarResponse updateCar(@InputArgument Long id, @InputArgument("input") Map<String, Object> input) {
        CarRequest request = new CarRequest(
                (String) input.get("brand"),
                (String) input.get("model"),
                ((Number) input.get("year")).intValue(),
                ((Number) input.get("price")).doubleValue(),
                (String) input.get("vin")
        );
        return carService.updateCar(id, request);
    }

    @DgsMutation
    public Long deleteCar(@InputArgument Long id) {
        carService.deleteCar(id);
        return id;
    }
}
