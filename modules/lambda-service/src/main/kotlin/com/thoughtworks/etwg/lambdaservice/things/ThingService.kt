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

import com.thoughtworks.etwg.lambdaservice.things.data.ThingEntity
import com.thoughtworks.etwg.lambdaservice.things.data.ThingRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject

val LOGGER: Logger = LoggerFactory.getLogger(ThingsService::class.java)

@Service
class ThingsService @Inject constructor(val repo: ThingRepository) {

    fun upsert(value: ThingEntity): ThingEntity {
        try {
            return this.repo.save(value)
        } catch (e: Exception) {
            LOGGER.info("Failed to save thing.id=${value.id}", e)
            throw ThingServiceException("Failed to save thing", e)
        }
    }

    fun findAll(): Iterable<ThingEntity> {
        return this.repo.findAll()
    }

    @Throws(ThingServiceException::class)
    fun delete(id: String): ThingEntity {
        try {
            val thing = this.repo.findById(id)
            if (thing.isPresent) {
                this.repo.delete(thing.get())
                return thing.get()
            } else {
                throw ThingNotFoundException("No thing with id $id")
            }
        } catch (e: java.lang.Exception) {
            throw ThingServiceException("Could not delete id=$id", e)
        }
    }
}

open class ThingServiceException(message: String, cause: Throwable?) :
        Exception(message, cause)

class ThingNotFoundException(message: String) : ThingServiceException(message, null)