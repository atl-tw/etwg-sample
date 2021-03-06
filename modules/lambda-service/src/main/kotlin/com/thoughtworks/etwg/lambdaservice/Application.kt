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
package com.thoughtworks.etwg.lambdaservice
import com.thoughtworks.etwg.lambdaservice.things.ThingResource
import com.thoughtworks.etwg.lambdaservice.things.ThingsService
import com.thoughtworks.etwg.lambdaservice.things.data.ThingRepository
import com.thoughtworks.etwg.lambdaservice.util.NoCoverageGenerated
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
@NoCoverageGenerated(justification = "Spring Boilerplate")
// Because this is a lambda, we want to use @Import rather than component scan
@Import(WMVC::class, ThingsService::class, ThingResource::class)
open class Application {
    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("^/api/.*"))
                .build()
    }
}

@EnableWebMvc
@NoCoverageGenerated(justification = "Spring Boilerplate")
class WMVC : WebMvcConfigurerAdapter() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}

@NoCoverageGenerated(justification = "Spring Boilerplate")
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}