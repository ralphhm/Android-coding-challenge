# City Weather
Sample Android Application written in Kotlin to show the weather of a city using the [Open Weather Map API](https://openweathermap.org/api)
The MVVM pattern is used to encapsulate App specific domain logic into ViewModels that can be unit tested in isolation.
The use of [Android architecture ViewModel](The use https://developer.android.com/topic/libraries/architecture/viewmodel) allows ui state preserving on orientation change.
The internals of the ViewModel are based on reactive explicit ui states that are inspired by the talk [Managing State with RxJava by Jake Wharton](https://www.youtube.com/watch?v=0IKHxjkgop4).
The ViewModel is backed by a repository which hides the underlying API service and allows later extension by a database backed cache.

## Build
The App uses the Weather API which requires a registered API key for most of the API calls. To build the app the API key needs to be specified as a gradle property with the key "OpenWeatherMapApiKey" that is accessible by the gradle build system. One possibility is to add it to the gradle.properties file in the user's .gradle folder  [USER_HOME]/.gradle/gradle.properties in the following way
````
OpenWeatherMapApiKey="{API_KEY}"
```` 
The App can be build by executing the Gradle Task assembleRelease, the debug key is used for App signing

## Dependencies
* [Retrofit 2](http://square.github.io/retrofit) HTTP client to query the API and map requests in a declarative way
* [Moshi](https://github.com/square/moshi) JSON library to parse custom Date attributes
* [RxJava 2](https://github.com/ReactiveX/RxJava) reactive extensions for the JVM to handle asynchronous events in a reactive/stream based way  
* [Koin](https://insert-koin.io/) lightweight Kotlin dependency injection framework that allows simple ViewHolder injection and without code generation

##Improvements (potential next steps)
* Allow API key to be specified as environment variable
* Create intermediate domain model of Weather to be passed to to the view that handles pre formatting of values like number formats, units and metrix systems
* Introduce a layer in between Repository and Service that handles device locale based metric switching