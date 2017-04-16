package com.testwithspring.intermediate.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FormExampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormExampleController.class);

    private static final String MODEL_ATTRIBUTE_FORM = "form";

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String renderFormView(Model model) {
        LOGGER.info("Rendering form view");

        FormDTO form = new FormDTO();
        model.addAttribute(MODEL_ATTRIBUTE_FORM, form);

        return "form/view";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@ModelAttribute(MODEL_ATTRIBUTE_FORM) FormDTO form) {
        LOGGER.info("Processing form submission");
        return "form/result";
    }
}
