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
import java.lang.RuntimeException
import javax.inject.Inject



@Service
open class ThingsService @Inject constructor(val repo: ThingRepository) {

    val LOGGER: Logger = LoggerFactory.getLogger(ThingsService::class.java)

    @Throws(ThingServiceException::class)
    open fun upsert(value: ThingEntity): ThingEntity {
        try {
            return this.repo.save(value)
        } catch (e: RuntimeException) {
            LOGGER.info("Failed to save thing.id=${value.id}", e)
            throw ThingServiceException("Failed to save thing", e)
        }
    }

    @Throws(ThingServiceException::class)
    open fun findAll(): Iterable<ThingEntity> {
        try {
            return this.repo.findAll()
        } catch (e: RuntimeException) {
            throw ThingServiceException("Unexpected error", e)
        }
    }

    @Throws(ThingServiceException::class)
    open fun delete(id: String): ThingEntity {
        val thing = this.findById(id)
        try {
            this.repo.delete(thing)
        } catch (e: RuntimeException) {
            throw ThingServiceException("Could not delete id=$id", e)
        }
        return thing
    }

    @Throws(ThingServiceException::class)
    open fun findById(id: String): ThingEntity {
        return repo.findById(id).orElseThrow { ThingNotFoundException("No thing with id $id") }
    }
}

open class ThingServiceException(message: String, cause: Throwable?) :
        Exception(message, cause)

open class ThingNotFoundException(message: String) : ThingServiceException(message, null)