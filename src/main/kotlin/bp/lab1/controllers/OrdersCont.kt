package bp.lab1.controllers

import bp.lab1.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class OrdersCont {

    @Autowired
    lateinit var itemRepository: Item1Repository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var checkRepository: CheckRepository

    @Autowired
    lateinit var notifyRepository: NotifyRepository


    @PutMapping("/action/order")
    @ResponseBody
    fun orderItem(@RequestParam(value = "id") id: Long, @RequestParam(value = "notify") notify: Int): Answer {
        val item = itemRepository.findById(id).get()
        var order = Order()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (item.company.user.id == userDet.user.id)
            return Answer(402, "You can't buy your own product")
        order.item = item
        order.user = userDet.user
        order.created_time = Date()
        order = orderRepository.save(order)
        if (notify == 1) {
            val notif = Notify()
            notif.createdTime = Date()
            notif.user = userDet.user
            notif.order = order
            notifyRepository.save(notif)
        }
        val answer = Answer(
            202,
            "Successfully ordered item: ${item.name} from company ${item.company.name} , Order number is ${order.id}"
        )
        return answer
    }


    @PostMapping("/action/order/changestatus")
    @ResponseBody
    fun changeOrderStatus(
        @RequestParam(value = "id") id: Long,
        @RequestParam(value = "newstate") newstate: Int
    ): Answer {
        val order = orderRepository.findById(id).get()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.status.ordinal > newstate) return Answer(
            401,
            "You cannot change the status of that order to lower level"
        )
        if (order.user.id == userDet.user.id) {
            when (newstate) {
                2, 4, 6 -> {
                    order.status = StatusOrder.values()[newstate]
                    orderRepository.save(order)
                    return Answer(202, "Order of item ${order.item.name} is change to status ${newstate}")
                }
                else -> return Answer(400, "Bad Status")
            }
        }
        if (order.item.company.user.id == userDet.user.id) {
            when (newstate) {
                1 -> {
                    order.status = StatusOrder.values()[newstate]
                    orderRepository.save(order)
                    var notify = notifyRepository.findByOrder_Id(order.id)
                    if (notify != null)
                        notifyRepository.delete(notify)
                    return Answer(202, "Order of item ${order.item.name} is change to status ${newstate}")
                }
                2, 5, 7, 8 -> {
                    order.status = StatusOrder.values()[newstate]
                    orderRepository.save(order)
                    return Answer(202, "Order of item ${order.item.name} is change to status ${newstate}")
                }
                else -> return Answer(400, "Bad Status")
            }
        }
        return Answer(401, "You cannot change the status of that order, You dont have a connection with it")
    }

    @RequestMapping("/action/order/pay")
    @ResponseBody
    fun setAwaitPay(@RequestParam(value = "id") id: Long): Answer {
        val order = orderRepository.findById(id).get()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.user.id == userDet.user.id) {
            order.status = StatusOrder.AwaitsPayment
            var check = Check()
            check.created_time = Date()
            check.order = order
            check = checkRepository.save(check)
            order.check = check
            orderRepository.save(order)
            return Answer(
                202, "Order of item ${order.item.name} is change to status: Awaits payment" +
                        " Check id: ${check.id}"
            )
        } else
            return Answer(401, "You cannot change the status of that order")
    }

    @PostMapping("/action/order/received")
    @ResponseBody
    fun setReceived(@RequestParam(value = "id") id: Long): Answer {
        val order = orderRepository.findById(id).get()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.user.id == userDet.user.id) {
            order.status = StatusOrder.Received
            orderRepository.save(order)
            return Answer(202, "Order of item ${order.item.name} is change to: received")
        } else
            return Answer(401, "You cannot change the status of that order")
    }

    @GetMapping("/show/sellerorders")
    @ResponseBody
    fun getAllSellOrders(response: HttpServletResponse): List<Order>? {
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (userDet.user.company?.id == null) {
            response.sendError(402, "Вы не являетесь продавцом")
            return null
        }
        return orderRepository.findByItem_Company_Id(userDet.user.company!!.id)
    }

    @GetMapping("/show/buyingorders")
    @ResponseBody
    fun getAllBuyingOrders(): List<Order> {
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        return orderRepository.findByUser_Id(userDet.user.id)
    }


}