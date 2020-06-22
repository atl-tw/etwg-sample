package com.thoughtworks.etwg.lambdaservice.things

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
@RequestMapping("/api/things")
open class ThingResource @Inject constructor(private val service: ThingsService) {


    @GetMapping("/")
    fun findAll() = service.findAll()
}