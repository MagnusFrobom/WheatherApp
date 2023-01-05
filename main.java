import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // Get a reference to the location manager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            // Permission has already been granted, fetch the weather
            fetchWeather()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, fetch the weather
                fetchWeather()
            } else {
                // Permission has been denied, display an error message
                Toast.makeText(this, "Weather app requires location permission to function.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather() {
        try {
            // Get the last known location
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (lastLocation != null) {
                // Get the latitude and longitude of the location
                val latitude = lastLocation.latitude
                val longitude = lastLocation.longitude

                // Use the latitude and longitude to fetch the weather
                val weather = getWeather(latitude, longitude)

                // Display the weather
                weatherTextView.text = weather
            } else {
                // Location not available, display an error message
                weatherTextView.text = "Unable to determine location."
            }
        } catch (e: SecurityException) {
            // Location permission has not been granted, display an error message
            weatherTextView.text = "Weather app requires location permission to function."
        } catch (e: IOException) {
            // An error occurred while fetching the weather, display an error message
            weatherTextView.text = "An error occurred while fetching the weather."
        }
    }

    private fun getWeather(latitude: Double, longitude: Double): String {
        // Send an HTTP GET request to the OpenWeatherMap API and retrieve the response
        val response = requests.
