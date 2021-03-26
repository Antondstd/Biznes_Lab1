package bp.lab1.controllers

import bp.lab1.models.*
import bp.lab1.reposetory.CheckRepository
import bp.lab1.reposetory.ItemRepository
import bp.lab1.reposetory.NotifyRepository
import bp.lab1.reposetory.OrderRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@Api(value = "Orders Controller")
class OrderController {

    @Autowired
    lateinit var itemRepository: ItemRepository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var checkRepository: CheckRepository

    @Autowired
    lateinit var notifyRepository: NotifyRepository

    @ApiOperation(value = "Make order")
    @PostMapping("/orders")
    fun orderItem(
        @RequestParam(value = "id") id: Long,
        @RequestParam(value = "notify") notify: Int
    ): ResponseEntity<String> {
        val item = itemRepository.findById(id).get()
        var order = Order()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (item.company.user.id == userDet.user.id)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("You cannot change the status of that order")
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
        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully ordered item: ${item.name} from company ${item.company.name} , Order number is ${order.id}")
    }


    @ApiOperation(value = "Change Order status")
    @PutMapping("/orders/{id}")
    fun changeOrderStatus(
        @PathVariable(value = "id") id: Long,
        @RequestParam("newState") newState: StatusOrder
    ): ResponseEntity<String> {
        val order = orderRepository.findById(id).get()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.status.ordinal > newState.ordinal)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("You cannot change the status of that order to lower level")
        if (order.user.id == userDet.user.id) {
            when (newState) {
                StatusOrder.ACCEPTED, StatusOrder.PAYED, StatusOrder.RECEIVED -> {
                    order.status = newState
                    orderRepository.save(order)
                    return ResponseEntity.status(HttpStatus.OK)
                        .body("Order of item ${order.item.name} is change to status ${newState}")
                }
                else -> return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cant use that state")
            }
        }
        if (order.item.company.user.id == userDet.user.id) {
            when (newState) {
                StatusOrder.INWORK -> {
                    order.status = newState
                    orderRepository.save(order)
                    var notify = notifyRepository.findByOrder_Id(order.id)
                    if (notify != null)
                        notifyRepository.delete(notify)
                    return ResponseEntity.status(HttpStatus.OK)
                        .body("Order of item ${order.item.name} is change to status ${newState}")
                }
                StatusOrder.ACCEPTED, StatusOrder.DELIVERING, StatusOrder.CANCELLED, StatusOrder.SPAM -> {
                    order.status = newState
                    orderRepository.save(order)
                    return ResponseEntity.status(HttpStatus.OK)
                        .body("Order of item ${order.item.name} is change to status ${newState}")
                }
                else -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You cant use that state")
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You cannot change the status of that order")
    }

    @ApiOperation(value = "Paying for order and create recipe")
    @PostMapping("/orders/{id}/pay")
    fun setAwaitPay(@PathVariable(value = "id") id: Long): ResponseEntity<String> {
        val order = orderRepository.findById(id).get()
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.user.id == userDet.user.id) {
            order.status = StatusOrder.AWAITSPAYMENT
            var check = Receipt()
            check.created_time = Date()
            check.order = order
            check = checkRepository.save(check)
            order.receipt = check
            orderRepository.save(order)
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    "Order of item ${order.item.name} is change to status: Awaits payment" +
                            " Check id: ${check.id}"
                )
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("You cannot change the status of that order")
    }


    @ApiOperation(value = "Get all your orders selling stuff")
    @GetMapping("/shop/orders")
    @ResponseBody
    fun getAllSellOrders(response: HttpServletResponse): List<Order>? {
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (userDet.user.company?.id == null) {
            response.sendError(402, "Вы не являетесь продавцом")
            return null
        }
        return orderRepository.findByItem_Company_Id(userDet.user.company!!.id)
    }

    @ApiOperation(value = "Get all your orders to buy")
    @GetMapping("/orders")
    @ResponseBody
    fun getAllBuyingOrders(): List<Order> {
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        return orderRepository.findByUser_Id(userDet.user.id)
    }


}