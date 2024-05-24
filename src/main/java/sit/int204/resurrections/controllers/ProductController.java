package sit.int204.resurrections.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import sit.int204.resurrections.dtos.PageDTO;
import sit.int204.resurrections.dtos.VerySimpleProductDTO;
import sit.int204.resurrections.entities.Product;
import sit.int204.resurrections.exceptions.ErrorResponse;
import sit.int204.resurrections.exceptions.ItemNotFoundException;
import sit.int204.resurrections.services.ProductService;
import sit.int204.resurrections.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    private final ListMapper listMapper;

    @Autowired
    public ProductController(ProductService service, ListMapper listMapper) {
        this.service = service;
        this.listMapper = listMapper;
    }

    @GetMapping("")
    public ResponseEntity<Object> findAllProducts(
            @RequestParam(defaultValue = "0") Double lower,
            @RequestParam(defaultValue = "0") Double upper,
            @RequestParam(defaultValue = "") String partOfProductName,
            @RequestParam(defaultValue = "") String[] sortBy,
            @RequestParam(defaultValue = "ASC") String[] direction,
            @RequestParam(defaultValue = "1") @Min(0) int pageNo,
            @RequestParam(defaultValue = "10") @Min(10) @Max(20) int pageSize) {
        Page<Product> productPage = service.findAllProducts(lower, upper, partOfProductName, sortBy, direction, pageNo, pageSize);
        PageDTO<VerySimpleProductDTO> productPageDTO = listMapper.toPageDTO(productPage, VerySimpleProductDTO.class);
        return ResponseEntity.ok(productPageDTO);
    }

    @GetMapping("/product-line/{id}")
    public ResponseEntity<Object> getProductLine(@PathVariable String id) {
        List<Product> productList = service.findProductsByCategory(id);
        List<VerySimpleProductDTO> verySimpleProductDTOs = listMapper.convertList(productList, VerySimpleProductDTO.class);
        return ResponseEntity.ok(verySimpleProductDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping("/example")
    public ResponseEntity<List<Product>> getExampleProduct() {
        return ResponseEntity.ok(service.getProductByExample());
    }

//    @ExceptionHandler(ItemNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ItemNotFoundException handleItemNotFoundException(ItemNotFoundException exception) {
//        return exception;
//    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
