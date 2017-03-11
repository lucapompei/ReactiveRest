# Reactive Rest
=============================

The Reactive Rest client is a simple and fast HTTP client, based on Retrofit 2 implementation, which help you to make REST communications in a rapidly and easy way.

Using this library, you can make 4 different types of HTTP REST communications:

1) <strong>Synchronous HTTP request:</strong> each HTTP response is synchronously obtained;
    
2) <strong>Asynchronous HTTP request:</strong> each HTTP response is asynchronously obtained, using the Retrofit queue implementation;
    
3) <strong>Event based HTTP request:</strong> each HTTP response is asynchronously processed and then dispatched on a common Event Bus to all registered entity (i.e. it's used the Guava Event Bus implementation for this purpose); 
    
4) <strong>Reactive HTTP request:</strong> based on Reactive programming paradigm, each HTTP response is asynchronously prepared, taking the advantage of using Observable, so that each Observer interested and subscribed to it, can obtain the response when it is processed.

The Reactive REST client supports GET, POST, PUT and DELETE HTTP method.

API Usage
--------


1) Define a new HTTP request:

To define a basic HTTP request you must specify the base url to call and the api endpoint. As default, it's used the GET HTTP method.
    
```java
// Define mandatory parameters
String baseUrl = "https://www.example.com/api/v2/";
String apiEndpoint = "getServices";
    
// Basic HTTP request
HttpRequest httpRequest = new HttpRequest.
    Builder(baseUrl, apiEndpoint)
    .build();
```
    
To enrich your HTTP request, you can add one or more optional parameters (i.e. http method, headers, query string and body params).

```java
// Define optional parameters
HttpMethod httpMethod = HttpMethod.GET;
Map<String, String> headers = ImmutableMap.of("AUTH_TOKEN", "1234567");
Map<String, String> queryParams = ImmutableMap.of("sort", "desc");
Map<String, String> bodyParams = ImmutableMap.of("code", "first");
    
// Full HTTP request
HttpRequest httpRequest = new HttpRequest.
    Builder(baseUrl, apiEndpoint)
    .httpMethod(httpMethod)
    .headers(headers)
    .queryParams(queryParams)
    .bodyParams(bodyParams)
    .build();
```
 
2) Make REST communication:

- SyncAPI:

    ```java
    HttpResponse httpResponse = SyncAPI.call(httpRequest);
    ```

- AsyncAPI:

    ```java
    // Define a Java Consumer to handle success reponse 
    // and, optionally, another Consumer to handle error response
    Consumer<HttpResponse> consumerOnSuccess;
    Consumer<Throwable> consumerOnError;
        
    // Call API
    AsyncAPI.call(httpRequest, consumerOnSuccess);
        
    // Call API handling error
    AsyncAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
    ```

- EventAPI:

    ```java
    // Define an event identifier
    static String EVENT_IDENTIFIER = "MyEvent";
        
    // Register entity to Event Bus through a coordinator
    CoordinatorAPI.getCoordinator().register(this);
        
    // Call API
    EventAPI.call(httpRequest, EVENT_IDENTIFIER);
            
    // Subscribe a method to handle HTTP response
    @Subscribe
    public void onConsumeMyEventEvent(EventResponse eventResponse) {
        if (CoordinatorAPI.getCoordinator().isEventForMe(eventResponse, EVENT_IDENTIFIER)) {
            if (eventResponse.isSuccess()) {
                // use http response
                System.out.println(eventResponse.getEventResponse().toString());
            } else {
                // use http error
                System.out.println(eventResponse.getEventError());
            }
        }
    }
    ```

- ReactiveAPI:

    ```java
    // Define a Reactive Consumer to handle success reponse 
    // and, optionally, another Consumer to handle error response
    Consumer<HttpResponse> consumerOnSuccess;
    Consumer<Throwable> consumerOnError;
        
    // Call API
    ReactiveAPI.call(httpRequest, consumerOnSuccess);
        
    // Call API handling error
    ReactiveAPI.call(httpRequest, consumerOnSuccess, consumerOnError);
    ```

For a better comprehension of ReactiveRest, some test classes are provided.

Download
--------

Downloadable .jars can be found on the [Bintray download page][binary].

You can also depend on the .jar through Maven:

```xml
<dependency>
  <groupId>lp.reactive</groupId>
  <artifactId>ReactiveRest</artifactId>
  <version>1.1</version>
</dependency>
```

or Gradle:

```groovy
compile "lp.reactive:ReactiveRest:1.1"
```


License
-------

  Copyright (C) 2017 lucapompei
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

 [binary]: https://dl.bintray.com/lucapompei/maven/lp/reactive/ReactiveRest/
