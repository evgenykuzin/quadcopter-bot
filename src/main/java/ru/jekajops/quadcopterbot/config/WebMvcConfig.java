package ru.jekajops.quadcopterbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private ServletContext servletContext;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ServletContextTemplateResolver templateResolver =
                new ServletContextTemplateResolver(servletContext);
        // Папка с шаблонами относительно папки 'webapp'.
        // Полный путь: '/src/main/webapp/view/'.
        templateResolver.setPrefix("/resources/xmarket.ru/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        // Расположение файла 'messages.properties' с константами.
        // Полный путь: '/src/main/resources/messages.properties'.
        messageSource.setBasename("resources/messages");

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(messageSource);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);

        registry.viewResolver(viewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("/resources/xmarket.ru/");
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/resources/xmarket.ru/");
        registry
                .addResourceHandler("/**")
                .addResourceLocations("/resources/xmarket.ru/");
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/resources/xmarket.ru/");
        registry
                .addResourceHandler("")
                .addResourceLocations("/resources/xmarket.ru/");
    }
}