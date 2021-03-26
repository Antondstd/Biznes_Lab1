package bp.lab1.reposetory

import bp.lab1.models.Notify
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotifyRepository : JpaRepository<Notify, Long> {
    fun findByCreatedTimeBefore(date: Date): List<Notify>
    fun findByCreatedTimeBeforeAndOrder_Item_Company_IdNot(date: Date, company_id: Long): List<Notify>
    fun findByOrder_Id(id: Long): Notify?
}