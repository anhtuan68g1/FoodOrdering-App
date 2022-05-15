package vn.aptech.sem4prj.fe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import vn.aptech.sem4prj.entity.OrderDetails;
import vn.aptech.sem4prj.entity.UserAddress;
import vn.aptech.sem4prj.service.OrderDetailsService;
import vn.aptech.sem4prj.service.OrderService;
import vn.aptech.sem4prj.service.UserAddressService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerOrderController {
    @Autowired
    private OrderDetailsService detailsService;
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/view-order/{id}")
    public String viewOrder(Model model, HttpServletRequest request, @PathVariable("id") int orderId){
        HomeController a = new HomeController();
        a.storeSession(model, request);
        List<OrderDetails> listDetail = detailsService.getListByOrderId(orderId);
        model.addAttribute("listDetail", listDetail);
        model.addAttribute("order", orderService.findById(orderId).get());
        return "fe/view_order";
    }
}
