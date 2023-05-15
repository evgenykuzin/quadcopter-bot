package ru.jekajops.quadcopterbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jekajops.quadcopterbot.dto.ProductRq;
import ru.jekajops.quadcopterbot.dto.ProductsListRq;
import ru.jekajops.quadcopterbot.models.Order;
import ru.jekajops.quadcopterbot.models.Product;
import ru.jekajops.quadcopterbot.repository.OrderRepository;
import ru.jekajops.quadcopterbot.repository.ProductRepository;
import ru.jekajops.quadcopterbot.util.transformers.ProductTransformer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements DataService<Product, Long, ProductRepository> {
    @Autowired
    private ProductTransformer productTransformer;
    @Autowired
    private ProductRepository productRepository;

    public Boolean addProducts(ProductsListRq productsListRq) {
        List<Product> products = productsListRq.getProducts()
                .stream()
                .map(productTransformer::toProduct)
                .collect(Collectors.toList());
        productRepository.saveAll(products);
        return true;
    }

    public ProductsListRq getAllProducts() {
        List<ProductRq> products = productRepository.findAll().stream()
                .map(productTransformer::toProductRq)
                .collect(Collectors.toList());
        return new ProductsListRq(products);
    }

    public List<ProductRq> getAllByIds(List<Long> ids) {
       return ids.stream()
               .map(productRepository::findById)
               .filter(Optional::isPresent)
               .map(Optional::get)
               .peek(System.out::println)
               .map(productTransformer::toProductRq)
               .collect(Collectors.toList());
    }

    @Override
    public ProductRepository repository() {
        return productRepository;
    }

}
