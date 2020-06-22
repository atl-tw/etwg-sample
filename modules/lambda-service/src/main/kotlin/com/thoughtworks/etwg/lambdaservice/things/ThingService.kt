package com.thoughtworks.etwg.lambdaservice.things

import com.thoughtworks.etwg.lambdaservice.data.Thing
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject

val LOGGER: Logger = LoggerFactory.getLogger(ThingsService::class.java)

@Service
class ThingsService @Inject constructor(val repo: ThingRepository) {

    fun upsert(value: Thing): Thing {
        try {
            return this.repo.save(value)
        } catch (e: Exception) {
            LOGGER.info("Failed to save thing.id=${value.id}", e);
            throw ThingServiceException("Failed to save thing", e);
        }
    }

    fun findAll(): Iterable<Thing> {
        return this.repo.findAll();
    }

    @Throws(ThingServiceException::class)
    fun delete(id: String): Thing {
        try {
            val thing = this.repo.findById(id)
            if (thing.isPresent) {
                this.repo.delete(thing.get());
                return thing.get();
            } else {
                throw ThingNotFoundException("No thing with id $id");
            }
        } catch (e: java.lang.Exception) {
            throw ThingServiceException("Could not delete id=${id}", e);
        }
    }
}

open class ThingServiceException(message: String, cause: Throwable?) :
        Exception(message, cause)

class ThingNotFoundException(message: String) : ThingServiceException(message, null);