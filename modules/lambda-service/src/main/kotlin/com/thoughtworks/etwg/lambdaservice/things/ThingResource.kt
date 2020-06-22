/**
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
package com.thoughtworks.etwg.lambdaservice.things

import com.thoughtworks.etwg.lambdaservice.model.Thing
import com.thoughtworks.etwg.lambdaservice.model.ThingResult
import com.thoughtworks.etwg.lambdaservice.things.data.ThingEntity
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.slf4j.LoggerFactory
import net.logstash.logback.marker.Markers.append
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject


@RestController
@RequestMapping("/api/things")
open class ThingResource @Inject constructor(private val service: ThingsService) {

    val LOGGER = LoggerFactory.getLogger(ThingResource::class.java)

    @GetMapping("/")
    @SuppressFBWarnings("BC",
            justification = "This is a problem with spotbugs and kotlin collection types")
    fun findAll(): ThingResult {
        return ThingResult().results(service.findAll()
                .map { entity -> Thing().id(entity.id).name(entity.name) }
                .toCollection(ArrayList())
        )
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun newThing(@RequestBody value: Thing): Thing {
        val saved = service.upsert(
                ThingEntity().name(value.name)
        )
        LOGGER.info(append("thing", saved), "Created element");
        return Thing().id(saved.id).name(saved.name)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun saveThing(@RequestBody value: Thing, @PathVariable("id") id: String): Thing {
        val saved = service.upsert(
                ThingEntity().id(id).name(value.name)
        )
        LOGGER.info(append("thing", saved), "Update element");
        return Thing().id(saved.id).name(saved.name)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun findThing(@PathVariable("id") id: String): Thing {
        val saved = service.findById(id)
        LOGGER.info(append("thing", saved), "Returned element");
        return Thing().id(saved.id).name(saved.name)
    }
}