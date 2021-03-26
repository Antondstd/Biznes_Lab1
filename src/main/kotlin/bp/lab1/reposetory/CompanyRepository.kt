package bp.lab1.reposetory

import bp.lab1.models.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findByUserId(id: Long): Company
}