package com.nasp.skillhunterapi.testutils.builder;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;
import com.nasp.skillhunterapi.model.ContactMethod;

public class ContactMethodBuilder {
  private Long id = 1L;
  private ContactMethodType type = ContactMethodType.EMAIL;
  private ContactMethodContext context = ContactMethodContext.WORK;
  private String value = "clark.kent@dailyplanet.com";
  private Boolean isPreferred = true;
  
  public static ContactMethodBuilder aContactMethod() {
    return new ContactMethodBuilder();
  }

  public ContactMethodBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ContactMethodBuilder withoutId() {
    this.id = null;
    return this;
  }

  public ContactMethodBuilder withType(ContactMethodType type) {
    this.type = type;
    return this;
  }

  public ContactMethodBuilder withContext(ContactMethodContext context) {
    this.context = context;
    return this;
  }

  public ContactMethodBuilder withValue(String value) {
    this.value = value;
    return this;
  }

  public ContactMethodBuilder withIsPreferred(Boolean isPreferred) {
    this.isPreferred = isPreferred;
    return this;
  }

  public ContactMethod build() {
    var contactMethod = new ContactMethod(type, context, value, isPreferred);
    contactMethod.setId(id);
    
    return contactMethod;
  }
}
