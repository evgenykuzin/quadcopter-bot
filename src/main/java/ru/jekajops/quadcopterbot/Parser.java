package ru.jekajops.quadcopterbot;

import com.google.common.io.Files;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Parser {
    public static void main(String[] args) {
        parse();
    }

    public static void parse() {
        try {
            WebDriver driver = driver();
            if (driver != null) {
                driver.get("https://www.xmarket.ru");

                String[] cataloglinks = Files.readLines(new File("/Users/19190813/IdeaProjects/quadcopter-bot/src/main/resources/cataloglinks.txt"),
                        StandardCharsets.UTF_8).toArray(String[]::new);

                parseAndSaveProductsFromLinksList(driver, cataloglinks);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void parseAndSaveProductsFromLinksList(WebDriver driver, String... links) {

        WorkbookSaver workbookSaver = new WorkbookSaver(new XSSFWorkbook());
        for (String link : links) {
            String key = link.split("catalog/")[1].replaceAll("/", "");
            List<Product> products = Parser.parseProducts(driverPage(driver, link));
            workbookSaver.saveProducts(key, products);
        }
    }

    public static WebDriver driver() {
        try {
            System.setProperty("webdriver.chrome.driver", "/Users/19190813/IdeaProjects/quadcopter-bot/src/main/resources/chromedriver");
            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://www.xmarket.ru/auth");
            new WebDriverWait(driver, Duration.ofMillis(60000))
                    .until(dr -> !dr.getCurrentUrl().equals("https://www.xmarket.ru/auth"));
            sleep(60000);
            return driver;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String driverPage(WebDriver driver, String url) {
        driver.get(url);
        return driver.getPageSource();
    }

    public static List<Product> parseProducts(String source) {
        try {
            Document doc = Jsoup.parse(source, StandardCharsets.UTF_8.name());
            Elements product_items = doc.getElementsByClass("product-item");
            List<Product> products = new ArrayList<>();
            for (Element pi : product_items) {
                List<String> images = pi.getElementsByClass("product-item-image-original").stream()
                        .map(e -> e.attr("style"))
                        .map(s -> "https://www.xmarket.ru/upload" +
                                Arrays.stream(Arrays.stream(s.split("background-image: url\\("))
                                                .skip(1)
                                                .findFirst()
                                                .orElse("")
                                                .split("'\\);"))
                                        .findFirst()
                                        .orElse("")
                                        .replaceAll("\\); display: none;", "")
                                        .replaceAll("'upload", "/upload")
                                        .split("/upload")[1]
                        )
                        .collect(Collectors.toList());

                String title = pi.getElementsByClass("product-item-title")
                        .first()
                        .getElementsByTag("a")
                        .first()
                        .text();

                String priceStr = pi.getElementsByClass("product-item-price-current")
                        .first()
                        .text()
                        .replaceAll(" ", "")
                        .replaceAll("руб\\.", "")
                        .replaceAll("\n", "")
                        .replaceAll("\t", "");
                BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceStr)).setScale(2, RoundingMode.UP);
                BigDecimal percentAdd = price.movePointLeft(2).multiply(BigDecimal.valueOf(22)).setScale(2, RoundingMode.UP);
                BigDecimal ourPrice = price.add(percentAdd);

                products.add(Product.builder()
                        .title(title)
                        .price(price)
                        .ourPrice(ourPrice)
                        .images(images)
                        .build());
            }
            System.out.println("products = " + products);
            return products;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static File downloadFile(String url) {
        try {
            URL urll = new URL(url);
            File file = File.createTempFile("image", "jpeg", new File("/Users/19190813/IdeaProjects/quadcopter-bot/src/main/resources/images/"));
            try (InputStream is = urll.openStream(); FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(is.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImageData(File path) {
        try {
            return path.toURI().toURL().openStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class WorkbookSaver {
        private final Workbook workbook;

        public WorkbookSaver(Workbook workbook) {
            this.workbook = workbook;
        }

        public synchronized void saveProducts(Map<String, List<Product>> productsMap) {
            productsMap.keySet().forEach(
                    key -> saveProducts(key, productsMap.get(key))
            );
        }

        public synchronized void saveProducts(String category, List<Product> products) {
            try {
                Sheet sheet = workbook.createSheet(category);
                CellFactory cellFactory = CellFactory.getInstance(workbook);
                Row headRow = sheet.createRow(0);
                cellFactory.createCell(headRow, 0, "Товар");
                cellFactory.createCell(headRow, 1, "Цена");
                cellFactory.createCell(headRow, 2, "Фото");

                int rowsize = sheet.getPhysicalNumberOfRows();

                for (int i = 0; i < products.size(); i++) {
                    int indx = i + rowsize;
                    Product product = products.get(i);
                    Row row = sheet.createRow(indx);


                    cellFactory.createCell(row, 0, product.title);
                    cellFactory.createCell(row, 1, product.ourPrice.toPlainString() + " руб.");

                    List<byte[]> imgDataList = product.images.stream()
                            .map(Parser::downloadFile)
                            .filter(Objects::nonNull)
                            .map(Parser::getImageData)
                            .filter(bytes -> bytes.length > 0)
                            .collect(Collectors.toList());

                    if (!imgDataList.isEmpty()) {
                        int inputImagePictureID = workbook.addPicture(imgDataList.get(0), Workbook.PICTURE_TYPE_PNG);
                        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                        XSSFClientAnchor anchor = new XSSFClientAnchor();
                        anchor.setCol1(2); // Sets the column (0 based) of the first cell.
                        anchor.setCol2(3); // Sets the column (0 based) of the Second cell.
                        anchor.setRow1(indx); // Sets the row (0 based) of the first cell.
                        anchor.setRow2(indx + 1); // Sets the row (0 based) of the Second cell.
                        drawing.createPicture(anchor, inputImagePictureID);
                    }

                }

                sheet.setDefaultRowHeightInPoints(300);
                sheet.setDefaultColumnWidth(70);

                for (int j = 0; j < 3; j++) {
                    sheet.autoSizeColumn(j);
                }
                try (FileOutputStream saveExcel = new FileOutputStream("/Users/19190813/IdeaProjects/quadcopter-bot/src/main/resources/pricelist.xlsx")) {
                    workbook.write(saveExcel);
                    System.out.println("pricelist saved");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static class CellFactory {
            private final XSSFCellStyle style;

            private CellFactory(Workbook workbook) {
                XSSFFont font = (XSSFFont) workbook.createFont();
                font.setFontHeightInPoints((short) 22);
                font.setFontName("IMPACT");
                font.setBold(false);

                //Set font into style
                XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
                style.setFont(font);
                this.style = style;
            }

            public static CellFactory getInstance(Workbook workbook) {
                return new CellFactory(workbook);
            }

            private synchronized XSSFCell createCell(Row row, Integer indx, String value) {
                XSSFCell cell = (XSSFCell) row.createCell(indx);
                cell.setCellValue(value);
                cell.setCellStyle(style);
                return cell;
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Product {
        String title;
        BigDecimal price;
        BigDecimal ourPrice;
        List<String> images;
    }
}
