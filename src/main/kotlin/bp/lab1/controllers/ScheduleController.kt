package bp.lab1.controllers

import bp.lab1.models.Item
import bp.lab1.models.Order
import bp.lab1.reposetory.ItemRepository
import bp.lab1.reposetory.NotifyRepository
import bp.lab1.reposetory.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import java.util.*
import java.util.concurrent.TimeUnit


class ScheduleController {


    @Autowired
    lateinit var itemRepository: ItemRepository

    @Autowired
    lateinit var orderRepository: OrderRepository


    @Autowired
    lateinit var notifyRepository: NotifyRepository


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


}