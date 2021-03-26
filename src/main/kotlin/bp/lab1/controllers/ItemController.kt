package bp.lab1.controllers

import bp.lab1.models.Item
import bp.lab1.models.MyUserDetails
import bp.lab1.reposetory.CompanyRepository
import bp.lab1.reposetory.ItemRepository
import bp.lab1.reposetory.UserRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@Api(value = "Items Controller")
class ItemController {
    @Autowired
    lateinit var userReposetory: UserRepository

    @Autowired
    lateinit var itemRepository: ItemRepository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @ApiOperation(value = "Get all items")
    @GetMapping("/items")
    @ResponseBody
    fun getAllitems(): MutableList<Item> {
        return itemRepository.findAll()
    }

    @ApiOperation(value = "Add item")
    @PostMapping("/items")
    fun addItem(
        @RequestBody itemInformation: Item
    ): ResponseEntity<String> {
        var userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        var user = userReposetory.findById(userDet.getId()).get()
        if (user.company == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("You dont have a company to sell products")
        var item = Item()
        item.name = itemInformation.name
        item.price = itemInformation.price
        item.description = itemInformation.description
        item.company = companyRepository.findByUserId(user.id)
        item.category = itemInformation.category
        itemRepository.save(item)
        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully added item ${item.name} by company ${item.company.name}")
    }
}