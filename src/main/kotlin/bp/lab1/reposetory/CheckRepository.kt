package bp.lab1.reposetory

import bp.lab1.models.Receipt
import org.springframework.data.jpa.repository.JpaRepository

interface CheckRepository : JpaRepository<Receipt, Long> {
    fun findByOrder_User_Id(id: Long): List<Receipt>
}