# Notes from the Coursera course: Object Oriented Programming in Java

## Lesson 1

### Public v. Private
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

### Memory Models

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

### Scope
*Member variables* are defined outside of a method which gives them a scope of the entire class. This makes them the broadest, because they can be accessed by any of the methods in the class. *Local variables* are variables that are declared inside a method and outside of any blocks therein (e.g. if statements). These are available anywhere within the method, but not in other methods of the same class. *Parameters / arguments* are essentially local variables which are created when passed into the method.

A constructor is a method. The scope of a method's local variables exists entirely within the method's existance / operation. Since the constructor is run once to create each object, the parameters passed to it are created as local variables and exit until the method is complete. These are - naturally - different from the member variables of the object which the constructor creates (remember how we're using setters an getters).

To access the member variables explicitly the constructor may call 'this.latitude' and in this way copy a value from the parameter to the object which it is creating. On Java, 'this' is not necessary. However it will first look within the method. So if you're using the same variable names in multiple places then you may have trouble or get confused if this stuff isn't kept track of.

## Lesson 2

Reflections on assignment:

> This assignment was to use the unfoldingmaps and processing libraries to add earthquake markers to a map with different styles and the a map key. In hind sight, I should have immediately realized that unfoldingmaps was going to make it easy to put custom pointers on the map; That's what it's designed for. However, the two libraries have vastly different documentations. Unfoldingmaps seems to be automatically generated docs, whereas processing has been written by the human hand. 

> I preferred processing's docs, because they seemed to know what the reader was looking for, while Unfoldingmaps didn't it just threw out all the information with no emphasis on what most users probably use most of the time. This made it harder to find the right methods, but it's a useful experience.

> In both libraries, the developers have intentions of how the methods might be used and there's an internal logic between how the various methods and classes work together. Some of this information can be gleaned from the names. Other information is available my considering which classes have what. So - unlike my first pass at this assignment - before thinking about what needs to be done and trying to figure out how to do it with what I see, I've learned to focus on using this information to first determine exactly what the library is designed to enable others to do. If it's designed to do something, then there's probably an easy way to do it if you break down how the components work together.

## Lesson 3

- Add Markers for Cities.
- Determine if the earthquake occured on land and do something different.
- Same for depth.
- Same for recency.

### Inheritance

Objects have similarities and differences. To the extent that they are similar, inheritance makes it possible to avoid having multiple copies of code that does the same thing. Having the same code twice is bad because then you have to keep track of the two and so forth. While inheritance deals with what is the same, the children add the differences.

Inheritance is done by:
```
public class Person {
	private String name;
	...
}
public class Student extends Person {
	...
}
```
In this case Person is what's known as a 'base class' or a 'superclass'. Student is known as 'derived class' or 'subclass'.

Student inherits 1) the public instance variables (i.e. member variables), the public methods, and the private instance variables. So don't include 'private Strong name;' in the Student class because it's already there. That would be a 'hidden variable' and is bad if only because it'd be confusing as the day is long. 

#### UML Diagram
UML stands for Unified Modeling Language.
```
__________________
     Person
__________________
String name
------------------
String getName()
```

Use arrows to indicate inheritance between the classes. So, when drawing the subclasses, it is not necessary to specify 'String name' or 'getName()', because those have been inherited and are covered by inclusion of the arrow.

#### Reference v. Object Type
Naturally, you can't pass a string to an int. The same can occur - or at least something very similar - with objects.

Example:
```
Person p = new Person();
Student s = new Student();
```
That's fine. Makes perfect sense.
```
Person p = new Student();
```
This is also allowed. This exemplifies the 'is a' relationship. A Person-array may contain Students because each Serson 'is a' Person.
```
Student s = new Person();
```
*NO!* This may not be immediately intuitive. Of course that's how it works in the real world, but programming languges aren't always intuitive. However, that's where you'd be wrong. Object oriented programming languages (or at least Java) is designed to mirror the real world. It works the way you would think it should work. It may be that you can't pass a String to an Int, but you can pass an Into to a Double, because Strings are broader and contain extra information, but Ints are narrower and can fit into a Double... but referencing primitives and referencing objects are different. Just believe that it works in the code the what you would want it to work. They both makes sense.

#### Visibility Modifiers
- Private: Accessible from:
- - *same class*
- Package: Accessible from:
- - same class
- - *same package*
- Protected: Accessible from:
- - same class
- - same package
- - *any subclass*
- Public: Accessiblility is *wide open.*

Protected is weird. It makes sense that a subclass would need access to it's parent's variables, etc., but why should another class in the same package have access? Package access is wierder, because that still gives access to other classes in the package, but lacks the utility of giving access to the subclasses. In general, stick to *private* and *public*.

#### Object Construction in Java
All objects inherit from the object class and are constructed 'from the inside out.'
```
Student s = new Student();
```
Java goes Student() -> Person() -> Object(), then initializeses the Object variables. Once that's done, it steps back out and adds on the different variables that Person() adds, and then onto the different variables that Student() adds. So, it sensibly starts with what's inherited in the order that it's inherited.

##### Compiler Rules
On one of the passes, the compiler adds/changes the code.

1. It adds a superclass 'extends Object'.
2. If it doesn't have a constructor, then a default constructor is added.
3.  The first line of a class must be either 'this(...)' or 'super(...)', otherwise Java will insert 'super();' so that it can breadcrumb it's way up to the Object() class and work its way back out.

