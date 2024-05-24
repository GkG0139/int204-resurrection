package sit.int204.resurrections.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sit.int204.resurrections.entities.Office;
import sit.int204.resurrections.repositories.OfficeRepository;

import java.util.List;

@Service
public class OfficeService {
    private final OfficeRepository repository;

    @Autowired
    public OfficeService(OfficeRepository repository) {
        this.repository = repository;
    }

    public Long getOfficeCount() {
        return repository.count();
    }

    public List<Office> getAllOffices() {
        return repository.findAll();
    }

    public List<Office> getAllOffices(String city) {
        if (city == null || city.isEmpty()) {
            return getAllOffices();
        }

        return repository.findByCityContaining(city);
    }

    public Office getOffice(String officeCode) {
        return repository.findById(officeCode)
                .orElseThrow(() -> new HttpClientErrorException(
                        HttpStatus.NOT_FOUND,
                        "Office with code " + officeCode + " not found"));
    }

    @Transactional
    public Office createOffice(Office office) {
        return repository.save(office);
    }

    @Transactional
    public void removeOffice(String officeCode) {
        Office office = repository.findById(officeCode)
                .orElseThrow(() -> new HttpClientErrorException(
                        HttpStatus.NOT_FOUND,
                        "Office Id " + officeCode + " DOES NOT EXIST!!!"));
        repository.delete(office);
    }

    @Transactional
    public Office updateOffice(String officeCode, Office office) {
        boolean isOfficeCodeConflict = office.getOfficeCode() != null &&
                !office.getOfficeCode().trim().isEmpty() &&
                !office.getOfficeCode().equals(officeCode);
        if (isOfficeCodeConflict) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Conflict Office code  !!! (" + officeCode + " vs " + office.getOfficeCode() + ")");
        }

        repository.findById(officeCode)
                .orElseThrow(() -> new HttpClientErrorException(
                        HttpStatus.NOT_FOUND,
                        "Office Id" + officeCode + " DOES NOT EXIST !!!"));

        return repository.save(office);
    }
}
