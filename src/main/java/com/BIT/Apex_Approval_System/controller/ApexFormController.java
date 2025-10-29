package com.BIT.Apex_Approval_System.controller;

import com.BIT.Apex_Approval_System.model.ApexForm;
import com.BIT.Apex_Approval_System.service.ApexFormService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "null"}, allowCredentials = "true", allowedHeaders = "*")
public class ApexFormController {

    private final ApexFormService service;


    public ApexFormController(ApexFormService service) {
        this.service = service;
    }

    @PostMapping
    public ApexForm createForm(@RequestBody ApexForm form) {
        return service.saveForm(form);
    }

    @GetMapping
    public List<ApexForm> getAllForms() {
        return service.getAllForms();
    }

    @GetMapping("/{id}")
    public ApexForm getFormById(@PathVariable Long id) {
        return service.getFormById(id);
    }

    @PutMapping("/{id}/status")
    public ApexForm updateFormStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }



    @PutMapping("/{id}/forward")
    public ApexForm forwardForm(@PathVariable Long id, @RequestParam String forwarderName) {
        return service.forwardForm(id, forwarderName);
    }

    @PutMapping("/{id}/recommend")
    public ApexForm recommendForm(@PathVariable Long id, @RequestParam String recommenderName) {
        return service.recommendForm(id, recommenderName);
    }

    @PutMapping("/{id}/approve")
    public ApexForm approveForm(
            @PathVariable Long id,
            @RequestParam String approverName,
            @RequestParam(required = false) String remarks,
            @RequestParam(defaultValue = "true") boolean approved
    ) {
        return service.approveForm(id, approverName, remarks, approved);
    }
}
