package bp.lab1.controllers

import bp.lab1.models.MyUserDetails
import bp.lab1.models.Receipt
import bp.lab1.models.StatusOrder
import bp.lab1.reposetory.CheckRepository
import bp.lab1.reposetory.OrderRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@Api(value = "Receipts Controller")
class ReceiptController {

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var checkRepository: CheckRepository

    @ApiOperation(value = "Set receipt as paid")
    @PostMapping("/receipts/{id}/payed")
    @ResponseBody
    fun setPayed(@PathVariable(value = "id") id: Long): ResponseEntity<String> {
        val order = orderRepository.findByReceiptId(id)
        val userDet = (SecurityContextHolder.getContext().authentication.principal as MyUserDetails)
        if (order.user.id == userDet.user.id) {
            order.receipt!!.isPayed = true
            order.status = StatusOrder.PAYED
            orderRepository.save(order)
            return ResponseEntity.status(HttpStatus.OK)
                .body("Order of item ${order.item.name} is change to: payed")
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("You cannot change the status of that order")
    }

    @ApiOperation(value = "Get all your receipts to buy")
    @GetMapping("/receipts")
    @ResponseBody
    fun getChecks(@AuthenticationPrincipal userDetails: MyUserDetails): List<Receipt> =
        checkRepository.findByOrder_User_Id(userDetails.user.id)
}