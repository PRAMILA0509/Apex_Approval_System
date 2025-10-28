package com.BIT.Apex_Approval_System.service;

import com.BIT.Apex_Approval_System.model.ApexForm;
import com.BIT.Apex_Approval_System.repository.ApexFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApexFormService {



    @Autowired
    private ApexFormRepository repo;

    public ApexForm saveForm(ApexForm form) {
        return repo.save(form);
    }

    public List<ApexForm> getAllForms() {
        return repo.findAll();
    }

    public ApexForm getFormById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public ApexForm updateStatus(Long id, String status) {
        ApexForm form = repo.findById(id).orElse(null);
        if (form != null) {
            form.setStatus(status);
            repo.save(form);
        }
        return form;
    }
    public ApexForm forwardForm(Long id, String forwarderName) {
        ApexForm form = repo.findById(id).orElse(null);
        if (form != null && form.getStatus().equals("SUBMITTED")) {
            form.setStatus("FORWARDED");
            form.setForwardedBy(forwarderName);
            form.setForwardedAt(LocalDateTime.now());
            repo.save(form);
        }
        return form;
    }

    public ApexForm recommendForm(Long id, String recommenderName) {
        ApexForm form = repo.findById(id).orElse(null);
        if (form != null && form.getStatus().equals("FORWARDED")) {
            form.setStatus("RECOMMENDED");
            form.setRecommendedBy(recommenderName);
            form.setRecommendedAt(LocalDateTime.now());
            repo.save(form);
        }
        return form;
    }

    public ApexForm approveForm(Long id, String approverName, String remarks, boolean approved) {
        ApexForm form = repo.findById(id).orElse(null);
        if (form != null && form.getStatus().equals("RECOMMENDED")) {
            form.setStatus(approved ? "APPROVED" : "REJECTED");
            form.setApprovedBy(approverName);
            form.setApprovedAt(LocalDateTime.now());
            form.setRemarks(remarks);
            repo.save(form);
        }
        return form;
    }

}
