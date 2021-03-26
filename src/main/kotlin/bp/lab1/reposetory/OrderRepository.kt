package bp.lab1.reposetory

import bp.lab1.models.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByReceiptId(id: Long): Order
    fun findByItem_Company_Id(id: Long): List<Order>
    fun findByUser_Id(id: Long): List<Order>
}