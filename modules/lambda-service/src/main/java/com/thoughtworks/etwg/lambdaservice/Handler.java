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
package com.thoughtworks.etwg.lambdaservice;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.thoughtworks.etwg.lambdaservice.util.NoCoverageGenerated;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@NoCoverageGenerated(justification = "AWS Boilerplate")
public class Handler implements RequestStreamHandler {

  private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> INSTANCE;

  static {
    try {
      INSTANCE = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
    } catch (ContainerInitializationException e) {
      throw new InvocationError("Could not initialize Spring Boot application", e);
    }
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    INSTANCE.proxyStream(inputStream, outputStream, context);
  }

  public static class InvocationError extends RuntimeException {
    public static final int serialVersionUID = 1;

    public InvocationError(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
