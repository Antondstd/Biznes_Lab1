package bp.lab1.reposetory

import bp.lab1.models.Item
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ItemRepository: JpaRepository<Item, Long> {
    override fun findById(id:Long): Optional<Item>
    fun findByCategoryEquals(cat: Int):List<Item>
    fun findByCategoryEqualsAndCompany_IdNot(cat: Int,company_id:Long):List<Item>
}