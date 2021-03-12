package bp.lab1.controllers

import bp.lab1.models.*
import bp.lab1.reposetory.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit

@RestController
class Contr {

    @Autowired
    lateinit var userReposetory: UserRepository

    @Autowired
    lateinit var itemRepository: Item1Repository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var checkRepository: CheckRepository

    @Autowired
    lateinit var notifyRepository: NotifyRepository

    @RequestMapping("/")
    fun home(): String {
        return "Hi"
    }

    @PutMapping("/action/addcompany")
    @ResponseBody
    fun addCompany(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "phone") phoneNumber: String
    ): Answer {
        var userDet = SecurityContextHolder.getContext().authentication.principal as MyUserDetails
        println(userDet)
        var company = Company()
        company.name = name
        company.phoneNumber = phoneNumber
        company = companyRepository.save(company)
        userDet.user.company = company
        userReposetory.save(userDet.user)
        val answer = Answer(201, "Successfully created a company ${company.name} for user")
        return answer
    }

    @Scheduled(cron = "0 0/2 * * * *")
    fun checkNotifications() {
        var listNotify =
            notifyRepository.findByCreatedTimeBefore(Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1)))
        var listItems: List<Item>
        for (notify in listNotify) {
            listItems = itemRepository.findByCategoryEqualsAndCompany_IdNot(
                notify.order.item.category,
                notify.order.item.company.id
            )
            if (listItems.size > 0) {
                for (item in listItems) {
                    var order = Order()
                    order.created_time = Date()
                    order.user = notify.user
                    order.item = item
                    orderRepository.save(order)
                }
            }
            notifyRepository.delete(notify)
        }
    }


    @PostMapping("/action/check/payed")
    @ResponseBody
    fun setPayed(@RequestParam(value = "id") id: Long): Answer {
        val order = orderRepository.findByCheckId(id)
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.user.id == userDet.user.id) {
            order.check!!.status = true
            order.status = StatusOrder.Payed
            orderRepository.save(order)
            return Answer(202, "Order of item ${order.item.name} is change to: payed")
        } else
            return Answer(401, "You cannot change the status of that order")
    }

    @GetMapping("/show/checks")
    @ResponseBody
    fun getChecks(@AuthenticationPrincipal userDetails: MyUserDetails): List<Check> =
        checkRepository.findByOrder_User_Id(userDetails.user.id)


    @RequestMapping("/test")
    fun test(): String {
        var auth = SecurityContextHolder.getContext().authentication
        return "Hello ${auth.name}"
    }


}