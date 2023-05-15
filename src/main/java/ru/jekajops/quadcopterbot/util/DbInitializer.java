package ru.jekajops.quadcopterbot.util;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.jekajops.quadcopterbot.dto.ProductsListRq;
import ru.jekajops.quadcopterbot.service.ProductService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Slf4j
public class DbInitializer {
    private final JsonMapper jsonMapper;
    private final ProductService productService;
    private final ResourceLoader resourceLoader;

    public DbInitializer(JsonMapper jsonMapper, ProductService productService, ResourceLoader resourceLoader) {
        this.jsonMapper = jsonMapper;
        this.productService = productService;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {

            try (InputStream is = resourceLoader.getResource("classpath:ru.jekajops.quadcopterbot.util/init_db.json").getInputStream()) {
                ProductsListRq productsListRq = jsonMapper.readValue(new InputStreamReader(is), ProductsListRq.class);
                Boolean result = productService.addProducts(productsListRq);
                log.info("initiated products added: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
