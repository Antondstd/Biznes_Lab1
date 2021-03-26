package bp.lab1.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "BL_Item")
class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    var id: Long = 0

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    lateinit var order: Set<Order>

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    lateinit var company: Company

    @Column(name = "category", nullable = false)
    var category: Int = 1

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "price", nullable = false)
    var price: Double = 0.0

    @Column(name = "description", nullable = false)
    lateinit var description: String


}