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

## Memory Models

[Primitives skipped since well understood.]

```
int var1 = 52;
SimpleLocation ucsd;
ucsd = new SimpleLocation(32.9, -117.2);
SimpleLocation lima = new SimpleLocation(-12.0, -77.0);
```
When a new object in instantiated, space is allocated in memory to store that object. This is done in the 'heap' at an address. While primitive values are stored 'in the box', the primitive box is assigned a reference (i.e. the address) to the object.

In essence the code 'int var1' and 'SimpleLocation ucsd' and 'SimpleLocation lima' each create primitives with those respective names. Since var1 is an 'int' and ints are themselves primitive, the '=' operator assigns var1 to contain 52. However, since ucsd is an object (and thus not primitive), '=' assigned a reference number which points to a location in the heap. At that location, SimpleLocation(32.9, -117.2) is then able to assign the values '32.9' and '-117.2' as instructed by the constructor (note that the constructor has lat. and long as primitives, which gives a place for these values to be stored).

Now imagine the following:
```
lima.latitude = -12.04;
```
If the '=' operator opened lima (which we know contains a reference to the heap) and replaced that reference with '12.04', then the lima object in the heap would be lost. We'd have no way of finding it. That wouldn't be any good. So, Java is instead designed to follow the reference until it gets to the primitive resting place (for lack of a better word). As such, '=' opens lima, reads the reference, follows the reference and gets to lima.latitude in the heap where it finds '-12.0' (which isn't a reference) and it overwrites that to be '-12.04'.

It may be helpful to think of the '=' operator as 'copy-and-paste' which can pick up one primitive and set it down somewhere else, and that somewhere else has to be an appropriate (primitive) box.

## Scope
*Member variables* are defined outside of a method which gives them a scope of the entire class. This makes them the broadest, because they can be accessed by any of the methods in the class. *Local variables* are variables that are declared inside a method and outside of any blocks therein (e.g. if statements). These are available anywhere within the method, but not in other methods of the same class. *Parameters / arguments* are essentially local variables which are created when passed into the method.

A constructor is a method. The scope of a method's local variables exists entirely within the method's existance / operation. Since the constructor is run once to create each object, the parameters passed to it are created as local variables and exit until the method is complete. These are - naturally - different from the member variables of the object which the constructor creates (remember how we're using setters an getters).

To access the member variables explicitly the constructor may call 'this.latitude' and in this way copy a value from the parameter to the object which it is creating. On Java, 'this' is not necessary. However it will first look within the method. So if you're using the same variable names in multiple places then you may have trouble or get confused if this stuff isn't kept track of.
