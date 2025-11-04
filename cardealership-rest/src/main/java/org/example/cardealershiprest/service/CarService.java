package org.example.cardealershiprest.service;

import org.example.apicontract.dto.CarRequest;
import org.example.apicontract.dto.CarResponse;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.DuplicateResourceException;
import org.example.apicontract.exception.InvalidOperationException;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.example.eventscontract.events.CarAddedEvent;
import org.example.cardealershiprest.config.RabbitMQConfig;
import org.example.cardealershiprest.model.Car;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {

    private final List<Car> cars = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final RabbitTemplate rabbitTemplate;

    public CarService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        Car car1 = new Car(idCounter.getAndIncrement(), "Toyota", "Camry", 2023, 35000.0, "JT2BF22K5X0123456");
        car1.setCategory("Седан");
        cars.add(car1);

        Car car2 = new Car(idCounter.getAndIncrement(), "BMW", "X5", 2024, 65000.0, "5UXCR6C09L9B12345");
        car2.setCategory("Внедорожник");
        cars.add(car2);
    }

    public List<CarResponse> getAllCars() {
        return cars.stream().map(this::toResponse).toList();
    }

    public List<CarResponse> getAllCars(String categoryFilter) {
        if (categoryFilter == null || categoryFilter.isBlank()) {
            return getAllCars();
        }
        return cars.stream()
                .filter(c -> c.getCategory() != null &&
                        c.getCategory().equalsIgnoreCase(categoryFilter))
                .map(this::toResponse)
                .toList();
    }

    public CarResponse getCarById(Long id) {
        return toResponse(findCarById(id));
    }

    public CarResponse createCar(CarRequest request) {
        boolean vinExists = cars.stream().anyMatch(c -> c.getVin().equals(request.vin()));
        if (vinExists)
            throw new DuplicateResourceException("Автомобиль", "VIN", request.vin());

        Car car = new Car(idCounter.getAndIncrement(),
                request.brand(), request.model(), request.year(), request.price(), request.vin());
        car.setCategory("Не указано");
        cars.add(car);

        var event = new CarAddedEvent(
                car.getId(), car.getBrand(), car.getModel(), car.getYear(), car.getPrice()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_CAR_ADDED,
                event
        );

        return toResponse(car);
    }

    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = findCarById(id);
        if (car.isSold())
            throw new InvalidOperationException("Нельзя обновить проданный автомобиль");

        if (!car.getVin().equals(request.vin()) &&
                cars.stream().anyMatch(c -> c.getVin().equals(request.vin())))
            throw new DuplicateResourceException("Автомобиль", "VIN", request.vin());

        car.setBrand(request.brand());
        car.setModel(request.model());
        car.setYear(request.year());
        car.setPrice(request.price());
        car.setVin(request.vin());
        return toResponse(car);
    }

    public StatusResponse deleteCar(Long id) {
        Car car = findCarById(id);
        if (car.isSold())
            throw new InvalidOperationException("Нельзя удалить проданный автомобиль");
        cars.remove(car);
        return new StatusResponse("success", "Автомобиль удалён");
    }

    public void markAsSold(Long carId) {
        Car car = findCarById(carId);
        if (car.isSold()) throw new InvalidOperationException("Автомобиль уже продан");
        car.setSold(true);
    }

    private Car findCarById(Long id) {
        return cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль", id));
    }

    private CarResponse toResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getPrice(),
                car.getVin(),
                car.isSold(),
                car.getCreatedAt(),
                car.getCategory()
        );
    }
}
