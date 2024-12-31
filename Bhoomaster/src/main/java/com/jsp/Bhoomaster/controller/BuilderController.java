package com.jsp.Bhoomaster.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.Bhoomaster.dto.Builder;
import com.jsp.Bhoomaster.dto.Property;
import com.jsp.Bhoomaster.repository.PropertyRepository;
import com.jsp.Bhoomaster.service.BuilderService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/builder")
public class BuilderController {

    @Autowired
    BuilderService builderService;

    @Autowired
    PropertyRepository propertyRepository;

    // Load the registration page
    @GetMapping("/register")
    public String loadRegister(Builder builder, ModelMap map) {
        map.put("builder", builder);
        return "builder-register.html";
    }

    // Handle builder registration
    @PostMapping("/register")
    public String register(@Valid Builder builder, BindingResult result, HttpSession session) {
        return builderService.register(builder, result, session);
    }

    // Load the OTP page
    @GetMapping("/otp/{id}")
    public String loadOtp(ModelMap map, @PathVariable("id") int id) {
        map.put("id", id);
        return "builder-otp.html";
    }

    // Verify OTP
    @PostMapping("/otp")
    public String otp(@RequestParam("id") int id, @RequestParam("otp") int otp, HttpSession session) {
        return builderService.otp(id, otp, session);
    }

    // Resend OTP
    @GetMapping("/resend-otp/{id}")
    public String resendOtp(@PathVariable("id") int id, HttpSession session) {
        return builderService.resendOtp(id, session);
    }

    // Load builder home page
    @GetMapping("/home")
    public String loadHome(HttpSession session) {
        if (session.getAttribute("builder") != null) {
            return "builder-home.html";
        } else {
            session.setAttribute("error", "Invalid Session, Login Again");
            return "redirect:/login";
        }
    }

    // Load the 'Post Property' form
    @GetMapping("/post-property")
    public String loadPostProperty(HttpSession session) {
        if (session.getAttribute("builder") != null) {
            return "post-property.html";
        } else {
            session.setAttribute("error", "Invalid session, Login Again");
            return "redirect:/login";
        }
    }

    // Handle property posting
    @PostMapping("/post-property")
    public String postProperty(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("propertyName") String propertyName,
            @RequestParam("details") String details,
            @RequestParam("location") String location,
            @RequestParam("price") int price,
            @RequestParam("year") int year,
            HttpSession session,
            ModelMap model) {
        try {
            Builder builder = (Builder) session.getAttribute("builder");
            if (builder == null) {
                model.addAttribute("error", "You must be logged in as a builder to post a property.");
                return "redirect:/login";
            }

            Property property = new Property();
            property.setPropertyName(propertyName); // Use property name from form
            property.setDetails(details);
            property.setLocation(location);
            property.setPrice(price);
            property.setYear(year);

            if (!imageFile.isEmpty()) {
                property.setImage(imageFile.getBytes());
            }

            property.setBuilder(builder);

            propertyRepository.save(property);
            model.addAttribute("success", "Property posted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error while posting property: " + e.getMessage());
        }
        return "redirect:/builder/home";
    }

    // Image Fetching Controller
    @GetMapping("/property-image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getPropertyImage(@PathVariable Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    
        byte[] image = property.getImage();
        if (image == null || image.length == 0) {
            System.out.println("No image found for property ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        System.out.println("Image fetched successfully for property ID: " + id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
    
    

    // Manage properties
    @GetMapping("/manage-properties")
    public String manageProperties(HttpSession session, ModelMap map) {
        if (session.getAttribute("builder") != null) {
            Builder builder = (Builder) session.getAttribute("builder");
            List<Property> propertyList = propertyRepository.findByBuilder(builder);
            if (propertyList.isEmpty()) {
                session.setAttribute("failure", "No properties Added Yet");
                return "redirect:/builder/home";
            } else {
                map.put("properties", propertyList);
                return "builder-properties.html";
            }
        } else {
            session.setAttribute("error", "Invalid Session, Login Again");
            return "redirect:/login";
        }
    }

    // Delete property
    @GetMapping("/delete-property/{id}")
    public String deleteProperty(@PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("builder") != null) {
            propertyRepository.deleteById(id);
            session.setAttribute("success", "Property Removed Successfully");
            return "redirect:/builder/manage-properties";
        } else {
            session.setAttribute("error", "Invalid Session, Login Again");
            return "redirect:/login";
        }
    }
}
