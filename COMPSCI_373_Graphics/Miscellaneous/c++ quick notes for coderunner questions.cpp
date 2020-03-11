//------------------------------------------
// C++ Quick Notes for Geometry questions
// This is easier to read in notepad++ or an IDE than in the Canvas file viewer
// This is not a thorough guide to C++, but may be useful for helping identify errors
//------------------------------------------

//Two slashes marks the rest of the line as a comment

//basic types
int a = 15;			//integer, whole numbers, positive or negative
float b = 15.0f;	//float, floating point numbers, positive or negative, declare with decimal point and "f" after the number
double c = 15.0;	//double, double precision floating point number, declare with decimal point

//function definition

double square(double val)	//types are explicit - you have to say that a variable is a double or an int or a Vector3, etc
{
	double retVal = val * val;	//end statements with a semicolon
	return retVal;				//exit function with return statement
}

//function usage

double fiveSquared = square(5.0);

//Functions must be declared above where they are used:
float funcA() { return 0.0f; }
float aRes = funcA();	//This works

float bRes = funcB();
float funcB() { return 0.0f; }	//This does not work

//flow control

//looping 10 times (for loop)
for (int i = 0; i < 10; i++)
{
	doSomething();
}
//looping 10 times (while loop)
int i = 0;
while (i < 10)
{
	doSomething();
	i++;
}
//if statement
if (a < b)
{
	c = 5.0;
}
else
{
	c = 10.0;
}
//inline if statement
c = a < b ? 5.0 : 10.0;


//integer division

double x = 3 / 2;		//x is going to be equal to 1.0 (dividing int by int)
double x = 3.0 / 2.0;	//x is going to be equal to 1.5	(dividing double by double)

//classes

class Vector3	//The vector3 class is used in several Coderunner questions, in these questions it is already defined for you.
{
	public:
	double x;
	double y;
	double z;
	Vector3()	//default constructor
	{
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	Vector3(double xVal, double yVal, double zVal)
	{
		x = xVal;
		y = yVal;
		z = zVal;
	}
};

Vector3 v = Vector3(x, y, z);		//calling constructor

double xComponent = v.x;	//access class property

//Coderunner gives errors on unused variables:
double unusedVariableFunction()
{
	double a = 1.0;
	double b = 1.0;
	return b;			//Gives an error because variable a has been declared but not used
}

//----------------------------
//Simple Example Question
//----------------------------

// Write a function magnitude(), that takes as a parameters a 3D vector, and returns a double representing its magnitude. Your function should have the following signature:
// Vector3 magnitude(Vector3 v)
// You can assume that Vector3 is a class that represents a 3D vector, and exposes fields named x, y, and z.
// You can assume the following function to perform a square root is available to you: sqrt()

double magnitude(Vector3 v)
{
	double xSquared = v.x * v.x;
	double ySquared = v.y * v.y;
	double zSquared = v.z * v.z;
	double mag = sqrt(xSquared + ySquared + zSquared);
	return mag;
}

//A more compact answer
double magnitude(Vector3 v)
{
    return sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
}