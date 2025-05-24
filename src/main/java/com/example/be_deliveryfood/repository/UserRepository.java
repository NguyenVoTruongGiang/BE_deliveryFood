package com.example.be_deliveryfood.repository;

import com.example.be_deliveryfood.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm kiếm người dùng theo email (đã có trong phiên bản trước)
    Optional<User> findByEmail(String email);

    // Tìm kiếm người dùng theo vai trò (CUSTOMER, ADMIN, DELIVERY)
    List<User> findByRole(User.Role role);

    // Kiểm tra sự tồn tại của người dùng theo email
    boolean existsByEmail(String email);

    // Tìm kiếm người dùng theo tên (gần đúng, sử dụng LIKE)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);


    // Tìm người dùng theo vai trò và trạng thái hoạt động (nếu thêm trường active)
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = :active")
    List<User> findByRoleAndActive(@Param("role") User.Role role, @Param("active") boolean active);

    // Đếm số lượng người dùng theo vai trò
    long countByRole(User.Role role);

    // Tìm tất cả người dùng, sắp xếp theo tên
    List<User> findAllByOrderByNameAsc();
}
