package bp.lab1.models

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "BL_Notify")
class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "check_id")
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @OneToOne
    @JoinColumn(name = "order_id")
    lateinit var order: Order

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var createdTime: Date
}