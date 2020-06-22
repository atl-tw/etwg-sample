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

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ThingResult implements Serializable {
  public static final int serialVersionUID = 1;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  private List<Thing> results;

  public List<Thing> getResults() {
    return results;
  }

  public void setResults(List<Thing> results) {
    this.results = results;
  }

  public ThingResult results(List<Thing> val) {
    this.results = val;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ThingResult)) return false;
    ThingResult that = (ThingResult) o;
    return Objects.equals(getResults(), that.getResults());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getResults());
  }
}
