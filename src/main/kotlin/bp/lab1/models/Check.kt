package bp.lab1.models


import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BL_CHECK")
class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "check_id")
    var id: Long = 0

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    var order: Order? = null

    @Column(name = "status")
    var status: Boolean = false

    @Column(name = "cr_time")
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var created_time: Date

}

interface CheckRepository : JpaRepository<Check, Long> {
    fun findByOrder_User_Id(id: Long): List<Check>
}