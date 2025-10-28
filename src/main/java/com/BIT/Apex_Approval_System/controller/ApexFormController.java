package com.BIT.Apex_Approval_System.controller;

import com.BIT.Apex_Approval_System.model.ApexForm;
import com.BIT.Apex_Approval_System.service.ApexFormService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")  // ✅ Add this line
@CrossOrigin(origins = "*")    // ✅ Optional: allows frontend access
public class ApexFormController {

    private final ApexFormService service;

    // ✅ Constructor injection (recommended)
    public ApexFormController(ApexFormService service) {
        this.service = service;
    }

    // ============================================================
    // 📌 Faculty Endpoints
    // ============================================================

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

    // ============================================================
    // 📌 Forwarder / Admin Endpoints
    // ============================================================

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
