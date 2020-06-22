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

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtworks.etwg.lambdaservice.model.Thing
import com.thoughtworks.etwg.lambdaservice.model.ThingResult
import com.thoughtworks.etwg.lambdaservice.things.data.ThingEntity
import org.junit.Test

class ThingResourceTest {

    private val service: ThingsService = mock()
    private val instance = ThingResource(service)

    @Test
    fun testGet() {
        val entity = ThingEntity().id("1234").name("foo")
        whenever(service.findById("1234")).thenReturn(entity)
        val result = instance.findThing("1234")
        assertThat(result).isEqualTo(Thing().id("1234").name("foo"))
    }

    @Test
    fun testFindAll() {
        val entity = ThingEntity().id("1234").name("foo")
        whenever(service.findAll()).thenReturn(listOf(entity).asIterable())
        val result = instance.findAll()
        assertThat(result).isEqualTo(ThingResult().results(listOf(Thing().id(entity.id).name(entity.name))))
    }

    @Test
    fun testSave() {
        val entity = ThingEntity().id("1234").name("foo")
        whenever(service.upsert(any())).thenReturn(entity)
        val result = instance.saveThing(Thing().id("1234").name("foo"), "1234")
        assertThat(result).isEqualTo(Thing().id("1234").name("foo"))
    }

    @Test
    fun testCreate() {
        val thing = Thing().name("foo")
        val entity = ThingEntity().id("1234").name("foo")
        whenever(service.upsert(any())).thenReturn(entity)
        val result = instance.newThing(thing)
        assertThat(result.name).isEqualTo("foo")
        assertThat(result.id).isNotNull()
    }
}