package bp.lab1.models

import com.fasterxml.jackson.annotation.JsonIgnore
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
    var receipt: Receipt? = null

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
    var status: StatusOrder = StatusOrder.CREATED

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var created_time: Date

    @JsonIgnore
    @OneToOne(mappedBy = "order")
    var notify: Notify? = null

}

enum class StatusOrder {
    CREATED,
    INWORK,
    ACCEPTED,
    AWAITSPAYMENT,
    PAYED,
    DELIVERING,
    RECEIVED,
    CANCELLED,
    SPAM
}