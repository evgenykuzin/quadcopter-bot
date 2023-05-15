package ru.jekajops.quadcopterbot.util.transformers;

import org.springframework.stereotype.Component;
import ru.jekajops.quadcopterbot.dto.ProductRq;
import ru.jekajops.quadcopterbot.models.Product;

import java.util.ArrayList;

@Component
public class ProductTransformer {
    public Product toProduct(ProductRq productRq) {
        return new Product(
                productRq.getId(),
                productRq.getName(),
                productRq.getPrice(),
                productRq.getStocks(),
                productRq.getPhotoUrl(),
                productRq.getCategory(),
                new ArrayList<>()
        );
    }

    public ProductRq toProductRq(Product product) {
        return new ProductRq(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getStocksCount(),
                product.getPhotoUrl(),
                product.getProductCategory()
        );
    }
}
