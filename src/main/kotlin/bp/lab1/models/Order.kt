package bp.lab1.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BL_Order")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    var id: Long = 0

    @JsonIgnore
    @OneToOne(mappedBy = "order")
    var check: Check? = null

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    lateinit var item: Item

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

//    @ManyToOne
//    @JoinColumn(name="company_id",nullable = false)
//    lateinit var company: Company


    @Column(name = "status")
    var status: StatusOrder = StatusOrder.Created

    @Column(name = "cr_time")
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var created_time: Date

    @OneToOne(mappedBy = "order")
    var notify: Notify? = null

}

enum class StatusOrder {
    Created,
    InWork,
    Accepted,
    AwaitsPayment,
    Payed,
    Delivering,
    Received,
    Cancelled,
    Spam,
}

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByCheckId(id: Long): Order
    fun findByItem_Company_Id(id: Long): List<Order>
    fun findByUser_Id(id: Long): List<Order>
}