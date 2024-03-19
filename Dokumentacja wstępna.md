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
		- `\'` -> '
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
##### Inne typy danych (STL?):
-  lista - przechowuje kolekcję obiektów stałego typu, lista zaimplementowana w formacie linked listy, obiekty mogą być jednym z wbudowanych typów lub własnym typem, przy próbie dodania niepoprawnego typu do listy pojawia się #Error `TypeError`
	- metody:
		- length() -> zwraca długość listy
		- get(int index) -> zwraca element na zadanym indeksie
		- add(T obj) -> dodaje obiekt do listy
		- set(int index, T obj) -> ustawia wartość obiektu na podanym adresie na podaną wartość 
```
list<int> a = []
a.add(1)

list<flt> b = [1.0]
print(b.get(0)) # prints 1.0

```
- dict - przechowuje parę klucz wartość, przy próbie dodania niepoprawnego typu do dict-a pojawia się #Error `TypeError`
	- metody:
		- keys() -> zwraca listę kluczy
		- get(key) -> zwraca obiekt od danego klucza
```
dict<str, flt> klucze = {"klucz": 1.2, "klucz1": 1.8}
klucze.set("klucz") = 1.1
```
- Struktury: 
	- umożliwiają na utworzenie własnej struktury z atrybutami o podanych typach 
	- umożliwiają zdefiniowania własnych funkcji
	- domyślnie i atrybuty i metody są **prywatne**, aby były publiczne należy dodać słowo kluczowe `pub` - public
	- atrybuty są w sekcji init
	- init jest obowiązkową częścią struct-a
	- jest możliwość odwołania się do obiektu samego w sobie poprzez słowo kluczowe - `this` 
```
struct Box {
	init (name, color, x) {
		pub str name = name
		const str color = color
		int height = x
	}
	pub fun int volume() {
		return (height ** 3)
	}
	fun bool isiBig() {
		return this.volume() > 6
	}	
}

Box b = Box("pudelko", "czarne", 1)
```
- Tagged union:
	- obiekt przechowuje tylko jeden z możliwych typów zdefiniowanych przez programistę
	- można wywołać na nim metodę `curType` w celu sprawdzenia aktualnego typu przechowywanego
	- drugą metodą jest `data` zwracająca dane w obiekcie
```
TaggedUnion Grade {
	int numeric
	str decsriptional
	flt curved
}

Grade gr = Grade.numeric(1)
if (gr.curType == Grade.numeric) {
	print(str(gr.data))
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
	- nawiasy  `()`
		- Do użycia w skomplikowanych operacjach do modelowania priorytetów operacji
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
- input - odbiera jedną linijkę z konsoli wypisując uprzednio na konsolę tekst podany jako argument
	- `str a = input("Wpis coś ")`
- abs - wartość bezwzględna 2 liczb
	- możliwe typy: `flt`, `int`
- max - największa wartość z 2 podanych lub z listy podanych
	- możliwe typy: (`int`, `int`), (`flt`, `flt`), `list`
- range - zwraca listę `int`-ów z danego przedziału
```
>>print(str(abs(max(-5, -1))))
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
		- na przykład zmienna utworzona w bloku nadpisze zmienną globalną utworzoną wcześniej na czas istnienia bloku:
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
	- widoczność danej zmiennej w całej funkcji może zostać osiągnięta przez słowo kluczowe `fscope` -> function scope
	- widoczność danej zmiennej globalnie można osiągnąć poprzez słowo kluczowe `gscope` -> global scope
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
	- if, budowa przykład kodu:
```
if (x == 1) {
	print("1")
}
if (x == 1) { print ("1")}
```
- instrukcja pętli
	- while - przykład kodu:
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
fun flt kmToMiles(flt km) {
	return km * 0.6
}

fun void changeKmToMiles(&flt km) {
	km = km * 0.6
}

flt milesCopy = kmToMiles(10.1)

flt milesRef = 10.4
changeKmToMiles(&milesRef)

```
- rekursywne wywołania funkcji
	- Jest możliwe ale istnieje limit: 500
	- limit dla danej funkcji można zmienić za pomocą dekoratora nad funkcją: #QUESTION *wywalić dekorator, dać argument w wywołaniu interpretera?*
```
@recursive-limit=800
fun silnia(int n) {
	if (n == 0) {
		return 1
	}
	return silnia(n-1)
}
```
#### Błędy
- W wypadku wystąpienia błędu system zaprzestaje działanie "panikuje" poprzez wypisanie napotkanego błędu na konsole oraz miejsce gdzie został on napotkany, przykładowy błędny kod i wypisanie błędu: #todo wpisz errory wszystkie możliwe
```
1 int a = 8
2 a = 9.3 # TypeError
```

```
>> simple file.si
Traceback: file.si, line 2, character 4
	TypeError: inncorect value for int 
