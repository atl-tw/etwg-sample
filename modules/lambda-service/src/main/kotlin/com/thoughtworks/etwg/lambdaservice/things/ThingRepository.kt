package com.thoughtworks.etwg.lambdaservice.things

import com.thoughtworks.etwg.lambdaservice.data.Thing
import org.springframework.data.repository.CrudRepository

interface ThingRepository: CrudRepository<Thing, String> {

}