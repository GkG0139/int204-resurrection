package sit.int204.resurrections.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int204.resurrections.dtos.NewCustomerDto;
import sit.int204.resurrections.dtos.SimpleCustomerDTO;
import sit.int204.resurrections.entities.Customer;
import sit.int204.resurrections.entities.Order;
import sit.int204.resurrections.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Integer id) {
        Customer customer = service.findById(id);
        SimpleCustomerDTO simpleCustomerDTO = modelMapper.map(customer, SimpleCustomerDTO.class);
        return ResponseEntity.ok(simpleCustomerDTO);
    }

    @GetMapping("/{id}/orders")
    public List<Order> getCustomerOrders(@PathVariable Integer id) {
        return service.findById(id).getOrders();
    }

    @GetMapping
    public List<NewCustomerDto> findAllCustomers() {
        return service.findAllCustomers();
    }

    @PostMapping
    public NewCustomerDto createCustomer(@RequestBody NewCustomerDto newCustomerDto) {
        return service.createCustomer(newCustomerDto);
    }
}
