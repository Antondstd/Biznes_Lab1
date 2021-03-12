package bp.lab1.reposetory

import bp.lab1.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository:JpaRepository<User,Long> {
    fun findByEmail(email:String):User
}