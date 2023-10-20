package com.sunBaseData.Assignment.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunBaseData.Assignment.Model.Customer;
import com.sunBaseData.Assignment.Model.User;
import com.sunBaseData.Assignment.Services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private ApiService apiService;
    private String token;

    // --------------------------------- Login Page ------------------------------------------------------------------//
    @GetMapping("/")
    public String login(Model model)
    {
        model.addAttribute("cred", new User());
        return "Login";
    }
    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute User user)
    {
        token = apiService.authenticateUser(user.getLoginId(), user.getPassword());
        if(token != null)
        {
            return "redirect:/customerList";
        }
        else
            return "redirect:/";
    }

    // -------------------------------- Add New Customer -------------------------------------------------------------//
    @GetMapping("/addCustomer")
    public String addCustomer(Model model)
    {
        model.addAttribute("data", new Customer());
        return "Add_customer";
    }
    @PostMapping("/createCustomer")
    public String createCustomer(@ModelAttribute Customer customer)
    {
        ResponseEntity<String> response = apiService.createNewCustomer(token, customer);
        return "redirect:/addCustomer";
    }

    // ------------------------------- Customer List -----------------------------------------------------------------//

    @GetMapping("/customerList")
    public String customerList(Model model)
    {
        model.addAttribute("customerList", new Customer());
        ResponseEntity<String> response = apiService.displayCustomerList(token);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Customer> customerList = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                customerList = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                });
                System.out.println(customerList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            // Handle non-successful response if needed
        }
        model.addAttribute("customerList", customerList);
        return "Customer_details";
    }

    // ------------------------------------------- Delete ------------------------------------------------------------

    @GetMapping("/deleteCustomer/{id}")
    public String Delete(@PathVariable(value = "id") String id, RedirectAttributes redirectAttributes){
        apiService.deleteCustomer(id);
        return "redirect:/customerList";
    }

    // ---------------------------------------------Edit--------------------------------------------------------------

    @PostMapping("/editCustomer/{id}")
    public String Edit(@PathVariable String id, Model model){
        return "redirect:/customerList";
    }
}

