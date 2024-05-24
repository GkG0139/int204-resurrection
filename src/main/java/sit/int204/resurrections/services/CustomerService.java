package sit.int204.resurrections.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.resurrections.dtos.NewCustomerDto;
import sit.int204.resurrections.entities.Customer;
import sit.int204.resurrections.repositories.CustomerRepository;
import sit.int204.resurrections.utils.ListMapper;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public CustomerService(CustomerRepository repository, ModelMapper modelMapper, ListMapper listMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public Customer findById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer number " + id + " does not exist!"));
    }

    public NewCustomerDto createCustomer(NewCustomerDto newCustomerDto) {
        if (repository.existsById(newCustomerDto.getCustomerNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Duplicate customer number " + newCustomerDto.getCustomerNumber()
            );
        }

        Customer customer = modelMapper.map(newCustomerDto, Customer.class);
        Customer savedCustomer = repository.save(customer);
        return modelMapper.map(savedCustomer, NewCustomerDto.class);
    }

    public List<NewCustomerDto> findAllCustomers() {
        return listMapper.convertList(repository.findAll(), NewCustomerDto.class);
    }
}
