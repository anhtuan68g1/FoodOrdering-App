package vn.aptech.sem4prj.fe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.sem4prj.dto.ProductDto;
import vn.aptech.sem4prj.entity.CartItems;
import vn.aptech.sem4prj.entity.Product;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.service.CartItemsService;
import vn.aptech.sem4prj.service.ProductService;
import vn.aptech.sem4prj.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;


    public void storeSession(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Users user = (Users) session.getAttribute("user");

        if (session.getAttribute("cart") != null) {
            List<CartItems> items = (List<CartItems>) session.getAttribute("cart");
            Integer count = 0;
            Double total = 0.0;
            for (CartItems c : items) {
                total += (c.getProductId().getPrice() * c.getQuantity());
                count += 1;
            }
            model.addAttribute("count", count);
            model.addAttribute("total", total);
            model.addAttribute("list", items);
        }

        model.addAttribute("get", user);
    }

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/checkLogin";
    }

    @GetMapping("/index")
    public String home(Model model, HttpServletRequest request) {
        storeSession(model, request);
        model.addAttribute("products", productService.findAll());
        return "fe/index_slider";
    }

    @GetMapping("/404")
    public String error(Model model) {
        return "fe/404";
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpServletRequest request) {
        storeSession(model, request);
        model.addAttribute("products", productService.findAll());
        return "fe/menu";
    }

    @GetMapping("/product-details/{id}")
    public String menuDetails(Model model, HttpServletRequest request, @PathVariable String id) {
        storeSession(model, request);
        Product details = productService.findById(id).get();
        model.addAttribute("product", details);
        List<ProductDto> related = productService.findByCateName(details.getCategoryId().getName());
        if (!related.isEmpty()) {
            related.removeIf(c -> c.getId().equals(details.getId()));
        }
        model.addAttribute("related_product", related);
        return "fe/product_details";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        storeSession(model, request);
        return "fe/about";
    }

    @GetMapping("/cart-list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        storeSession(model, request);
        //store cart cookies
//        List<CartItems> cart = (List<CartItems>) request.getSession().getAttribute("cart");
//        Users u = (Users) request.getSession().getAttribute("user");
//        Cookie[] cookies = request.getCookies();
//        List<CartItems> productList = new ArrayList<>();
//        for (Cookie o : cookies) {
//            if (o.getName().equals("cart")) {
//                String[] txt = o.getValue().split(",");
//                Product pt = productService.findById(txt[0]).get();
//                productList.stream().map(p ->
//                        productList.add(new CartItems(u.getId(), txt[0], txt[1], pt.getPrice() * Integer.parseInt(txt[1])))
//                ).collect(Collectors.toList());
//            }
//        }
        return "fe/cart";
    }
}
