package sit.int204.resurrections.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int204.resurrections.entities.Employee;
import sit.int204.resurrections.entities.Office;
import sit.int204.resurrections.services.OfficeService;

import java.util.List;

import java.util.Set;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {
    private final OfficeService service;

    @Autowired
    public OfficeController(OfficeService service) {
        this.service = service;
    }

    @GetMapping("/count")
    public Long getOfficeCount() {
        return service.getOfficeCount();
    }
    @GetMapping("/{officeCode}/employees")
    public Set<Employee> getOfficeEmployee(@PathVariable String officeCode) {
        return service.getOffice(officeCode).getEmployees();
    }
    @GetMapping("")
    public List<Office> getAllOffices(@RequestParam(required = false) String city) {
        return service.getAllOffices(city);
    }

    @GetMapping("/{officeCode}")
    public Office getOfficeById(@PathVariable String officeCode) {
        return service.getOffice(officeCode);
    }

    @PostMapping("")
    public Office addNewOffice(@RequestBody Office office) {
        return service.createOffice(office);
    }

    @PutMapping("/{officeCode}")
    public Office updateOffice(@RequestBody Office office, @PathVariable String officeCode) {
        return service.updateOffice(officeCode, office);
    }

    @DeleteMapping("/{officeCode}")
    public void removeOffice(@PathVariable String officeCode) {
        service.removeOffice(officeCode);
    }

}