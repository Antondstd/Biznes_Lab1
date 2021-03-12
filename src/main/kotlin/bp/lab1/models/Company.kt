package bp.lab1.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(name = "BL_Company")
class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    var id: Long = 0

    @JsonIgnore
    @OneToOne(mappedBy = "company")
    lateinit var user: User

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "phone_number", nullable = false, unique = true)
    lateinit var phoneNumber: String

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    lateinit var items: Set<Item>
//    @JsonIgnore
//    @OneToMany(mappedBy = "company")
//    lateinit var orders:Set<Order>

}

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findByUserId(id: Long): Company
}