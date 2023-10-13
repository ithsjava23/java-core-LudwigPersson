package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private String name;
    private final List<ProductRecord> products;
    private final List<ProductRecord> changedProducts;
    private final Map<UUID, ProductRecord> productMap = new HashMap<>();

    private Warehouse(String name) {
        this.name = name;
        this.products = new ArrayList<>();
        this.changedProducts = new ArrayList<>();
    }

    public static Warehouse getInstance(String warehouse) {
        return new Warehouse(warehouse);
    }

    public static Warehouse getInstance() {
        return new Warehouse("Warehouse");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        List<ProductRecord> list = new ArrayList<>(products);
        return Collections.unmodifiableList(list);
    }

    public List<ProductRecord> getChangedProducts() {
        return List.copyOf(changedProducts);
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return Collections.unmodifiableList(products.stream()
                .filter(product -> product.category().equals(category))
                .collect(Collectors.toList()));
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (productMap.containsKey(id)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        ProductRecord product = new ProductRecord(id, name, category, price);
        products.add(product);
        productMap.put(id, product);
        return product;
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return products.stream()
                .filter(product -> product.uuid().equals(uuid))
                .findFirst();
    }

    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        Optional<ProductRecord> optionalProduct = getProductById(uuid);
        if (optionalProduct.isPresent()) {
            ProductRecord product = optionalProduct.get();
            product.setPrice(newPrice);
            changedProducts.add(product);
        } else {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }
    }
}
