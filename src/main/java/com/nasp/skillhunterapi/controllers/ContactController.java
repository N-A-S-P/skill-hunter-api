package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.Contact.*;
import com.nasp.skillhunterapi.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Contact")
@RestController
@RequestMapping("/api/contacts")
class ContactController {
    private final ContactService contactService;

    @GetMapping
    public List<ContactListItemResponse> GetContacts() {
        return contactService.getContacts();
    }

    @GetMapping("/{id}")
    public ContactDetailResponse GetContact(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDetailResponse CreateContact(@Valid @RequestBody ContactCreateRequest request) {
        return contactService.createContact(request);
    }

    @PutMapping("/{id}")
    public ContactDetailResponse UpdateContact(@PathVariable Long id, @Valid @RequestBody ContactUpdateRequest request) {
        return contactService.updateContact(id, request);
    }

    @PutMapping("/{id}/contact-methods")
    public ContactDetailResponse UpdateContact(@PathVariable Long id, @Valid @RequestBody ContactContactMethodRequest request) {
        return contactService.updateContact(id, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContact(Long id) {
        contactService.removeContact(id);
    }
}
