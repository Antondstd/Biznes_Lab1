package bp.lab1.controllers

import bp.lab1.models.User
import bp.lab1.reposetory.UserRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@Api(value = "Users Controller")
class UserController {
    @Autowired
    lateinit var userReposetory: UserRepository

    @PostMapping("/user")
    @ApiOperation(value = "Add User")
    fun adduser(
        @RequestBody userInformation: User
    ): ResponseEntity<String> {
        var user = User()
        user.email = userInformation.email
        user.password = userInformation.password
        user.role = "ROLE_REGISTERED"
        userReposetory.save(user)
        return ResponseEntity.status(HttpStatus.OK).body("Successfully created user ${user.email}")
    }

    @RequestMapping("/profile")
    fun profile(): String {
        var auth = SecurityContextHolder.getContext().authentication
        return "Hello ${auth.name}"
    }
}