## Weather Forcast Application
This Spring Boot application retrieves and displays weather forecast information for various zipcodes around the world.

## Features
1. Fetches weather data using a weather API
2. Displays current weather conditions (temperature, type, max temp, min temp, unit, caching status)
3. Provides extended forecast (For the current day)
4. User-friendly API

## Getting Started
```
1. Prerequisites:
   Java 17+ (https://www.oracle.com/java/technologies/downloads/)
   Maven (https://maven.apache.org/download.cgi)
   
2. Clone the repository:
    git clone https://github.com/VarunGoel252/weather-forcast-app.git
    cd weather-forcast-app
    git checkout master
    
3. Install dependencies
    mvn clean install
    
4. Run the application
    mvn spring-boot:run
    
This will start the application on port 8080 by default (you can customize this in application.properties)
```
## Usage
Get the current weather along with the forcast for the current date<br/>
GET : /api/weather/{zipcode}<br/>
Sample Output :
```
{
  "currentTemp": "89.0", // Current Temperature
  "weatherType": "Sunny", // Current weather type
  "maxTemp": "93.0", // Max expected temperature for the day
  "minTemp": "67.0", // Min expected temperature for the day
  "unitType": "F", // Unit type -> F means Fehreiheit
  "fetchedFromCache": false, // Tells whether the data has been fetched from cache or not. Default time for caching is set to 30 mins in application.properties under the resources folder
  "cachedTime": 1710076361376 // Time in millis that tells when the response was last cached
}
```

## Additonal Notes:
This application uses the free version of the APIs provided by AccWeather - https://developer.accuweather.com/apis <br/>
Also, in application.properties file we have given an apikey which is generated from a free account, and has a **hard limit of making 20 API calls** to the AccWeather system <br/>
As of now, a new key has been generated, and set in the properties file, but it has the potential to fail if used more than 20 times <br/>