##### Variable Initialization
Consider the following:
```
public class Person extends Object {
    private String name;
    public Person(String n) {
        super();
        this.name = n;
    }
}
public class Student extends Person {
    public Student (String n) {
        // Note that you can't do this:
        // super();
        // this.name = n;
        // Because name is Private. Instead:
        super(n);
    }
    public Student () {
        // When you haven't been passed an argument
        // such as 'name' you could do the following:
        // super ( "Default Name" );
        // However, you might as well make use of
        // the Student(n) constructor that is in
        // this class, that way you can keep everything
        // tight and put code in there that will run
        // in both instances.
        this( "Default Name" );
    }
}
```
##### Method Overriding
Overriding is not the same as overloading.
- Overloading is when a method is in the *same class* with the same name, but different parameters.
- Overriding is when a method is in a *sub*class and has the same name and the *same* parameters. It *over-rides* the method in the parent class - as the name would suggest.

What does overriding allow us to do? Well consider there is a method in a class. It does something. By putting a method with the same name and same parameters in a subclass, we can call the same each the same way. However, we can have the subclass behave differently (because we over-rode that method).

For example, recall:
```
__________________
     Person
__________________
String name
------------------
String getName()
String toString()
```
and consider
```
Person p = new Person("Tim");
// note System.out.println() automatically calls
// .toString when passed an object. So:
// System.out.printn(p.toString());
// is the same as:
System.out.println( p );
```
We have a Person object which has a toString() method and will output the name and soforth of that object. However, Person has a subclass of Student, which has other additional variables and Student also needs a .toString().

To give a subclass a .toString() (and presumably the same can be done in other contexts):

```
public class Student extends Person {
    public int getSID() {
        return studentID;
    }
    public String toString() {
        return  this.getSID() + ": " +
                super.toString();
    }
    // Look we used 'this' and 'super'!
}
```

### Polymorphism
We've already done it. Person got poly-morphy being passed a sublcass which is different(morphed) in one way (poly). See:
```
Person s = new Student('Cara', 1234);
```
So this allows us to:
```
Person p[] new person[3];
p[0] = new Person('Tim');
p[1] = new Person('Cara', 1234);
p[2] = new Person('Mia', 'abcd');
```
#### Core Rules
There are 'compile-time' rules and 'runtime-rules'.
##### Compile Time Rules
The compiler only knows the reference type. For example:
```
Person s = new Student('Cara', 1234);
System.out.println(s);
// Remember this will call s.toString();
```
When the compiler sees this, it only knows that 's' is a Person and that it is looking to run a toString(). Since Person has a toString(), it compiles - but *which* toString() isn't determined until runtime where the override is encountered.
##### Runtime Rules
At runtime, Java sees that it's a Student and that instead of Person.toString() it will do Student.toString().
##### Example
```
__________________
     Person
__________________
String name
------------------
String getName()
String toString()
```
```
__________________
     Student
__________________
int studentID
------------------
int getSID()
String toString()
```
```
Person s = new Student('Cara', 1234);
System.out.println(s);
// Remember this will call s.toString();
```
This code runs perfectly as discussed above, but what about:
```
Person s = new Student('Cara', 1234);
System.out.println( s.getSID() );
```
This won't work, because at comple-time, the compiler only knows that it is calling 'getSID()' on the Person class. The compiler does not see a getSID(), because that's not in all Persons only the subclass Student. So we get a compile-time error.
#### Casting
Casting fixes the above problem.
```
Person s = new Student('Cara', 1234);
System.out.println( ( (Student)s ).getSID() );
```
This 'casts' the variable 's' as a Student, instructing the compiler just go with it. It won't find a .getSID in the Person class, and it can't see into Student to check if the subclass has such a method. So it's just going to do it live and hope the runtime can find what you're claiming is there.

So don't be wrong. Imagine that instead of 'new Student...' you do 'new Person' or *in an array of mixed Students and Persons, the following occurs:
```
Person s = new Person('Tim');
System.out.println( ( (Student)s ).getSID() );
```
Runtime error! To prevent this error do the following:
```
if( s instanceof Student ) {
    // only executes of 's' is-a Student at runtime
    ...
}
```
### Abstract Classes: Implementation v. Interface
Suppose we're now working in the accounting department, and we send statements to students and faculty. They may happen to be people, but what makes them a person isn't relevant to our billing/payments, etc.

An abstract class allows us to 
- Force subclasses to have 'monthyStatement()' method.
- Stop creating actual objects of the Person type, because we're just printing statements, not adding new students or faculty to the university.
- Keep being able to use Person references.
- Retain common Person code.

Abstract classes offer inheritance of both **implementation** (instance variables and methods) as well as **interface**.
```
public abstract class Person { ... }
```
This code creates a class which will not create objects of Person type. This is appropriate, because the accounting department doesn't need instances of people. However, this abstract class can be *inherited from*.

In addition to inheriting from abstract classes, it is possible to force subclasses to override the methods in the abstract class. This may be done if you know each subclass will have to have/do something, but there is no common code between the different subclasses. This is forced by methods which say:
```
public abstract void monthlyStatement() {...}
```
***note:** if a class contains an abstract method, then the class must be abstract.*

An interface does all the same, but does not enable the subclasses to inherit the common base-class code. Also, and interface only defines the required methods. If there is not base code that both will need, then an interface is probably approprite. One may be made by:
```
// Defined in java.jang.Comparable
package java.lang;

public interface Comparable<E> {
    // Compare this object's name to o's name
    // Return < 0, 0, > 0 if object so compares.
    public abstract int compareTO( E o );
}
```
```
public class Person implements Comparable<Person> {
    private String name;
    ...
    
    @Override
    public int compareTo (Person o ) {
        return this.getName().compareTo( o.getName() );
    }
}
```
