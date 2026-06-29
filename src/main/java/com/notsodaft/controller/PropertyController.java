package com.notsodaft.controller;

import com.notsodaft.model.Property;
import com.notsodaft.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Controller
public class PropertyController{
    private final PropertyService propertyService;
    private final ObjectMapper objectMapper;

    public PropertyController(PropertyService propertyService, ObjectMapper objectMapper){
        this.propertyService = propertyService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model){
        try{
            List<Property> properties = propertyService.getVerifiedProperties();
            model.addAttribute("propertiesJson", objectMapper.writeValueAsString(properties));
        }catch (Exception e){
            model.addAttribute("propertiesJson", "[]");
        }
        return "index";
    }
}