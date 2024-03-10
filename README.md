Spring Boot Application to get the current weather status and also the forcast for the current date.

API :
1. Get the current weather along with the forcase for the current date
GET : /api/weather/{zipcode}
Example Output :
{
  "currentTemp": "89.0", // Current Temperature
  "weatherType": "Sunny", // Current weather type
  "maxTemp": "93.0", // Max expected temperature for the day
  "minTemp": "67.0", // Min expected temperature for the day
  "unitType": "F", // Unit type -> F means Fehreiheit
  "fetchedFromCache": false, // Tells whether the data has been fetched from cache or not. Default time for caching is set to 30 mins in application.properties under the resources folder
  "cachedTime": 1710076361376 // Time in millis that tells when the response was cached
}

Steps to run:
1. Clone the project
2. Install all the maven dependencies
3. Run the Spring Boot App from WeatherForcastApplication.java
4. Call the API - http://localhost:8080/api/weather/{zipcode} - For eg : http://localhost:8080/api/weather/560037 - For Bengaluru
5. Observe the output
   {
    "currentTemp": "89.0",
    "weatherType": "Sunny",
    "maxTemp": "93.0",
    "minTemp": "67.0",
    "unitType": "F",
    "fetchedFromCache": false,
    "cachedTime": 1710076792065
  }

Notes:
This application uses the free version of the APIs provided by AccWeather - https://developer.accuweather.com/apis
Also, in application.properties file we have given an apikey which is generated from a free account, and has a hard limit of making 20 API calls to the AccWeather system
As of now, a new key has been generated, and set in the properties file, but it has the potential to fail if used more than 20 times
