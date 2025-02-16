/*
 * Copyright 2022 The Sigstore Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.sigstore.http;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import java.io.IOException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;

/** HttpClients generates Google Http Client objects from configuration. */
public class HttpClients {

  /**
   * Build a transport, you probably want to use {@link #newRequestFactory} to instantiate GET and
   * POST requests.
   */
  public static HttpTransport newHttpTransport(HttpParams httpParams) {
    HttpClientBuilder hcb =
        ApacheHttpTransport.newDefaultHttpClientBuilder().setUserAgent(httpParams.getUserAgent());
    if (httpParams.getAllowInsecureConnections()) {
      hcb.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
    }
    return new ApacheHttpTransport(hcb.build());
  }

  /** Create a new get requests with the httpParams applied and exponential backoff retries. */
  public static HttpRequestFactory newRequestFactory(HttpParams httpParams) throws IOException {
    return HttpClients.newHttpTransport(httpParams)
        .createRequestFactory(
            request -> {
              request.setConnectTimeout(httpParams.getTimeout() * 1000);
              request.setReadTimeout(httpParams.getTimeout() * 1000);
              request.setUnsuccessfulResponseHandler(
                  UnsuccessfulResponseHandler.newUnsuccessfulResponseHandler());
            });
  }
}
