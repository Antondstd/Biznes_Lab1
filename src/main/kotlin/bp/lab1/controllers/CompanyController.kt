package bp.lab1.controllers

import bp.lab1.models.Company
import bp.lab1.models.MyUserDetails
import bp.lab1.reposetory.CompanyRepository
import bp.lab1.reposetory.UserRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "Company Controller")
class CompanyController {
    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var userReposetory: UserRepository

    @ApiOperation(value = "Add company")
    @PostMapping("/company")
    @ResponseBody
    fun addCompany(
        @RequestBody companyInformation: Company
    ): ResponseEntity<String> {
        var userDet = SecurityContextHolder.getContext().authentication.principal as MyUserDetails
        println(userDet)
        var company = Company()
        company.name = companyInformation.name
        company.phoneNumber = companyInformation.phoneNumber
        company = companyRepository.save(company)
        userDet.user.company = company
        userReposetory.save(userDet.user)
        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully created a company ${company.name} for user")
    }
}