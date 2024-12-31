package com.jsp.Bhoomaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsp.Bhoomaster.dto.Booked;
import com.jsp.Bhoomaster.dto.Property;
import com.jsp.Bhoomaster.dto.User;
import com.jsp.Bhoomaster.service.BookingService;
import com.jsp.Bhoomaster.service.PropertyService;
import com.jsp.Bhoomaster.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService; // Injecting PropertyService

    @Autowired
    private  BookingService bookingService;  //Injecting BookingService

    @GetMapping("/register")
    public String loadRegister(User user, ModelMap map) {
        return userService.register(user, map);
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult result, HttpSession session) {
        return userService.register(user, result, session);
    }

    @GetMapping("/otp/{id}")
    public String otp(@PathVariable("id") Integer id, ModelMap map) {
        map.put("id", id);
        return "user-otp.html";
    }

    @PostMapping("/otp")
    public String otp(@RequestParam("otp") int otp, @RequestParam("id") int id, HttpSession session) {
        return userService.otp(otp, id, session);
    }

    @GetMapping("/resend-otp/{id}")
    public String resendOtp(@PathVariable("id") Integer id, HttpSession session) {
        return userService.resendOtp(id, session);
    }


    //user-home- without booking status
    
//     @GetMapping("/home")
// public String loadHome(HttpSession session, ModelMap map) {
//     if (session.getAttribute("user") != null) {
//         try {
//             List<Property> properties = propertyService.getAllProperties();
//             map.addAttribute("properties", properties);
//             return "user-home.html";
//         } catch (Exception e) {
//             map.addAttribute("error", "Failed to load properties: " + e.getMessage());
//             return "user-home.html";
//         }
//     } else {
//         session.setAttribute("error", "Invalid Session, Login Again");
//         return "redirect:/login";
//     }
// }


//user with booking status

@GetMapping("/home")
public String loadHome(HttpSession session, ModelMap map) {
    if (session.getAttribute("user") != null) {
        try {
            List<Property> properties = bookingService.getAllPropertiesWithBookingStatus();
            map.addAttribute("properties", properties);
            return "user-home.html";
        } catch (Exception e) {
            map.addAttribute("error", "Failed to load properties: " + e.getMessage());
            return "user-home.html";
        }
    } else {
        session.setAttribute("error", "Invalid Session, Login Again");
        return "redirect:/login";
    }
}


// Endpoint to view the list of properties booked by the user.
 @GetMapping("/booked-properties")
    public String viewBookedProperties(HttpSession session, ModelMap map) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                List<Booked> bookedProperties = bookingService.getUserBookedProperties(user.getId());
                map.addAttribute("bookedProperties", bookedProperties);
                return "booked-properties.html";
            } catch (Exception e) {
                map.addAttribute("error", "Failed to load booked properties: " + e.getMessage());
                return "user-home.html";
            }
        } else {
            session.setAttribute("error", "Invalid Session, Login Again");
            return "redirect:/login";
        }
    }

    //  Endpoint to book a property.
    
    @PostMapping("/book-property/{propertyId}")
    public String bookProperty(@PathVariable("propertyId") int propertyId, HttpSession session, ModelMap map) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                String result = bookingService.bookProperty(user.getId(), propertyId);
                session.setAttribute("success", result);
            } catch (RuntimeException e) {
                session.setAttribute("error", "Failed to book property: " + e.getMessage());
            }
            return "redirect:/user/home";
        } else {
            session.setAttribute("error", "Invalid Session, Login Again");
            return "redirect:/login";
        }
    }

}
