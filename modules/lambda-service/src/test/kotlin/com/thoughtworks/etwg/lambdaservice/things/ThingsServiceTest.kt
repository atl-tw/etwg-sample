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
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtworks.etwg.lambdaservice.things.data.ThingEntity
import com.thoughtworks.etwg.lambdaservice.things.data.ThingRepository
import org.junit.Test
import org.mockito.Matchers
import java.lang.RuntimeException
import java.util.Optional

class ThingsServiceTest {

    var repo: ThingRepository = mock()

    @Test
    fun testSave() {
        val entity = ThingEntity().name("Foo")
        whenever(repo.save(entity)).thenReturn(entity)
        val instance = ThingsService(repo)
        val result = instance.upsert(entity)
        verify(repo).save(Matchers.eq(entity))
        assertThat(result).isEqualTo(entity)
    }

    @Test(expected = ThingServiceException::class)
    fun testSaveException() {
        val entity = ThingEntity().name("Foo")
        whenever(repo.save(entity)).thenThrow(RuntimeException())
        val instance = ThingsService(repo)
        instance.upsert(entity)
    }

    @Test
    fun testDelete() {
        val entity = ThingEntity().name("Foo").id("1234")
        whenever(repo.findById("1234")).thenReturn(Optional.of(entity))
        val instance = ThingsService(repo)
        val result = instance.delete("1234")
        verify(repo).delete(Matchers.eq(entity))
        assertThat(result).isEqualTo(entity)
    }

    @Test(expected = ThingNotFoundException::class)
    fun testDeleteNotFound() {
        whenever(repo.findById("1234")).thenReturn(Optional.empty())
        val instance = ThingsService(repo)
        instance.delete("1234")
    }

    @Test(expected = ThingServiceException::class)
    fun testDeleteException() {
        val entity = ThingEntity().name("Foo").id("1234")
        whenever(repo.findById("1234")).thenReturn(Optional.of(entity))
        whenever(repo.delete(eq(entity))).thenThrow(RuntimeException())
        val instance = ThingsService(repo)
        instance.delete("1234")
    }

    @Test
    fun testFindAll() {
        val entity = ThingEntity().name("Foo").id("1234")
        whenever(repo.findAll()).thenReturn(listOf(entity).asIterable())
        val instance = ThingsService(repo)
        assertThat(instance.findAll()).containsExactly(entity)
    }

    @Test(expected = ThingServiceException::class)
    fun testFindAllException() {
        whenever(repo.findAll()).thenThrow(RuntimeException())
        val instance = ThingsService(repo)
        instance.findAll()
    }
}