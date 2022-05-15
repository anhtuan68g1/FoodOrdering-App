package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.aptech.sem4prj.entity.Order;
import vn.aptech.sem4prj.repository.ReportRepository;
import vn.aptech.sem4prj.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class ReportController {
    @Autowired
    private OrderService service;
    @Autowired
    private ReportRepository repo;

    @GetMapping(value = "/be/report")
    public String list(Model model) {
        List<Order> list = service.findAll();
        Double revenue = 0.0;
        for (Order o : list) {
            revenue += o.getTotal_price();
        }
        model.addAttribute("list", list);
        model.addAttribute("revenue", revenue);
        return "be/report/index";
    }

    @GetMapping("/be/report/search")
//    , @RequestParam(value = "from", required = false) String from,
//    @RequestParam(value = "to", required = false) String to
    public String search(Model model, HttpServletRequest request, @RequestParam(value = "from", required = false) String from,
                         @RequestParam(value = "to", required = false) String to) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        /*handle day + 1*/
        Date temp = df.parse(to);
        LocalDate localDate = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth() + 1;
        String handleDay = year + "-" + month + "-" + day;
        /* /.handle day + 1*/
        List<Order> list = repo.findByOrderDate(df.parse(from), df.parse(handleDay));
        Double revenue = 0.0;
        for (Order o : list) {
            revenue += o.getTotal_price();
        }
        model.addAttribute("list", list);
        model.addAttribute("revenue", revenue);
        return "be/report/index";
//        if (from != null && to != null) {
//            List<Order> líst = repo.findByOrderDate(from, to);
//            model.addAttribute("list", líst);
//            return "be/report/index";
//        } else {
//            if (to == null) {
//                List<Order> líst = repo.findByOrderDate(from, Calendar.getInstance().getTime());
//                model.addAttribute("list", líst);
//                return "be/report/index";
//            } else {
//                List<Order> líst = repo.findByOrderDate(df.parse("01-01-2000"), to);
//                model.addAttribute("list", líst);
//                return "be/report/index";
//            }
//        }
    }

}