```
##### Lista możliwych wyjątków
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
- IndexError -> specyficzne dla listy
```
myList = [0, 1]
myList.get(4)
```
- KeyError -> specyficzne dla dict
```
dict = {"key": 1}
dict.get("keeey")
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

#QUESTION *czy robić że istnieje w jakiejś formie main? (tak jak funkcja main w cpp)* za - fajnie żeby istniała jakoś sformalizowana przestrzeń w kodzie z której uruchamiany jest program, przeciw - w sumie po co, można i bez tego (chyba) a więcej pisać programista musi

```
# Przykładowa implementacja sortowania bąbelkowego

fun list<int> bubble_sort(&list<int> arr) {
	int n = arr.length()
	int i = 0
	while (i < n) {
		int j = 0
		while (j < (n - i-1)) {
			if (arr.get(j) > arr.get(j+1)) {
				int temp = arr.get(j)
				arr.set(j, arr.get(j)
				arr.set(j+1, temp)
			}
			k++
		}
		i++
	}
}

list<int> arr = [1, 2, 3, 3, 2, 1]
bubble_sort(&arr)

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
			           | expresion 
			           | comment;

structure              = struct
			           | union;

struct                 = "struct" "{", init, {methodes}, "}" ;
init                   = "init", "(", attributes, {attributes}, ")" ;
attributes             = ["pub"], no_scope_variable;
methodes               = ["pub"], function;

union                  = "TaggedUnion", name, "{, no_scope_variable_dec, {no_scope_variable_dec}, "}";

function               = "fun", ("void" | type), name, "(", no_scope_variable_dec, {no_scope_variable_dec}, ")", "{", expresion, {expresion} "}";

expresion              = conditional
					   | variable
					   | function_call
					   | arithmatic_standalone;
					   
conditional            = ("if" | "while"), "(", condition, ")", "{", expresion, {expresion} "}";
condition              = check, {(("or"|"and"), check)};
check                  = variable_call
					   | test
					   | ("true" | "false") ;
test                   = value, tester, value;
tester                 = "<" | "<=" | ">" | ">=" | "==" | "!=";

function_call          = [(name, ".")], name, "(", {(variable_call | value)}, ")"; (* also method call *)

variable               = variables_result_init | variables_value_init;
variables_result_init  = [scope], [type], name, "=", (function_call | arithmetic_result );
variables_value_init   = [scope], (primitive | composite-type | custom-type) ;

primitive              = (["int"], name, "=", int)
                       | (["flt"], name, "=", flt)
                       | (["str"], name, "=", str);
composite-type         = (["list"], name, "=", list)
					   | (["dict"], name, "=", dict);
custom-type-var        = type, name, "=", customTypeInit; (*name of var, name of type *)

no_scope_variable_dec  = type, name;

type                   = ["&"], (normalType | dictType | listType | customTypeName);
normalType             = "int" | "flt" | "str";
listType               = "list", "<", type, ">";
dictType               = "dict", "<", type, ",", type, ">";
name                   = "[a-zA-Z0-9]*";
numeric_value          = int | flt;
int                    = non-zero-digit, {digit};
flt                    = int, ".", digit;
str                    = "\"[^"]*\"";
list                   = "[", [value], {",", value}, "]"
dict                   = "{", {(value, ":", value)}, "}"
value                  = int | flt | str | list | dict | customType | name | arithmetic_result | function_call;
customTypeInit         = name "(", value, ")";
customTypeName         = name;
digit                  = "[0-9]"
non-zero-digit         = "[1-9]"
comment                = "#.*$"

arithmatic_standalone  = name, ("++" | ("+=" (name | numeric_value) ))

arithmetic_result      = arithmetic_2tier, {("+" | "-"), arithmetic_2tier};
arithmetic_2tier       = arithmetic_3tier, {("*" | "/" | "%" | "+="), arithmetic_3tier};
arithmetic_3tier       = arithmetic_prod, ["++", "**"];
arithmetic_prod        = numeric_value | name | "(" , arithmetic_result, ")";
```

#QUESTION jaki sens ma value jak na końcu jest name (nazwa zmiennej), która jest w sumie czymkolwiek, powtarzanie się name
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
Program przechwytuje terminal w którym został uruchomiony i wypisuje na niego oraz pobiera z nich dane
#### Testy
- poza testami jednostkowymi wybranych części kodu:
	- analizator leksykalny będzie dostawał ciąg znaków i sprawdzany będzie czy przekazany token będzie taki jak spodziewany
	- do analizatora składniowego będzie sprawdzane czy po danych tokenach zwraca odpowiednie struktury składniowe