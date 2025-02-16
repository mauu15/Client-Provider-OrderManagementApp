package com.example.controllers;

import com.example.models.*;
import com.example.ORM.*;
import com.example.services.CategoryService;
import com.example.services.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreDAO storeDAO;
    @Autowired
    private OrderDetailsDAO orderDetailsDAO;
    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MeasureUnitsDAO measureUnitsDAO;
    @Autowired
    private SubcategoryDAO subcategoryDAO;

    @GetMapping("/delete_product")
    public String deleteProduct(@RequestParam("productId") Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Store store = storeDAO.findStoreById(id);
        if (store == null) {
            redirectAttributes.addFlashAttribute("error", "Prodotto non trovato");
            return "redirect:/homepage";
        }

        List<Order_Details> orderDetails = orderDetailsDAO.findByStore(store);
        for (Order_Details orderDetail : orderDetails) {
            if (orderDetail.getStatus().equalsIgnoreCase("S")) {
                redirectAttributes.addFlashAttribute("error", "Non puoi eliminare questo prodotto perchè c'è un ordine in sospeso con all'interno lo stesso, concludi l'ordine prima di procedere");
                return "redirect:/homepage";
            }
        }

        orderDetailsDAO.deleteAll(orderDetails);
        List<Cart> carts = cartDAO.findByProduct(store);
        cartDAO.deleteAll(carts);
        storeDAO.delete(store);

        redirectAttributes.addFlashAttribute("message", "Prodotto eliminato con successo");
        return "redirect:/homepage";
    }

    @GetMapping("/update_product")
    public String updateProduct(@RequestParam("productId") Long id, Model model, HttpSession session) {
        Store store = storeDAO.findStoreById(id);
        session.setAttribute("store", store);
        model.addAttribute("store", store);
        return "/provider/modify_product_page";
    }

    @GetMapping("/update")
    public String update(@RequestParam("quantity") int quantity,@RequestParam("price") Double price,@RequestParam("desc") String desc,@RequestParam("discount") int discount, RedirectAttributes redirectAttributes, HttpSession session) {
        Store store = (Store) session.getAttribute("store");
        store.setAvailableQuantity(quantity);
        store.setDiscount(discount);
        store.setDescProd(desc);
        store.setPriceProduct(price);
        boolean updateSuccess = storeService.updateStore(store); // Metodo del servizio per aggiornare nel DB

        if (updateSuccess) {
            redirectAttributes.addFlashAttribute("message", "Dati aggiornati con successo.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Errore durante l'aggiornamento.");
        }
        return "redirect:/homepage";
    }

    @GetMapping("/add_product")
    public String addProduct(Model model, HttpSession session) {
        List<Category> categorie = categoryService.getAllCategorie();
        List<Measure_Unit> measureUnits = measureUnitsDAO.findAll();
        model.addAttribute("categorie", categorie);
        model.addAttribute("unita_misura", measureUnits);
        return "provider/insert_product_page";
    }
    @GetMapping("/add")
    public String add(@RequestParam("subcategories") Long subcategoryId,@RequestParam("quantity") int quantity,@RequestParam("discount") int discount,@RequestParam("desc") String desc,@RequestParam("price") Double price,@RequestParam("unita_misura") Long unitamisuraId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Subcategory subcategory = subcategoryDAO.findById(subcategoryId).get();
        Measure_Unit measureUnit = measureUnitsDAO.findById(unitamisuraId).get();
        Store store = new Store(quantity,price,desc,subcategory,user,discount,measureUnit);
        storeDAO.save(store);
        model.addAttribute("message", "Prodotto aggiunto con successo");
        return "redirect:/homepage";
    }
}
