## Project info
The project was done as a part of a university course called TKOM (Technics of Comilation) where the topic was to create your own programming language


#### Instructor's Specification:
- Strong
- Mutable
- Value
### Language Operation
#### Data Types with Code Examples of Data Types
##### Basic Numeric Data Types:
- Integer Type
  - `int a = 5`
- Floating-point Type
  - `flt a = 5.0`
- When incorrect values are provided for initializing variables, a #Error `TypeError` is thrown.
##### Character Type - String
- `str a = "ala ma kota"`
- "\\" escaped
  - special character sequences:
    - `\\` -> \
    - `\"` -> "
    - `\t` -> tab
    - `\n` -> new line
  - example:
```
>> str a = "\"ala\"\\\"jacek\" to\tosoby \n\t ważne"
>> print(a)
"ala"\"jacek" to    osoby
	ważne
```
##### Logical Type:
- `bool` accepts values: `true` `false`
  - `bool a = true`
##### Other Data Types ():
- Structures:
  - allow you to create your own structure with attributes of specified types
```
struct Box {
	str name,
	const str color,
	int height
}

Box b = Box("klocek", "zielony", 2)
b.height = 5
```
- Tagged Union:
  - an object stores only one of the possible types defined by the programmer
  - to access the value of a given field, you first need to "match" the type it stores and apply the appropriate operations
  - the programmer does not have to handle all types from a given TaggedUnion
```
TaggedUnion Grade {
	int numeric,
	str descriptional,
	flt curved
}

Grade gr = Grade.numeric(1)
match gr {
	Grade.numeric(value) {
		print("int value " + str(value))
	}
	Grade.descriptional(value) {
		print("str value " + value)
	}
	Grade.curved(value) {
		print("flt value " + str(value))
	}
}
```
##### Type Casting
- Due to strong typing, it is possible to change the type of a variable, but it must be done consciously. The casting operator looks as follows: <type-to-cast-to>(variable), example:
```
int a = 8
flt b = int(a)
```
- Casting:
  - `int` can be to:
    - `flt` - always possible
    - `str` - always possible
  - `flt` can be to:
    - `int` - always possible, the value is truncated to whole numbers, e.g., `int(5.6)` returns 5, as does `int(5.4)`
    - `str` - always possible
  - `str` can be to:
    - `int` - if the `str` value is invalid, a #Error `TypeError` is thrown
#### Operators
- Operators - an operator can only be used between two of the same types. Using different types is not allowed; when two different types are used, a #Error `TypeError` is thrown:
  - addition `+`;
    - Types: `int` `flt` `str`
  - subtraction `-`; multiplication `*`; division `/`; exponentiation `**`; increment `+=`; modulo `%`
    - Types: `int` `flt`
  - (pre/post)increment `++`;
    - Types: `int`
  - inequalities `<` `<=` `>` `>=` `==` `!=`
    - Types: `int`, `flt`
  - logical `or` `and`
    - Types: `bool`
  - assignment operator `=`
    - assigns a **value** to a given variable
```
int a = 9
int b = a
a++
print(a) # prints 10
print(b) # prints 9
```
- **exception** for operations on two different types:
  - the `*` operator can be used between `str` and `int` as follows:
```
>> str a = "a"
>> str b = a * 5
>> print(b)
aaaaa
```
- operator hierarchy (the higher the operator, the greater the priority):
  - `++` `**`
  - `*` `/` `%` `+=`
  - `+` `-`
#### Built-in Functions:
- print - prints text to the console
  - possible types: `str`
```
>>print(1)
1
```

#### Comments
- single-line - starting with `#` and being at the end of a given line or starting from a new line
```
# comment
int a = 1 # another comment
```
#### Variable Semantics
- Typing
  - strong static
- Mutability:
  - mutable by default
  - to make immutable, add the keyword `const`:
    - `const int a = 5`
- variable visibility scope:
  - variable shadowing
    - variables within a given block or within their visibility for the duration of their lifetime overshadow variables of the same name defined earlier with greater visibility.
    - for example, a variable created in a block will overshadow a global variable created earlier for the duration of the block's existence:
```
int a = 6

fun test() {
	str a = "kolcek"
	print(a)
}

print(a) # prints 6
test() # prints kolcek

```
- by default, a block (some functions are large and work for quite a while) can have function/global visibility
  - global visibility of a given variable can be achieved using the keyword `gscope` -> global scope, global variables are **immutable!**
  - example of block visibility:
```
fun test() {
	if (x == 0) {
		gscope int b = 7
		int a = 6
		print(a)
	}
	print(a) # a is not visible here
	print(b) # b is visible here
}
```
- conditional statement:
  - `if` `else`, code example:
