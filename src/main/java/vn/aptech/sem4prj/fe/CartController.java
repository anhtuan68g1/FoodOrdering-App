package vn.aptech.sem4prj.fe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.entity.*;
import vn.aptech.sem4prj.service.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class CartController {
    @Autowired
    private ProductService service;

    @Autowired
    private UserAddressService addressService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/add-cart/{id}")
    public String addCart(Model model,
                          HttpServletRequest request,
                          @PathVariable("id") String id) {
        // tạo đối tượng HttpSession từ request
        HttpSession session = request.getSession();
        List<CartItems> cart;
        if (session.getAttribute("cart") != null) {
            // nếu có cart trong session, gán vào đối tượng cart
            cart = (List<CartItems>) session.getAttribute("cart");
        } else {
            // ngược lại thì tạo mới
            cart = new ArrayList<>();
        }
        service.findById(id).ifPresent(p -> {
            boolean founded = false;
            for (CartItems item : cart) {
                if (item.getProductId().getId().equals(p.getId())) {
                    item.setQuantity(item.getQuantity() + 1);
                    founded = true;
                    break;
                }
            }
            if (!founded) {
                CartItems ci = new CartItems();
                ci.setProductId(p);
                ci.setQuantity(1);
                cart.add(ci);   // thêm vào giỏ hàng
            }
            // lưu giỏ hàng vào session
            session.setAttribute("cart", cart);
        });
        return "redirect:/menu";    // chọn xong quay về trang chủ
    }

    @GetMapping(value = "/plus-qty/{id}")
    public String plusQty(HttpServletRequest request,
                          @PathVariable("id") String id) {
        HttpSession session = request.getSession();
        List<CartItems> list = (List<CartItems>) session.getAttribute("cart");
        for (CartItems ci : list) {
            if (ci.getProductId().getId().equals(id)) {
                ci.setQuantity(ci.getQuantity() + 1);
            }
        }
        session.setAttribute("cart", list);
        return "redirect:/cart-list";
    }

    @GetMapping(value = "/minus-qty/{id}")
    public String minusQty(HttpServletRequest request,
                           @PathVariable("id") String id) {
        HttpSession session = request.getSession();
        List<CartItems> list = (List<CartItems>) session.getAttribute("cart");
        for (CartItems ci : list) {
            if (ci.getProductId().getId().equals(id)) {
                if (ci.getQuantity() == 1) {
                    list.remove(ci);
                    session.setAttribute("cart", list);
                    return "redirect:/cart-list";
                }
                ci.setQuantity(ci.getQuantity() - 1);
            }
        }
        session.setAttribute("cart", list);
        return "redirect:/cart-list";
    }

    @GetMapping(value = "/remove/{id}")
    public String removeItem(HttpServletRequest request,
                             @PathVariable("id") String id) {
        HttpSession session = request.getSession();
        List<CartItems> list = (List<CartItems>) session.getAttribute("cart");
        list.removeIf(ci -> ci.getProductId().getId().equals(id));
        session.setAttribute("cart", list);
        return "redirect:/cart-list";
    }

    @PostMapping(value = "/save-order")
    public String saveOrder(HttpServletRequest request, @ModelAttribute("uAddress") UserAddress address) {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        List<CartItems> list = (List<CartItems>) session.getAttribute("cart");
        Double total = 0.0;
        for (CartItems c : list) {
            total += (c.getProductId().getPrice() * c.getQuantity());
        }
        String addressOrder = address.getReceiver() + ", " + address.getAddress() + ", " + address.getPhone();
        System.out.println("phone: " + address.getPhone());
        Order newOrder = new Order(null, user, "COD", "Accept", null, total, addressOrder);
        orderService.save(newOrder);
        System.out.println(newOrder.getTotal_price());
        list.forEach(ci -> {
                    Double totalPrice = ci.getQuantity() * ci.getProductId().getPrice();
                    OrderDetails od = new OrderDetails(
                            null,
                            newOrder,
                            ci.getProductId(),
                            ci.getQuantity(),
                            totalPrice);
                    orderDetailsService.save(od);
                }
        );
        session.removeAttribute("cart");
        return "redirect:/success";
    }

    @GetMapping(value = "/success")
    public String successs(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        return "fe/success";
    }

    @GetMapping(value = "/check-out")
    public String checkOut(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        Users user = (Users) request.getSession().getAttribute("user");
        List<UserAddress> list = addressService.findByUserId(user.getId());
        UserAddress get = null;
        for (UserAddress u : list) {
            get = u;
        }
        if (get != null) {
            model.addAttribute("uAddress", get);
            return "fe/shop_checkout";
        }
        model.addAttribute("uAddress", new UserAddress());
        return "fe/shop_checkout";
    }

    @GetMapping(value = "/choose-address/{id}")
    public String chooseAddress(Model model, HttpServletRequest request, @PathVariable int id) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        model.addAttribute("address_list", addressService.findByUserId(id));
        return "fe/choose_address";
    }

    @GetMapping(value = "/new-address")
    public String addNewAddress(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        model.addAttribute("uAddress", new UserAddress());
        return "fe/new_address";
    }

    @PostMapping(value = "/save-address")
    public String saveAddress(Model model,
                              HttpServletRequest request,
                              @ModelAttribute("uAddress") UserAddress newAddress,
                              RedirectAttributes redirectAttributes) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        HttpSession session = request.getSession();

        Users user = (Users) session.getAttribute("user");
        newAddress.setUserId(user);
        addressService.save(newAddress);
        redirectAttributes.addFlashAttribute("message", "Create successfully!!!.");
        return "redirect:/check-out";
    }

    @GetMapping(value = "/edit-address/{id}")
    public String editAddress(Model model, HttpServletRequest request, @PathVariable int id) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        model.addAttribute("edit_address", addressService.findById(id).get());
        return "fe/edit_address";
    }

    @PostMapping(value = "/save-edit")
    public String saveEditAddress(Model model,
                                  HttpServletRequest request,
                                  @ModelAttribute("edit_address") UserAddress newAddress,
                                  RedirectAttributes redirectAttributes) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        HttpSession session = request.getSession();

        Users user = (Users) session.getAttribute("user");
        addressService.update(newAddress.getId(), newAddress);
        redirectAttributes.addFlashAttribute("message", "Update successfully!!!.");
        return "redirect:/choose-address/" + user.getId();
    }

    @GetMapping(value = "/remove-address/{id}")
    public String removeAddress(@PathVariable int id) {
        addressService.deleteById(id);
        return "redirect:/account-info";
    }
}
