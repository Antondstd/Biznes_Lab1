package bp.lab1.models


import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BL_CHECK")
class Receipt { //Receipt maybe???

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "check_id")
    var id: Long = 0

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    var order: Order? = null

    @Column(name = "status")
    var isPayed: Boolean = false

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var created_time: Date

}