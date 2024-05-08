#### Specyfikacja od prowadzącego:
- silne
- mutowalne
- wartość
### Działanie języka
#### Typy danych z przykładami kodu typów danych
##### Podstawowe typy danych liczbowych:
- Typ pełno liczbowy
	- `int a = 5`
- Typ zmienno-przecinkowy
	- `flt a = 5.0`
- Przy podawaniu niepoprawnych wartości do inicjalizowania zmiennych zostaje wyrzucony #Error `TypeError`
##### Typ znakowy - String
- `str a = "ala ma kota"`
- "\\" escapowane
	- sekwencje znaków specjalnych:
		- `\\` -> \
		- `\"` -> "
		- `\t` -> tab
		- `\n` -> nowa linia
	- przykład:
```
>> str a = "\"ala\"\\\"jacek\" to\tosoby \n\t ważne"
>> print(a)
"ala"\"jacek" to    osoby
	ważne
```
##### Typ logiczny:
- `bool` przyjmuje wartości: `true` `false`
	- `bool a = true`
##### Inne typy danych ():
- Struktury:
	- umożliwiają na utworzenie własnej struktury z atrybutami o podanych typach
```
struct Box {
	str name,
	const str color,
	int height
}

Box b = Box("klocek", "zielony", 2)
b.height = 5
```
- Tagged union:
	- obiekt przechowuje tylko jeden z możliwych typów zdefiniowanych przez programistę
	- aby dostać się do wartości danego pola należy najpierw "zmatchować" odpowiednio tym przechowywany i dobrać do niego operacje
	- programista nie musi obsługiwać wszystkich typów z danego TaggedUnion
```
TaggedUnion Grade {
	int numeric,
	str decsriptional,
	flt curved
}

Grade gr = Grade.numeric(1)
match gr {
	Grade.numeric(value) {
		print("int value " + str(value))
	}
	Grade.decriptional(value) {
		print("str value " + value)
	}
	Grade.curved(value) {
		print("flt value " + str(value))
	}
}
```
##### Rzutowanie typów
- Ze względu na silne typowanie możliwym jest zmiana typu danej zmiennej aczkolwiek musi być to zrobione świadomie, operator rzutowania wygląda następująco <typ-na-który-rzutujemy>(zmienna), przykład:
```
int a = 8
flt b = int(a)
```
- Rzutowanie:
	- `int` może być na:
		- `flt` - zawsze możliwe
		- `str` - zawsze możliwe
	- `flt` może być na:
		- `int` - zawsze możliwe, wartość jest obcięta do wartości całkowitych, np `int(5.6)` zwróci 5, tak samo jak `int(5.4)`
		- `str` - zawsze możliwe
	- `str` może być na:
		- `int` - jeśli wartość `str` jest nieprawidłowa zostanie rzucony #Error `TypeError`
#### Operatory
- Operatory - operator można użyć tylko między dwoma takimi samymi typami, nie jest dozwolone używanie różnych typów w momencie użycia dwóch różnych typów rzucany jest #Error `TypeError`:
	- dodawanie  `+`;
		- Typy: `int` `flt` `str`
	- odejmowanie `-`; mnożenie `*`; dzielenie `/`; potęgowanie `**`; inkrementacja `+=`;  modulo `%`
		- Typy: `int` `flt`
	- (pre/post)inkrementacja `++`;
		- Typy: `int`
	- nierówności `<` `<=` `>` `>=` `==` `!=`
		- Typy: `int`, `flt`
	- logiczne `or` `and`
		- Typy: `bool`
	- operator przypisania `=`
		- przypisuje do danej zmiennej **wartość**
```
int a = 9
int b = a
a++
print(a) # wypisuje 10
print(b) # wypisuje 9
```
-  **wyjątek** operacji na dwóch różnych typach:
	- operator `*` może być użyty między `str` i `int` w następujący sposób:
```
>> str a = "a"
>> str b = a * 5
>> print(b)
aaaaa
```
- hierarchia operatorów (im wyżej dany operator tym ma większy priorytet):
	- `++` `**`
	- `*` `/` `%` `+=`
	- `+` `-`
#### Funkcje wbudowane:
- print - wypisanie tekstu na konsolę
	- możliwe typy: `str`
```
>>print(1)
1
```

#### Komentarze
- jednolinijkowe -  rozpoczynające się od `#` i będące na końcu danej lini lub zaczynające się od nowej lini
```
# komentarz
int a = 1 # inny komentarz
```
#### Semantyka zmiennych
- Typowanie
	- silne statyczne
- Mutowalność:
	- domyślnie mutowalne
	- aby były niemutowalne należy dodać słowo kluczowe `const`:
		- `cont int a = 5`
- zakres widoczności zmiennych:
	- przykrywanie zmiennych
		- zmienne w ramach danego bloku lub w ramach swojej widoczności na czas swojej żywotności przykrywają zmienne o tej samej nazwie zdefiniowane wcześniej o widoczności większej.
		- na przykład zmienna utworzona w bloku przykryje zmienną globalną utworzoną wcześniej na czas istnienia bloku:
