# Notes from the Coursera course: Object Oriented Programming in Java

## Public v. Private
```
public class SimpleLocation {
	public double latitude;
	public double longitude;
}
```
For the variables latitude and longitude, note that they are 'public' which will allow those variables to be accessed from an a SimpleLocation object e.g. 'lima.latitude'. The same is true for methods such as 'lima.measureDistance(nyc)'.

Conversely 'private' will prevent the above.

As a rule of thumb, always make member variables (an objects internal variables) private. To allow access 'getters' and 'setters' are written. These simply take and argument and change / or return the local (member) variable.

One reason for using getters and setters prevent users from setting invalid values.