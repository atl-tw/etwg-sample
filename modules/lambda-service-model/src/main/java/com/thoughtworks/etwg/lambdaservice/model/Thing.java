/*
 * Copyright 2020 Robert Cooper, ThoughtWorks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thoughtworks.etwg.lambdaservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Thing implements Serializable {

  public static final int serialVersionUID = 1;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  private String id;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  private String name;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Thing id(String val) {
    this.id = val;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String val) {
    this.name = val;
  }

  public Thing name(String name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Thing)) return false;
    Thing thing = (Thing) o;
    return Objects.equals(getId(), thing.getId()) && Objects.equals(getName(), thing.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }
}
