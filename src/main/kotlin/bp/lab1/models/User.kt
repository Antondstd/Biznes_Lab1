package bp.lab1.models


import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "BL_USERS")
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long = 0

    @Column(name = "email", nullable = false, unique = true)
    lateinit var email: String

    @Column(name = "password", nullable = false)
    lateinit var password: String

    @Column(name = "role", nullable = false)
    lateinit var role: String

    @OneToOne
    @JoinColumn(name = "company_id")
    var company: Company? = null

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    lateinit var orders: Set<Order>

}