```
int a = 6

fun test() {
	str a = "kolcek"
	print(a)
}

print(a) # wypisze 6
test() # wypisze klocek

```
- domyślnie blok (niektóre funkcje są duże i działają dość długo), mogą mieć widoczność funkcji/globalną
	- widoczność danej zmiennej globalnie można osiągnąć poprzez słowo kluczowe `gscope` -> global scope, zmienne globalne są **niemutowalne!**
	- przykład widoczności w bloku:
```
fun test() {
	if (x == 0) {
		fscope int b = 7
		int a = 6
		print(a)
	}
	print(a) # a nie jest tutaj widoczne
	print(b) # b jest tutaj widoczne
}
```
- instrukcja warunkowa:
	- `if` `else`, budowa przykład kodu:
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
- instrukcja pętli
	- `while` - przykład kodu:
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
#### Funkcje
- definiowania własnych:
	- rozpoczynamy od słowa kluczowego `fun`
	- podajemy typ zwracany
	- podajemy argumenty razem z ich typami
	- domyślnie argumenty do funkcji przekazywane są przez wartość aby przekazać je przez referencję użyć znaku `*`
	- Jeżeli funkcja nie zwraca żadnego typu należy w miejsce typu wpisać `void`
	- Jeżeli typ przekazany do funkcji nie będzie się zgadzał z deklarowanym typem zostanie rzucony #Error `TypeError`
