package com.example.be_deliveryfood.config;

import com.example.be_deliveryfood.dto.repository.*;
import com.example.be_deliveryfood.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public DataSeeder(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Users
        if (userRepository.count() == 0) {
            try {
                InputStream userStream = new ClassPathResource("data/users.json").getInputStream();
                List<User> users = mapper.readValue(userStream, new TypeReference<List<User>>() {});
                userRepository.saveAll(users);
                System.out.println("Seeded users from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed users: " + e.getMessage());
                return; // Stop if users fail
            }
        }

        // Seed Products
        if (productRepository.count() == 0) {
            try {
                InputStream productStream = new ClassPathResource("data/products.json").getInputStream();
                List<Product> products = mapper.readValue(productStream, new TypeReference<List<Product>>() {});
                productRepository.saveAll(products);
                System.out.println("Seeded products from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed products: " + e.getMessage());
                return; // Stop if products fail
            }
        }

        // Seed Carts
        if (cartRepository.count() == 0) {
            try {
                InputStream cartStream = new ClassPathResource("data/carts.json").getInputStream();
                List<Map<String, Object>> cartsRaw = mapper.readValue(cartStream, new TypeReference<List<Map<String, Object>>>() {});
                for (Map<String, Object> cartMap : cartsRaw) {
                    Object userIdObj = cartMap.get("user_id");
                    Object productIdObj = cartMap.get("product_id");
                    if (userIdObj == null || productIdObj == null) {
                        System.out.println("Skipping cart: Missing user_id or product_id");
                        continue;
                    }
                    Long userId = ((Number) userIdObj).longValue();
                    Long productId = ((Number) productIdObj).longValue();
                    User user = userRepository.findById(userId).orElse(null);
                    Product product = productRepository.findById(productId).orElse(null);
                    if (user == null) {
                        System.out.println("Skipping cart: User not found for user_id: " + userId);
                        continue;
                    }
                    if (product == null) {
                        System.out.println("Skipping cart: Product not found for product_id: " + productId);
                        continue;
                    }
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setProduct(product);
                    cart.setAddOns((String) cartMap.get("addOns"));
                    cartRepository.save(cart);
                }
                System.out.println("Seeded carts from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed carts: " + e.getMessage());
            }
        }

        // Seed Order Items
        if (orderItemRepository.count() == 0) {
            try {
                InputStream itemStream = new ClassPathResource("data/order_items.json").getInputStream();
                List<Map<String, Object>> itemsRaw = mapper.readValue(itemStream, new TypeReference<List<Map<String, Object>>>() {});
                for (Map<String, Object> itemMap : itemsRaw) {
                    Object productIdObj = itemMap.get("productId");
                    Object cartIdObj = itemMap.get("cartId");
                    if (productIdObj == null || cartIdObj == null) {
                        System.out.println("Skipping order item: Missing productId or cartId");
                        continue;
                    }
                    Long productId = ((Number) productIdObj).longValue();
                    Long cartId = ((Number) cartIdObj).longValue();
                    Product product = productRepository.findById(productId).orElse(null);
                    Cart cart = cartRepository.findById(cartId).orElse(null);
                    if (product == null) {
                        System.out.println("Skipping order item: Product not found for productId: " + productId);
                        continue;
                    }
                    if (cart == null) {
                        System.out.println("Skipping order item: Cart not found for cartId: " + cartId);
                        continue;
                    }
                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setCart(cart);
                    item.setQuantity((Integer) itemMap.get("quantity"));
                    item.setPrice(Double.valueOf(itemMap.get("price").toString()));
                    orderItemRepository.save(item);
                }
                System.out.println("Seeded order items from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed order items: " + e.getMessage());
            }
        }

        /// Seed Orders
        if (orderRepository.count() == 0) {
            try {
                InputStream orderStream = new ClassPathResource("data/orders.json").getInputStream();
                List<Map<String, Object>> ordersRaw;
                try {
                    ordersRaw = mapper.readValue(orderStream, new TypeReference<List<Map<String, Object>>>() {});
                } catch (Exception e) {
                    System.out.println("orders.json is a single object, attempting to parse as single order");
                    orderStream = new ClassPathResource("data/orders.json").getInputStream();
                    Map<String, Object> singleOrder = mapper.readValue(orderStream, new TypeReference<Map<String, Object>>() {});
                    ordersRaw = new ArrayList<>();
                    ordersRaw.add(singleOrder);
                }
                for (Map<String, Object> orderMap : ordersRaw) {
                    Object userIdObj = orderMap.get("user_id");
                    if (userIdObj == null) {
                        userIdObj = orderMap.get("userId"); // Fallback
                        if (userIdObj == null && orderMap.get("user") instanceof Map) {
                            userIdObj = ((Map<?, ?>) orderMap.get("user")).get("id"); // Nested user.id
                        }
                    }
                    if (userIdObj == null) {
                        System.out.println("Skipping order: Missing user_id");
                        continue;
                    }
                    Long userId = ((Number) userIdObj).longValue();
                    User user = userRepository.findById(userId).orElse(null);
                    if (user == null) {
                        System.out.println("Skipping order: User not found for user_id: " + userId);
                        continue;
                    }
                    Order order = new Order();
                    order.setUser(user);
                    order.setDeliveryAddress((String) orderMap.get("delivery_address"));
                    order.setTotalPrice((Double) orderMap.get("total_price"));
                    order.setCreatedAt(java.time.LocalDateTime.parse((String) orderMap.get("created_at")));
                    order.setStatus(Order.OrderStatus.valueOf((String) orderMap.get("status")));
                    orderRepository.save(order);

                    // Link order items to order
                    List<OrderItem> items = orderItemRepository.findAll();
                    for (OrderItem item : items) {
                        if (item.getCart() != null && item.getCart().getId().equals(1L)) {
                            item.setOrder(order);
                            item.setCart(null);
                            orderItemRepository.save(item);
                        }
                    }
                }
                System.out.println("Seeded orders and linked items from JSON!");
            } catch (Exception e) {
                System.out.println("Failed to seed orders: " + e.getMessage());
            }
        }
    }
}