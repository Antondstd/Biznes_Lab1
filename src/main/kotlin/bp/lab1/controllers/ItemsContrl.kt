package bp.lab1.controllers

import bp.lab1.models.*
import bp.lab1.reposetory.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class ItemsContrl {
    @Autowired
    lateinit var userReposetory: UserRepository

    @Autowired
    lateinit var itemRepository: Item1Repository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @GetMapping("/allitems")
    @ResponseBody
    fun getAllitems(): MutableList<Item> {
        return itemRepository.findAll()
    }

    @PutMapping("/action/additem")
    @ResponseBody
    fun addItem(
        @RequestParam(value = "name") name: String, @RequestParam(value = "price") price: Double,
        @RequestParam(value = "disc") discription: String,
        @RequestParam(value = "cat") category: Int
    ): Answer {
        var userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        var user = userReposetory.findById(userDet.getId()).get()
        if (user.company == null)
            return Answer(401, "You dont have a company to sell products")
        var item = Item()
        item.name = name
        item.price = price
        item.description = discription
        item.company = companyRepository.findByUserId(user.id)
        item.category = category
        itemRepository.save(item)
        val answer = Answer(201, "Successfully added item ${item.name} by company ${item.company.name}")
        return answer
    }
}