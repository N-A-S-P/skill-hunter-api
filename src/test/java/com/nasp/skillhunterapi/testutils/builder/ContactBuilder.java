package com.nasp.skillhunterapi.testutils.builder;

import java.util.ArrayList;
import java.util.List;

import com.nasp.skillhunterapi.model.Contact;
import com.nasp.skillhunterapi.model.ContactMethod;

public class ContactBuilder {
  private Long id = 1L;
  private String firstName = "Clark";
  private String lastName = "Kent";
  private String title = "Reporter";
  private List<ContactMethod> contactMethods = new ArrayList<>();

  public static ContactBuilder aContact() {
    return new ContactBuilder();
  }

  public ContactBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ContactBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public ContactBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public ContactBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public ContactBuilder withContactMethods(ContactMethod... contactMethods) {
    this.contactMethods = new ArrayList<>(List.of(contactMethods));
    return this;
  }

  public ContactBuilder addContactMethod(ContactMethod contactMethod) {
    if (this.contactMethods == null) {
      this.contactMethods = new ArrayList<>();
    }
    this.contactMethods.add(contactMethod);
    return this;
  }

  public Contact build() {
    var contact = new Contact(firstName, lastName, title, contactMethods);
    contact.setId(id);
    return contact;
  }
}
