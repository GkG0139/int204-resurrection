package sit.int204.resurrections.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sit.int204.resurrections.dtos.VerySimpleProductDTO;
import sit.int204.resurrections.entities.Product;
import sit.int204.resurrections.exceptions.ItemNotFoundException;
import sit.int204.resurrections.repositories.ProductRepository;
import sit.int204.resurrections.utils.ListMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ListMapper listMapper;

    @Autowired
    public ProductService(ProductRepository repository, ListMapper listMapper) {
        this.repository = repository;
        this.listMapper = listMapper;
    }

    public List<Product> findProductsByCategory(String category) {
//        return repository.findByProductLineStartingWith(category);
        return repository.findProductByCategory(category + "%");
    }

//    public List<Product> findAllProducts(String partOfProduct, Double lower, Double upper) {
//        if (lower <= 0 && upper <= 0) {
//            return repository.findByProductNameContains(partOfProduct);
//        }
//
//        if (lower > upper) {
//            double tmp = lower;
//            lower = upper;
//            upper = tmp;
//        }
//
//        return repository.findByBuyPriceBetweenAndProductNameContains(lower, upper, partOfProduct);
//    }

    public List<Product> findAllProducts(Double lower, Double upper, String partOfProduct, String sortBy, String direction) {
        if (lower <= 0 && upper <= 0) {
            upper = repository.findFirstByOrderByPriceDesc().getPrice();
        }

        if (lower > upper) {
            double temp = lower;
            lower = upper;
            upper = temp;
        }

        Sort.Order sortOrder = null;
        if (sortBy != null && !sortBy.isEmpty() && direction != null && !direction.isEmpty()) {
            sortOrder = new Sort.Order(Sort.Direction.fromString(direction), sortBy);
        }

        if (sortOrder != null) {
            return repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct, Sort.by(sortOrder));
        }
        return repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct);
    }

    public List<Product> findAllProducts(Double lower, Double upper, String partOfProduct, String[] sortBy, String[] direction) {
        if (lower <= 0 && upper <= 0) {
            upper = repository.findFirstByOrderByPriceDesc().getPrice();
        }

        if (lower > upper) {
            double temp = lower;
            lower = upper;
            upper = temp;
        }

        List<Sort.Order> sortOrders = new ArrayList<>();
        if (sortBy != null && sortBy.length > 0) {
            for (int i = 0; i < sortBy.length; i++) {
                sortOrders.add(new Sort.Order(Sort.Direction.fromString(direction[i]), sortBy[i]));
            }
        }

        if (!sortOrders.isEmpty()) {
            return repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct, Sort.by(sortOrders));
        }
        return repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct);
    }

    public Page<Product> findAllProducts(
            Double lower,
            Double upper,
            String partOfProduct,
            String[] sortBy,
            String[] direction,
            int pageNo,
            int pageSize) {
        if (lower <= 0 && upper <= 0) {
            upper = repository.findFirstByOrderByPriceDesc().getPrice();
        }

        if (lower > upper) {
            double temp = lower;
            lower = upper;
            upper = temp;
        }

        List<Sort.Order> sortOrders = new ArrayList<>();
        if (sortBy != null && sortBy.length > 0) {
            for (int i = 0; i < sortBy.length; i++) {
                sortOrders.add(new Sort.Order(Sort.Direction.fromString(direction[i]), sortBy[i]));
            }
        }

        if (!sortOrders.isEmpty()) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortOrders));
            return repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct, pageable);
        }
        return new PageImpl<>(repository.findByPriceBetweenAndProductNameContains(lower, upper, partOfProduct));
    }

    public Product getProductById(String productCode) {
        return repository.findById(productCode)
                .orElseThrow(() -> new ItemNotFoundException("Product code: " + productCode + " cannot be found!"));
    }

    public List<Product> getProductByExample() {
        Product productExample = new Product();
        productExample.setProductLine("Classic Cars"); // The product line would expect to be match 100%
        productExample.setProductName("19");
        productExample.setProductDescription("classic");
//        productExample.setProductVendor("gear");
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("productName", ignoreCase().contains())
                .withMatcher("productDescription", ignoreCase().contains())
                .withMatcher("productVendor", ignoreCase().startsWith());
        return repository.findAll(Example.of(productExample, matcher));
    }
}