```
if (x == 1) {
	print("1")
}
if (x == 1) { print ("1")}

if (x == 1) {
	print("1")
} else {
	print("else")
}

if (x == 1) {
	print("1")
} elif (x == 2) {
	print("elseif")
} else {
	print("else")
}

```
- loop statement
  - `while` - code example:
```
while (x == 1) {
	print(str(x))
	x++
}

while (true) {
	str a = input("a? ")
	if (a == "a") {
		break
	}
}
```
#### Functions
- defining custom ones:
  - start with the keyword `fun`
  - specify the return type
  - specify arguments with their types
  - by default, arguments are passed by value; to pass by reference, use the symbol `*`
  - If the function does not return any type, `void` should be specified in place of the type
  - If the type passed to the function does not match the declared type, a #Error `TypeError` is thrown
- calling - a function is called by providing the function name and arguments
- function overloading is **not** possible
- having a function and a variable with the same name is **not** allowed
- a main function can exist, acting similarly to cpp
```
fun kmToMiles(flt km) -> flt {
	return km * 0.6
}

fun changeKmToMiles(&flt km) -> void {
	km = km * 0.6
}

flt milesCopy = kmToMiles(10.1)

flt milesRef = 10.4
changeKmToMiles(&milesRef)

```
- recursive function calls
  - Possible but there is a limit: 500
  - the limit for a given function can be changed by adding a call argument: recursion-limit = x, example in [[#Technical Specification]]
```
fun silnia(int n) {
	if (n == 0) {
		return 1
	}
	return silnia(n-1)
}
```
#### Errors
- In case of an error, the system stops working and "panics" by printing the encountered error to the console and the location where it was encountered. Example erroneous code and error message:
```
1 int a = 8
2 a = 9.3 # TypeError
```

```
>> simple file.si
Traceback: file.si, line 2, character 4
	TypeError: incorrect value for int
```
##### List of Possible Errors
- TypeError (example above)
- NameError - variable g does not exist:
```
int a = 9
int b = 0
c = g + b
```
- SyntaxError - incorrect condition, missing {} after a conditional statement:
```
if name == 2:
	print("a")
```
- ZeroDivisionError

```
int a = 1 / 0
```
- RecursionError:
```
fun func() {
	func()
}
```
- OverflowError:
```
int a = 10**100000
```
#### Complex Code Examples

```
# Sample code

struct Point {
	flt x,
	flt y
}

struct Rectangle {
	Point a,
	Point b,
	const str color
}

struct Circle {
	Point center,
	flt radius
}

TaggedUnion Shape {
	Circle cir,
	Rectangle rec
}

fun circleArea(&Circle c) -> flt

 {
	return (3.14 * c.radius**2)
}

fun rectangleArea(&rectangle r) -> flt {
	return ((r.a.x - r.b.x) * (r.a.y - r.b.y))
}

fun shapeArea(&Shape s) -> flt {
	match s {
		Shape.cir(value) {
			return circleArea(value)
		}
		Shape.rec(value) {
			return rectangleArea(value)
		}
	}
}


fun runFun() -> void {
	Circle c = Circle(Point(1.2, 2.1), 5.4)
	Rectangle r = Rectangle(Point(1.1, 1.0), Point(2.1, 1.2))
	Shape cShape = Shape.cir(c)
	Shape rShape = Shape.rec(r)
	
	flt cArea = shapeArea(&cShape)
	flt rArea = shapeArea(&rShape)
	if (cArea > rArea) {
		print("Circle won")
	} elif (cArea < rArea) {
		print("rec won")
	} else {
		print("draw")
	}
}

runFun()
```


#### Definitions
- tagged union - a data type in which we specify what types it can hold; it can only hold one type at a time and simultaneously has a tag indicating which type it currently holds.
- strong - strictly defined type, to use in the context of another type, it must be converted, there are operations for specific sets of types
- mutable - a variable can change its value by default
- value - the way of passing an argument to a function - by default by value
### Project Structure

- Modules:
  - **File parser** - parses the file to provide subsequent characters from the stream in a consistent format (standardizes newline characters, etc.), supports utf-8 - provides source abstraction
  - **Lexical Analyzer** - extracts characters and groups them into lexical atoms - tokens (type, value, location in code)
  - **Syntax Analyzer** - generates tokens into syntactic structures, recognizes what sequences of tokens were present, and determines what that sequence means
  - **Semantic Analyzer** - checks the correctness of the meaning of syntactic structures
  - **Error Handling** - stops at the first error and prints the error, similar to the higher example
- Communication between modules can take place via a RabbitMQ queue, concept:
  - The lexical analyzer buffers up to one additional token for faster handling of syntax analyzer requests
![[project_structure.drawio.png]]
### Grammar
- Written in EBNF

```
program                = statment, {statment} ;

statment               = structure
                       | function
                       | block;

structure              = ("struct" | "TaggedUnion"), name, "{", var_declar_l, {",", var_declar_l}, "}";

function               = "fun", name, "(", [var_declar, {",", var_declar_l}], ")", "-", ">", (type | "void"), "{", block, {block} "}";

block                  = conditional
                       | variable_init
                       | name_block
                       | match_block
                       | break_block
                       | return_block;

name_block             = name,  (variable_assignemt | function_call | arithmatic_standalone | name_variable_init | struct_param_assign);

return_block           = "return", expression;

match_block            = "match", name, "{", match_case, {match_case}, "}";
match_case             = customTypeName, ".", name, "(", name, ")", "{", block, {block}, "}";

break_block            = "break";
return_block           = "return", expression; 

conditional            = if_condition
                       | loop_condition;
if_condition           = "if", condition, "{", block, { block } "}", {"elif", condition, "{", block, {block} "}"} ["else", "{", block, {block} "}"],
loop_condition         = "while", condition, "{", block, { block } "}" ;
condition              = "(", expression, ")";


function_call          = "(", [expression, {"," expression}] ")"; (* also method call *)

variable_init          = ["gscope"], ["const"], type, name, "=", expression;
name_variable_init     = name, "=", expression; (*type read as a name*)
variable_assignemt     = "=", expression;
struct_param_assign    = {".", name}, "=" expression;


var_declar             = type, name;
var_declar_l           = ["const"], var_declar;


expression             = and_condition, ["or", and_condition],
and_condition          = logical_exp, ["and", logical_exp];

logical_exp            = arithmetic_result , [tester,  arithmetic_result ];
tester                 = "<" | "<=" | ">" | ">=" | "==" | "!=";

arithmetic_result      = arithmetic_2tier, {("+" | "-"), arithmetic_2tier};
arithmetic_2tier       = arithmetic_3tier, {("*" | "/" | "%"), arithmetic_3tier};
arithmetic_3tier       = value, {"**", value};

value                  = ([-] | ["not"] ), (literal | name_value | "(" expression ")");

literal                = int | flt | str | bool | name_value;
name_value             = name, (variable_call | struct_init_or_call | function_call); (* just name is variable call *)
struct_init_or_call    = ".", name, {".", name}, [struct_init];
struct_init            = "(" expression ")";


type                   = ["&"], name;
comment                = "#.*$"

arithmatic_standalone  = ("++" | ("+=" (name | numeric_expression) ));

```

### Technical Specification

#### Technologies
- Java
- Maven
- Git

#### Launch
```
java -jar Speed.jar fileName
```
The program captures the terminal in which it was launched and outputs to it and receives data from it. Optionally, you can specify a recursion limit:
```
java -jar Speed.jar fileName --recursion-limit=800
```
#### Tests
- besides unit tests for selected parts of the code:
  - the lexical analyzer will receive a string of characters and check if the returned token is as expected
  - for the syntax analyzer, it will be checked whether it returns appropriate syntactic structures after given tokens


# Lexical Analyzer

### Tokens

| Type                        | Value                                                                                                    |
| --------------------------- | -------------------------------------------------------------------------------------------------------- |
| Keywords                    | int, flt, fun, match, str, bool, struct, TaggedUnion, print, const, while, if, elif, and, or, not, return |
| Additive Operator           | +, -                                                                                                      |
| Multiplicative Operator     | \*, /, %                                                                                                  |
| Increment Operator          | ++, +=, **                                                                                                |
| Relational Operator         | >, >=, <, <=, \==, !=                                                                                     |
| assignment operator         | =                                                                                                         |
| returned type operator      | ->                                                                                                        |
| Numerical Values            | 1, 2, 1.2...                                                                                              |
| Text Values                 | ".*"                                                                                                      |
| Parentheses Operators       | (, ), {, }                                                                                                |
| Reference Operator          | &                                                                                                         |
| Structure Value Operator    | .                                                                                                         |
| variable name               | .*                                                                                                        |

### Variable Value Limitations:
- int range -> (-2147483647, 2147483647)
- float precision -> same range as int, and 6 digits after the decimal point
- Max string length -> 200 
### Unit Test Coverage:
![[Pasted image 20240410223636.png]]

### Parser
#### Class Diagram:
![[parser-tree.png]]
#### Test Coverage:
![[Pasted image 20240508221945.png]]
low coverage results from not testing class getters and setters
