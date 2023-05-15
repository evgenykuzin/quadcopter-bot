package ru.jekajops.quadcopterbot;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.jekajops.quadcopterbot.dto.ProductsListRq;
import ru.jekajops.quadcopterbot.service.CartService;
import ru.jekajops.quadcopterbot.service.ProductService;

@Controller
@RequestMapping("/")
public class AdminController {
    private final ProductService productService;
    private final CartService cartService;
    public AdminController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/products/add")
    public ResponseEntity<Object> addProducts(@RequestBody ProductsListRq productsListRq) {
        Boolean result = productService.addProducts(productsListRq);
        System.out.println(productsListRq + "\n\nproducts added: "+result);
        return result ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/products/all")
    public ResponseEntity<ProductsListRq> getProducts() {
        ProductsListRq result = productService.getAllProducts();
        System.out.println("products has gotten from db: \n\n"+result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/xmarket")
    public String index() {
        return "index";
    }

}