- możliwość wołania - wywołanie funkcji następuje poprzez podanie nazwy funkcji oraz argumentów funkcji
- przeciążanie funkcji **nie** jest możliwe
- **nie** dopuszczane jest posiadanie funkcji i zmiennej o tych samych nazwach
- może istnieć funkcja main działająca analogicznie jak w cpp
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
- rekursywne wywołania funkcji
	- Jest możliwe ale istnieje limit: 500
	- limit dla danej funkcji można zmienić za pomocą dodania argumentu wywołania: recursion-limit = x, przykład w [[#Specyfikacja technologiczna]]
```
fun silnia(int n) {
	if (n == 0) {
		return 1
	}
	return silnia(n-1)
}
```
#### Błędy
- W wypadku wystąpienia błędu system zaprzestaje działanie "panikuje" poprzez wypisanie napotkanego błędu na konsole oraz miejsce gdzie został on napotkany, przykładowy błędny kod i wypisanie błędu:
```
1 int a = 8
2 a = 9.3 # TypeError
```

```
>> simple file.si
Traceback: file.si, line 2, character 4
	TypeError: inncorect value for int
```
##### Lista możliwych błędów
- TypeError (przykład powyżej)
- NameError - zmienna g nie istnieje:
```
int a = 9
int b = 0
c = g + b
```
- SyntaxError - niepoprawny warunek, brak {} po intrukcji warunkowej:
```
if name == 2:
	print("a")
```
- ZeroDevisionError

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
#### Złożone przykłady kodu

```
# Przykładowy kod

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

fun circleArea(&Circle c) -> flt {
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


#### Definicje
- tagged union - typ danych któremu w definicji podajemy jakie typy może on przechowywać, może on przechowywać tylko jeden typ jednocześnie i jednocześnie ma znacznik jaki aktualnie typ przechowuje.
- silne - typ ściśle określony, aby użyć w kontekście innego typu należy skonwertować, istnieją operacje dla konkretnych zestawów typów
- mutowalne - zmienna domyślnie może mieć zmienianą wartość
- wartość - sposób przekazywania argumentu do funkcji - domyślnie poprzez wartość
### Struktura projektu

- Moduły:
	- **File parser** - parsuje plik aby dostarczać w stałym formacie kolejne znaki ze strumienia (ujednolica znaki nowych lini etc.), wspierany utf-8 - dostarcza abstrakcję źródła
	- **Analizator leksykalny** - wyodrębnia znaki i grupuje w atomy leksykalne - tokeny  (typ, wartość, miejsce w kodzie)
	- **Analizator składniowy** - generuje tokeny w struktury składniowe, rozpoznaje jakie sekwencje tokenów były i rozpoznaje co ta sekwencja oznacza
	- **Analizator semantyczny** - sprawdza poprawność znaczenia struktur składniowych
	- **Obsługa błędów** - przerwanie przy pierwszym błędzie i wypisanie błędu, analogicznie do wyższego przykładu
- Komunikacja między modułami może się odbywać za pomocą kolejki RabbitMQ, pomysł:
	- Analizator leksykalny buforuje o maksymalnie jeden dodatkowy token w celu szybszej obsługi zapytań analizatora składniowego
![[struktura_projektu.drawio.png]]
### Gramatyka
- Napisana w EBNF

```
program                = statment, {statment} ;

statment               = structure
                       | function
                       | expresion;

structure              = ("struct" | "TaggedUnion"), name, "{", var_declar_l, {",", var_declar_l}, "}";

function               = "fun", name, "(", [var_declar, {",", var_declar_l}], ")", "-", ">", (type | "void"), "{", (expresion | returnExp), {(expresion | returnExp)} "}";

expresion              = conditional
                       | variable_init
                       | name_expression
                       | matchExp;


name_expression         = name,  (variable_assignemt | function_call | arithmatic_standalone | name_variable_init | struct_call)

returnExp              = "return", value;

matchExp               = "match", name, "{", matchCase, {matchCase}, "}";
matchCase              = customTypeName, ".", name, "(", name, ")", "{", expresion, {expresion}, "}"

conditional            = if_condition
                        | loop_condition;
if_condition           = "if", condition, "{", expresion, {expresion} "}", {"elif", condition, "{", expresion, {expresion} "}"} ["else", "{", expresion, {expresion} "}"],
loop_condition         = "while", condition, "{", expresion, {(expresion} "}" ;
condition              = "(", and_condition, {"or", and_condition}, ")";
and_condition          = check, {"and", check};
check                  = name
                       | test
                       | bool ;
test                   = value, tester, value;
tester                 = "<" | "<=" | ">" | ">=" | "==" | "!=";

function_call          = "(", [value, {"," value}] ")"; (* also method call *)

variable_init          = ["gscope"], ["const"], type, name, "=", value;
name_variable_init     = name, "=", value; (*type read as a name*)
variable_assignemt     = "=", value;
struct_call            = ".", name, {".", name}, ([ "(" value ")"] | "=" value); (*fist name is in name exp*)

var_declar             = type, name;
var_declar_l           = ["const"], var_declar;

type                   = ["&"], (normalType | customTypeName);
normalType             = "int" | "flt" | "str" | "bool";
name                   = [&], "[a-zA-Z][a-zA-Z0-9]*";
numeric_value          = int | flt;
int                    = non-zero-digit, {digit};
flt                    = int, ".", digit;
str                    = "\"[^"]*\"";
bool                   = "true" | "false";
value                  = [-] int | flt | str | bool | name | arithmetic_result | function_call;
customTypeName         = name;
digit                  = "[0-9]"
non-zero-digit         = "[1-9]"
comment                = "#.*$"

arithmatic_standalone  = ("++" | ("+=" (name | numeric_value) ))

arithmetic_result      = arithmetic_2tier, {("+" | "-"), arithmetic_2tier};
arithmetic_2tier       = arithmetic_3tier, {("*" | "/" | "%"), arithmetic_3tier};
arithmetic_3tier       = arithmetic_prod, {"**", arithmetic_prod};
arithmetic_prod        = value | "(" , arithmetic_result, ")";
```

#QUESTION jak wziąść pod uwagę komentarze na końcu lini
### Specyfikacja technologiczna

#### Technologie
- Java
- Maven
- Git

#### Uruchomienie
```
java -jar Speed.jar fileName
```
Program przechwytuje terminal w którym został uruchomiony i wypisuje na niego oraz pobiera z nich dane, opcjonalnie można podawać limit rekursji:
```
java -jar Speed.jar fileName --recursion-limit=800
```
#### Testy
- poza testami jednostkowymi wybranych części kodu:
	- analizator leksykalny będzie dostawał ciąg znaków i sprawdzany będzie czy przekazany token będzie taki jak spodziewany
	- do analizatora składniowego będzie sprawdzane czy po danych tokenach zwraca odpowiednie struktury składniowe


# Analizator leksykalny

### Tokeny

| Typ                         | Wartość                                                                                                   |
| --------------------------- | --------------------------------------------------------------------------------------------------------- |
| Słowa kluczowe              | int, flt, fun, match, str, bool, struct, TaggedUnion, print, const, while, if, elif, and, or, not, return |
| Operator addytywny          | +, -                                                                                                      |
| Operator multiplikowany     | \*, /, %                                                                                                  |
| Operator inkrementacyjny    | ++, +=, **                                                                                                |
| Operator relacji            | >, >=, <, <=, \==, !=                                                                                     |
| operator przypisania        | =                                                                                                         |
| operator zwracanego typu    | ->                                                                                                        |
| Wartości numeryczne         | 1, 2, 1.2...                                                                                              |
| Wartości tekstowe           | ".*"                                                                                                      |
| Operatory nawiasowe         | (, ), {, }                                                                                                |
| Operator referencji         | &                                                                                                         |
| Operator wartości struktury | .                                                                                                         |
| nazwa zmiennej              | .*                                                                                                        |

### Ograniczenia wartości zmiennych:
- Zakres int-a-> (-2147483647, 2147483647)
- Dokładność float-a -> zakres taki sam jak int, oraz 6 cyfr po przecinku
- Max długość string-a -> 200 
### Covrege testów jednostkowych:
![[Pasted image 20240410223636.png]]

### Parser
#### Diagram klas:
![[parser-tree.png]]
#### Pokrycie testów:
![[Pasted image 20240508221945.png]]
niskie pokrycie wynika między innymi z nie testowania get-erów i set-erów klas
