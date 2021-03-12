package bp.lab1.controllers

import bp.lab1.models.*
import bp.lab1.reposetory.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserContrl {
    @Autowired
    lateinit var userReposetory: UserRepository

    @PutMapping("/adduser")
    fun adduser(
        @RequestParam(value = "email") email: String,
        @RequestParam(value = "password") password: String
    ): Answer {
        var user = User()
        user.email = email
        user.password = password
        user.role = "ROLE_REGISTERED"
        userReposetory.save(user)
        return Answer(202, "Successfully created user ${email}")
    }
}