# Principles of Programming 
* Lecture & Practice Session
	* [LectureHompage](https://github.com/snu-sf-class/pp201701)

Imperative Programming:

* Sequence of read/write operations

Functional Programming:

* More declarative, more high-level description
* More reliability, and safe


## Type-Oriented Programming
스칼라는 directly 하게 type class를 다루지는 않는다. 오직 Object-oriendted 만을 지원한다. 그러나 이를 사용해서 type class비슷하게 할수 있다. Object-oriendted의 경우 data와 method라면, type class에서는 type과 method이다. 아래 코드에서 `Point`는 type이다. `plus`이는 오직 두개의 type을 받아들인다. oop에서 method는 직접 자신의 data에 접근하지만, type class 에서는 그러지 않는다. oop에서는 객체 자체가 method를 가지고 있지만, type class에서는 type자체가 method를 가지고 있다.  
Example)

```scala
val Point = new {
	def plus(p: Point, q: Point): Point = 
		new Point(p.px + q.px, p.py + q.py)
}

== 

object Point {
	def plus(p: Point, q: Point): Point = 
		new Point(p.px + q.px, p.py + q.py)
}
```

## 3월 9일
## Functional Programming with Function Applications

### Values, Expression, Names
* Type은 value들의 집합이다. 
	* ex) `Int: {-(2^32-1), ..., 2^32-1 }` 
* Expression은 value와 name, primitive operation의 조합.
* Name Binding (= Programming)은 expression과 이름을 binding 시키는 것이다. Name은 just idenitifier. Functional Programming에서 name이라 하면 value를 말하는 것, name을 evaluation하면 언제나 같은 결과를 준다. Name never change.
	
	```scala
	def a = 1 + (2 + 3)
	def b = 3 + a * 4
	```
	
### Evaluation
Evaluation은 expression을 value로 reducing하는 것이다. **name**이나 **operator**를 밖에서 부터 안쪽으로 (?) 취한다. 

* **name**의 경우 binding된 expression으로 바꾸고 expression을 evaluation한다.  
* **operator**의 경우 operands들을 evaluation하고 (left to right) operands에 operator를 적용한다.

Scala는 Typed Functional Programming Language다. 그러나 Type은 Fucntional Programming의 essential한 부분이 아니다. Type은 compiler가 내 프로그램을 check할 수 있도록 도와준다.

### Functions and Substitution
**Functions**은 Expression과 Parameters로 이루어져 있다. 또한 name과 binding할 수 있다.

```scala
def f(x: Int): Int = x + a
```

**Evaluation** by **Substitution**, (function)은 operands (argument)를  left to right로 evaluate한다. function application을 자신의 expression (body)로 replace하고 body의 parameter들을 operands (argument)로 replace한다.  
This scehme of expression evaluation is called the ***substitution model.*** The idea underlying this model is that all evaluation does is *reduce an expression to a value.*

>Substitution은 **rewriting**이다.

## 3월 14일
### Simple Recursion
* **Rescursion**
	* Use X in the definition of X
	* Powerful mechanism for **repetition**, repetition이 없다면, 모든 loop을 손으로 일일이 다 적어줘야 한다. 
	* Nothing special but just **rewriting** 
	* 다만 자신의 Recursion을 사용하려는 Function은 자신의 Type을 지정해줘야 한다.

```scala
def sum(n: Int): Int = 
	if (n <= 0)
		0
	else 
		n + sum(n-1)
->
sum(2)
~ if (2<=0) 0 else (2+sum(2-1)) 
~ 2+sum(1) 
~ 2+(if (1<=0) 0 else (1+sum(1-1))) 
~ 2+(1+sum(0))
~ 2+(1+(if (0<=0) 0 else (0+sum(0-1))))~ 2+(1+0)
~ 3
```

### Termination/Divergence
Evaluation may not terminate

* Termination
	* An expression may reduce to a value
* Divergence
	*  an expression may reduce forever

```scala
def loop: Int = loop
->
loop ~ loop ~ loop ~ ...
```

스칼라에서도 `def n = 3 + 4` 이렇게 정의하는데 메모리에 덮어씌우는 거 아니냐? 메모리를 사용하긴 하지만 **immutable**하다. 즉 한번 정의되고 나면 메모리의 content가 바뀌지 않는다. Recursion의 경우도 그 함수 (scope) 안에서 정의된 parameter의 값이 바뀌지 않는다. 다시 함수 call을 한다면 다시 함수가 불리는 것이므로 다른 scope인 것. 

### Evaluation strategy: Call-by-value, Call-by-name
* Call-by-value
	* Evaluate the argument first, then apply the function to them.
* Call-by-name
	* Just apply the function to its arguments, without evaluating them. 값이 필요한 순간에 계산한다 (lazy evaluation).

```scala
def square(x: Int) = x * x

[cbv]square(1+1) ~ square(2) ~ 2*2 ~ 4
[cbn]square(1+1) ~ (1+1)*(1+1) ~ 2*(1+1) ~ 2*2 ~ 4
```

### CBV, CBN: Differences
* Call-by-value
	* Evaluate argument once
* Call-by-name
	* Do not evaluate unused arguments

**They do not always result in the same value.**  

### Scala's evaluation strategy
* Call-by-value
	* by default
* Call-by-name
	* Use `=>`

```scala
def one(x: Int, y: => Int) = 1
one(1+2, loop) -> 1
one(loop, 1+2) -> divergence
```

### Scala's name binding strategy
* Call-by-value
	* Use `val` (also called "field") e.g.,`val x = e`
	* Evaluate the expression first, then bind name to it
* Call-by-name 
	* Use `def` (also called "method") e.g., `def x = e`
	* Just bind the name to the expression, without evaluating it
	* Mostly used to define functions
	
	```scala
	def loop: Int = loop
	
	def a = 1 + 2 + 3
	val a = 1 + 2 + 3   // 6
	def b = loop
	val b = loop		// divergence
	
	def f(a: Int, b: Int): Int = a*b - 2
	```

그런데 `val f: Int => Int = (x: Int) => x * x`라고 정의해도 `f(7)`처럼 사용이 가능하다. 또한 anonymous function또한 `val`로 정의된다. 어떻게 된 것 일까? `f`는  **class type**의 *값* 이다; `f`는 `Function1`이라는 class type의 value이다. 그러므로 `f(x)`는  `f.apply(x)`이다.

```scala
val f: Int => Int = (x: Int) => x * x
->
val f = new Function1[Int, Int] {
	def apply(x: Int)= x * x
}
f(7) -> f.apply(7)
```

그렇다면 `apply`는 object일까? method이가 object라면 `apply`가 끝없이 expansion 되므로 method는 object가 아니다. 그러나 

```scala
def f(x: Int): Boolean = ...
```
`f`가 Function type이 expected 되는 곳에 사용된다면, `f`는 자동적으로 function value로 바뀐다.

```scala
(x: Int) => f(x)
or
new Function1[Int, Boolean] {
	def apply(x: Int) = f(x)
}
```
> Method is not a function. So if we want to put the method where a Function type is expected, we need to convert the method to function value.

고민하던 call-by-name parameter를 lambda function에 넘기는 법

```scala
val f: (=> Int) => Int = x => x * x
val fun: (Boolean, => Int) => Int = (x, y) => if (x) y else 0
```

### Conditional Example
* If-else
	* `if (b) e_1 else e_2`
	* `b`: Boolean expression
	* `e_1`, `e_2`: expressions of the same type
	* `e_1` and `e_2` are treated as **call-by-name**, and `b` is treated as call-by-value.
* Rewrite rules:
	* `if (ture) e_1 else e_2 -> e1`
	* `if (false) e_1 else e_w -> e2`
	
	```scala
	def abs(x: Int) = if (x >= 0) x else -x
	``` 
 
 함수가 expression을 return 할 수 있나? 함수가 expression을 return 하냐는 것은 의미가 없다. 함수는 언제나 value를 return한다. 그러나 applied 함수가 call-by-name으로 그 함수를 받을 경우 늦게 evaluation되는 것 뿐이다. 
 
 ```scala
 def f(x: Int) = x + x
 def g1(x: Int) = x * x
 def g2(x: =>Int) = x * x
 ```
 
 `g1(f(3))`의 경우 `f(3)`이 먼저 evaluation 될 것이고, `g2(f(3))`의 경우 `f(3)`의 call-by-name으로 넘어갈 것이고, 그 value가 필요할 때 evaluate 될 것이다.
	
### Boolean Expression
* Boolean expression
* `&&` and `||` treats its operand call-by-name.

```scala
true, false
!b
b && b
b || b
e <=, e >= 3, e < e, e > e, e == e, e != e
```

* Rewrite rules:

```scala
!true -> false
!false -> true
true && b -> true
false && b -> false
true || b -> true
false || b -> b

true && (loop == 1) ~ loop == 1 ~ loop == 1
```

### Exercise: and, or
* Write two functions
	* `and(x, y) == x && y`
	* `or(x, y) == x || y`
	* Do not use `&&`, `||`

```scala
def and(x: Boolean, y: =>Boolean): Boolean = {
	if (x) y
	else false
}

def or(x: Boolean, y: =>Boolean): Boolean = {
	if (x) true 
	else y
}

and(false, loop == 1)
~ if (false) loop == 1 else false
~ false

and(true, loop == 1)
~ if (true) loop == 1 else false
~ loop == 1 ~ loop == 1 ...

or(true, loop == 1)
~ if (true) true else loop == 1
~ true

or(false, loop == 1)
~ if (false) true else loop == 1
~ loop == 1 ~ loop == 1 ...
```

### Exercise: square root calculation
* Calculate square roots with Newton's method

```scala
// (guess * guess - x).abs/x < 0.001
def isGoodEnough(guess: Double, x: Double) = 
	((guess * guess - x).abs/x < 0.001)
	
def improve(guess: Double, x: Double) = 
	(guess + x/guess)/2
	
def sqrtIter(guess: Double, x: Double): Double = 
	if (isGoodEnough(guess, x)) guess
	else sqrtIter(improve(guess, x), x)
	
def sqrt(x: Double) =
	sqrtIter(1, x)
	
sqrt(2)
```

## Blocks ans Name Scoping
### Block in Scala
* Block

	```scala
	{ 
		val x1 = e1
		def x2 = e2
		e
	}
	```
* Is an single **expression**
* Allow nested name binding
* Allow arbitrary order of `def`s, but not `val`s (think about why)
	* `val a = b; val b = a` 이러한 cycle을 만들지 않기 위해서.
	
	```scala
	val a = b    // raise error. a is value, the compiler wants to evaluate b. 
	val b = 2    // but b defined here. Over the a's scope, so the compiler raises error.
	
	def a = b    // call-by-name, it's not evalute right now.
	def b = 2    
	
	val c = d    // raise error.
	def d = 2
	
	def e = f    // raise error.
	val f = 4
	```    
### Scope of names
* Block

```scala
val t = 0
def square(x: Int) = t + x * x
val x = square(5)
val r = {
	val t = 10
	val s = square(5) // s =25
	t + x
}
val y = t + r // t = 10, y = 35
```

* A definition inside a block is only accessible within the block
* A definition inside a block shadows definitions of the same name outside the block 
* A definition inside a block is accessible unless it is shadowed
* **A function is evaluated under the environment where it is defined, not the enviornment where it is invoked.**

### Rewriting for blocks
```scala
1: val t = 0
2: def f(x: Int) = t + g(x)
3: def g(x: Int) = x * x
4: val x = f(5)
5: val r = {
6:	 val t = 10
7:	 val s = f(5)
8:	 t + s }
9: val y = t + r
```

* Evalutaion by rewriting

```
[],1 
~ [t=0],2
~ [..., f=(x)t+g(x)],3 
~ [..., g=(x)x*x],4 
~ [...,x=25],5 
~ [...]:[],6 
~ [...]:[t=10],7~ [...]:[...,s=25],8 
~ [...,r=35],9 
~ [...,y=35],10
4: [t=0, f=..., g=..., x=25]:[x=5], t+g(x) ~ 0+g(5) ~ 25
g(5): [t=0, f=..., g=...]:[x:5], x*x ~ 5*5 ~ 25
7: [t=0, f=..., g=..., x=25]:[x=5], t+g(x) ~ 0+g(5) ~ 25
g(5): [t=0, f=..., g=..., x=25]:[x:5], x*x ~ 5*5 ~ 25
```

### Semi-colons and Parenthesis
* Block 
	* Can write more than two definitions/expressions in a single line using `;`
	* Can write one definition/expression in two lines using `()`, but can omit `()` when clear

```scala
// OK
val r = {
	val t = 10; val s = square(5); t +
	s }
// Not OK
val r = {
	val t = 10; val s = square(5); t
	+ s }
// OK
val r = {
	val t = 10; val s = square(5); (t + 
	s) }
```

### Exercise Writing Better Code using Blocks
```scala
def sqrt(x: Double) = {
	def sqrtIter(guess: Double): Double = 
		if (isGoodEnough(guess)) guess
		else sqrtIter(improve(guess))
		
	def isGoodEnough(guess: Double) = 
		((guess * guess - x).abs/x < 0.001)
	
	def improve(guess: Double) = 
		(guess + x/guess)/2
	
	sqrtIter(1)
}
	
sqrt(2)
```

scope의 장점: name의 life time을 결정할 수 있다. name을 감출수 있다.

## Lazy Call-By-Value

### Lazy call-by-value
* Lazy call-by-value
	* Use `lazy val` e.g., `lazy val x = e`
	* Evaluate the expression **first time it is used**, then bind the name to it

```scala
def f1(c: Boolean, iv: Int): Int = {
	if (c) 0
	else iv * iv * iv
}

f1(true, { println("ok"); 100+100+100+100 }) // ok 0f1(false, { println("ok"); 100+100+100+100 }) // ok 64000000

def f2(c: Boolean, iv: => Int): Int = {
	if (c) 0
	else iv * iv * iv
}

f2(true, { println("ok"); 100+100+100+100 }) // 0f2(false, { println("ok"); 100+100+100+100 }) // ok ok ok 64000000

def f3(c: Boolean, i: => Int): Int = {
	lazy val iv = i
	if (c) 0
	else iv * iv * iv
}

f3(true, { println("ok"); 100+100+100+100 }) // 0f3(false, { println("ok"); 100+100+100+100 }) // ok 64000000
```

## Tail Recursion
### Recursion needs care
* Summation function
	* Write a summation function `sum` such that
	
	```scala
	sum(n) = 1 + 2 + ... + n
	```
	
	* Test

	```scala
	sum(1), sum(100), sum(1000), sum(10000), 
	sum(100000), sum(1000000)
	```
	
	* What's wrong? (Think about evaluation)

### Recursion: Try 
```scala
def sum(n: Int): Int =
	if (n <= 0) 0 else n + sum(n-1)
```
call stack이 함수를 계속 쌓아가는걸 메모리가 견딜 수가 없음

### Recursion: Tail Recursion
```scala
import scala.annotation.tailrec

def sum(n: Int): Int = {
	@tailrec
	def sumItr(res: Int, m: Int): Int = 
		if (m <= 0) res else sumItr(m + res, m - 1)
	sumItr(0, n)
}
```
call stack이 함수를 쌓아가지 않고, stack frame을 교체 하면서 계산함. depth를 고려해서 함수를 설계하고 만약 depth가 많이 깊어지면 tail recursion으로 함수를 설계하라.

## Higher-Order Functions
### Functions as Values
* Functions
	* Functions are normal values of function types `(A_1, ..., A_n => B)`.
	* They can be copied, passed and retured.
	* Functions that take functinos as arguments or return functions are called higher-order functions.
	* Higher-order functions increase code reusability.

### Examples
```scala
def sumLinear(n: Int): Int = 
	if (n <= 0) 0 else n + sumLinear(n-1)

def sumSquare(n: Int): Int =
	if (n <= 0) 0 else n * n + sumSquare(n-1)
	
def sumCubes(n: Int): Int =
	if (n <= 0) 0 else n * n * n + sumCubes(n-1)
```

Q: How to write reusable code? 

```scala
def sum(f: Int => Int, n: Int): Int =	if (n <= 0) 0 else f(n) + sum(f, n-1)
	def linear(n: Int) = ndef square(n: Int) = n * ndef cube(n: Int) = n * n * n
def sumLinear(n: Int) = sum(linear, n)def sumSquare(n: Int) = sum(square, n)def sumCubes(n: Int) = sum(cube, n)
```

### Anonymous Functions
* Anonymous Functions
	* Syntax 
	
	```scala
	(x_1: T_1, ..., x_n: T_n) => e
	or
	(x_1, ..., x_n) => e
	```
	ex)
	
	```scala
	((x: Int) => x*x)(100) // 10000
	```  
	
```scala
def sumLinear(n: Int) = sum((x:Int) => x, n)def sumSquare(n: Int) = sum((x:Int) => x*x, n)def sumCubes(n: Int) = sum((x:Int) => x*x*x, n)
```Or simply

```scaladef sumLinear(n: Int) = sum((x) => x, n)def sumSquare(n: Int) = sum((x) => x*x, n)def sumCubes(n: Int) = sum((x) => x*x*x, n)
```

### Exercise
```scala
def sum(f: Int => Int, a: Int, b: Int): Int = 	if (a <= b) f(a) + sum(f, a+1, b) else 0def product(f: Int => Int, a: Int, b: Int): Int = 
	if (a <= b) f(a) * product(f, a+1, b) else 1
```
DRY (Do not Repeat Yourself) using a higher-order function, called "mapReduce"

```scala
def mapReduce(map: Int => Int, reduce: (Int, Int) => Int, a: Int, b: Int, init: Int): Int = 
	if (a <= b) reduce(map(a), mapReduce(map, reduce, a+1, b, init)
	else init
/*	call-by-name map and reduce function
def mapReduce(map: => (Int => Int), reduce: => ((Int, Int) => Int), a: Int, b: Int, init: Int): Int = 
	if (a <= b) reduce(map(a), mapReduce(map, reduce, a+1, b, init)
	else init
*/

def sum(f: Int => Int, a: Int, b: Int): Int =
	mapReduce(f, _+_, a, b, 0)

def product(f: Int => Int, a: Int, b: Int): Int = 
	mapReduce(f, _*_, a, b, 1)
```

## 3월 23일
### Parameterized expression vs. values
* Functions defined using `def` are not values but parameterized expressions.
* Anonymous functions are values.
* But, parameterized expressions are implicitly converted to values.
* Explicit conversion: `f _`
* Anonymous functions can be seen as syntatic sugar:

	```scala
	(x: T) => e
	```
	is equivalent to
	
	```scala 
	{ def __noname(x: T) = e; __noname _ }
	```	
	(`e` must not use `__noname`)
* One can even write a recursive anonymous functions in this way.

Here are some questions.

* Q: **what's the difference between param, exps and function values?**
* A: functions values are "closures" (ie. param. exp. + env.)
* Q: **how to implement call-by-name?**
* A: The argument expression is converted to a closure.

### Closures for functinoal values
```scala
1: val t = 02: val f = {3:     val t = 104:     def g(x: Int): Int = x + t5:     g _ }    // not g, need g _6: f(20)
```

* Try: Evaluation without Closures

```scala
[],1 
~ [t=0],2
~ [...]:[],3 
~ [...]:[t=10],4~ [...]:[...,g=(x)x+t],5 
~ [t=0,f=(x)x+t],6 
~ [...],206: [t=0,f=(x)x+t]:[x=20],x+t ~ 20+0 ~ 20
```
`g` is a parameterized expression. But the `f` is value. So what't the value of function? 또한 `def`는 자신이 정의 됐던 environment에서 evaluation 되야 한다. `g`를 `f`에 그냥 copy 하는 것은 문제가 생긴다. (만약 `val f`가 아니라 `def f`라면 그런 문제가 안생기나? 애초에 closure 이전의 정의는 `def` 정의 안에 `val`이 들어가는 것이 가능하긴 하나? 즉 `def` clause 안에서 정의된 변수들을 사용할 경우 closure를 정의 하지 않고서는 불가능 한지 여쭤보자, 언제 env를 없애는지?, `var`로 정의해서 값을 바꾸면 값이 바뀐다. 모든 `def`를 저장할 때 closure로 바꿔서 저장하는 것도 하나의 방법이다. `var`는 포인터라고 생각해라)

function value는 parameterized expression과 어디서 정의 되었는지 (origin information) 를 둘다 가지고 있어야 한다. (**environment where define**, parameterized expression)은 value 이다. 이 value를 closure라 한다. 

* Evaluation with Closures

```scala
[],1 
~ [t=0],2 
~ [...]:[],3 
~ [...]:[t=10],4~ [...]:[...,g=(x)x+t],5~ [t=0,f={[t=0]:[t=10,g=(x)x+t],(x)x+t}],6 
~ [...],306: [t=0]:[t=10,g=(x)x+t]:[x=20],x+t ~ 20+10 ~ 30
```

### Example: call by name with closrues
```scala
1: val t = 0
2: def f(x: => Int) = t + x // x is treated as x()
3: val r = {
4:     val t = 10
5:     f(t * t) }          // t*t is treated as () => t*t
```

* Evaluation with Closures

```scala
[],1 
~ [t=0],2 
~ [...,f=(x: => Int)t+x],3 
~ [...]:[],4 
~ [...]:[t=10],5 
~ [...,r=100],65: [t=0,f=...]:[x={[t=0,f=...]:[t=10],()t*t}],t+x 
~ 0 + x ~ 0 + 100 ~ 100x: [t=0,f=...]:[t=10]:[],t*t ~ 10*10 ~ 100
```

`()=>t*t`로 취급 되므로 `x`를 evaluate할 때 `t`가 정의된 곳으로 가서 `t`의 값을 찾는다. 즉 `t*t`를 (env, parm exp)인 closure로 넘긴다.  
expression을 넘기고 싶으면 closure로 package해서 넘겨야 핸다. evaluation시에는 unpack을 한 후 define 됐던 enviroment로 가서 eval한다. origin environment를 Provenance라 한다.

## Currying
### Motivation
```scala
def sum(f: Int => Int, a: Int, b: Int): Int = 	if (a <= b) f(a) + sum(f, a+1, b) else 0def linear(n: Int) = ndef square(n: Int) = n * ndef cube(n: Int) = n * n * ndef sumLinear(a: Int, b: Int) = sum(linear, a, b)
def sumSquare(a: Int, b: Int) = sum(square, a, b) 
def sumCubes(a: Int, b: Int) = sum(cube, a, b)
```

We don't want write `a`, `b` repeatedly. We want the following. How?

```scala
def sumLinear = sum(linear)def sumSquare = sum(square)def sumCubes = sum(cube)
```

### Solution
```scala
def sum(f: Int => Int): (Int, Int) => Int = {
	def sumF(a: Int, b: Int): Int =
		if (a <= b) f(a) + sumF(a+1, b) else 0
	sumF
}

def sumLinear = sum(linear)def sumSquare = sum(square)def sumCubes = sum(cube)
```

### Benefits
```scala
def sumLinear = sum(linear)def sumSquare = sum(square)def sumCubes = sum(cube)
sumSquare(3,10) + sumCubes(5,20)
```

We don't need to define the wrapper functions.

```scala
sum(square)(3,10) + sum(cube)(5,20)
```

### Multiple Parameter List
```scala
def sum(f: Int => Int): (Int, Int) => Int = {
	def sumF(a: Int, b: int): Int = 
		if (a <= b) f(a) + sumF(a+1, b) else 0
	sumF
}
```

We can also write as follows.

```scala
def sum(f: Int => Int): (Int, Int) => Int = 
	(a, b) => if (a <= b) f(a) + sum(f)(a+1, b) else 0
```

Or more simply:

```scala
def sum(f: Int => Int)(a: Int, b: Int): Int =
	if (a <= b) f(a) + sum(f)(a+1, b) else 0
```
If I use `sum(square)`, is it a currying? And then, I can use like `sum(_: Int => Int)(1, 10)`? Yes! 매우 신기하다 아마도 automatically conversion이 일어나서 가능한 일인 것 같다. 알아서 currying의 순서를 조정해 준다.

### Currying and Uncurrying
* A function of type

```scala
(T_1, T_2, ..., T_n) => T
```
can be turned into one of type

```scala
T_1 => (T_2 => (... => (T_n => T)...)
```

* This is called "currying" named after Haskell Brooks Curry.
* The opposite direction is called "uncurrying"

### Currying using Anonymous Functions
```scala
// (Int, Int, Int) => ((Int, Int) => Int)
def foo(x: Int, y: Int, z: Int)(a: Int, b: Int): Int = 
	x + y + z + a + b// (Int, Int, Int) => Intval f1 = (x: Int, z: Int, b: Int) => foo(x,1,z)(2,b)
// (Int, Int, Int) => Int, automatically conversion
val f2 = foo(_:Int,1,_:Int)(2, _:Int)
// (Int, Int) => (Int => Int)val f3 = (x: Int, z: Int) => (b: Int) => foo(x,1,z)(2,b)
f1(1,2,3)  // 9f2(1,2,3)  // 9f3(1,2)(3) // 9
```

### Exercise
Curry the `mapReduce` functions.

```scala
def mapReduce(reduce: (Int, Int) => Int, init: Int)(map: Int => Int)(a: Int, b: Int): Int = {
	if (a <= b) mapReduce(reduce, init)(map)(a+1, b)
	else init
}

// need to make a closure since mapReduce is param. code.
def sum = mapReduce(_+_, 0) _

// val is better than def. Think about why.
val product = mapReduce(_*_, 1) _
```

`val`이 더 나은 이유는 `def`로 name binding 하면 함수를 call 할 때 마다 currying 다시 하기 때문이 아닐까. `val` binding 하면, Function value로 return 되기 때문에 선언시에만 currying이 이뤄진다. Computed된 새로운 closure를 `def`는 매번 다시 계산하고, `val`은 한번만 계산된다.

## 3월 28일

## Exceptions
### Exceptions & Handling
```scala
class factRangeException(val arg: Int) extends Exception

def fact(n: Int): Int = 
	if (n < 0) throw new factRangeException(n)
	else if (n == 0) 1
	else n * fact(n - 1)
	
def foo(n: Int) = fact(n + 10)

try {
	println(fact(3))
	println(foo(-100))
} catch {
	case e: factRangeException => {
		println("fact range error: " + e.arg)
	}
}
```

## Datatypes
### Types so far
Types have introduction operations and elimination ones.

* Introduction: **how to construct** elements of the type
* Elimination: **how to use** elements of the type

* Primitve types
	*  `Int`, `Boolean`, `Double`, `String`
	*  Intro for `Int`: ...`-2`, `-1`, `0`, `1`, `2`
	*  Elim for `Int`: `+`, `-`, `*`, `/`, `<`, `<=`, ...
	
* Function types
	* `Int => Int`, `(Int => Int) => (Int => Int)`
	* Intro: `(x: T) => e`
	* Elim: `f(v)` 

### Tuples
* Tuples
	* Intro: 
		* `(1, 2, 3): (Int, Int, Int)`
		* `(1, "a"): (Int, String)`
		
	* Elim: 
		* `(1, "a", 10)._1 = 1`
		* `(1, "a", 10)._2 = "a"`
		* `(1, "a", 10)._3 = 10`

Only up to length 22

### Structural Types (a.k.a Record Types): Examples
```scala
object foo {    // or, val foo = new {
	val a = 3
	def b = a + 1
	def f(x: Int) = b + x
	def f(x: String) = "hello " + x
}

foo.f(3)
foo.f("gil")

def g(x: { val a: Int; def b: Int;
           def f(x: Int): Int; def f(x: String): String }) =
    x.f(3)
    
g(foo)
```

It is named tuple type.

### Structural Types: Scope and Type Alias
```scala
val gn = 0
object foo {
	val a = 3
	def b = a + 1
	def f(x: Int) = b + x + gn
}

foo.f(3)    // 7 same scoping rule

type Foo = { val a: Int; def b: Int; def f(x: Int): Int }

def g(x: Foo) = {
    val gn = 10
    x.f(3)
}
    
g(foo)      // 7 same scoping rule
```

`def`와 `val`의 차이? 모든 `def`를 `val`로 대체할 수 있나? non-termination이 없다면 `def`와 `val`은 동일하다? 

```scala
def loop: Int = loop
def x(): Int = x
val a: () => Int = x    // cannot assign loop
```

### Algebraic Datatypes
* Ideas 
	
	```scala
	T = C of T * ... * T
	  | C of T * ... * T
	  |  ...
	  | C of T * ... * T
	```  
* E.g.

	```scala
	Attr = Name of String
	     | Age of Int
	     | DOB of Int * Int * Int
	     | Height of Double
	
	Intro:
	      Name("Chulsoo Kim"), Name("Younghee Lee"), Age(16),
	      DOB(2000, 3, 10), Height(171.5), ...
	```
Algebraic Datatype을 그냥 class를 사용해서 대체할 수 있지만, algebraic datatype은 pattern matching을 가능하게 해준다. Pattern matching은 elimination의 한 형태 이다.

### Algebraic Datatypes: Recursion
* Recursive ADT
	* E.g.
	
		```scala	 
		IList = INil
		      | ICons of Int * IList       Intro:
              INil(), ICons(3, INil), ICons(2, ICons(1, INil)), ...
       ```

### Algebraic Datatypes In Scala
* `Attr`

	```scala
	sealed abstract class Attr	case class Name(name: String) extends Attr	case class Age(age: Int) extends Attr	case class DOB(year: Int, month: Int, day: Int) extends Attr 
	case class Height(height: Double) extends Attr
		val a: Attr = Name("Chulsoo Kim")
	val b: Attr = DOB(2000,3,10)
	```

* `IList`

	```scala
	sealed abstract class IList	case class INil() extends IList	case class ICons(hd: Int, tl: IList) extends IList	val x: IList = ICons(2, ICons(1, INil()))
	```

### Exercise
```scala
IOption = INone        | ISome of IntBTree = Leaf      | Node of Int * BTree * BTree
```

### Solution
```scala
sealed abstract class IOption
case class INone() extends IOption
case class ISome(some: Int) extends IOption

sealed abstract class BTree
case class Leaf() extends BTree
case class Node(value: Int, left: BTree, right: BTree) extends BTree
```

### Pattern Matching
* Pattern Matching
	* A way to use algebraic datatypes
	* **Heart** of Algebraic Datatypes

	```scala
	e match {
		case C1(...) => e1
		...
		case Cn(...) => en
	}
	```
	 
### Pattern Matching: An Example
```scala
def length(xs: IList): Int = 
	xs match {
		case INil() => 0
		case ICons(x, tl) => 1 + length(tl)
	}

length(x)

def gen(n: Int): IList = 
	if (n < 0) INil()
	else ICons(n, gen(n - 1))
```
Pattern matching은 case analysis이다. 각각의 `case`는 같은 `type`을 내놔야 한다.

### Advanced Pattern Matching
* Advanced Pattern Matching

	```scala
	e match {
		case P1 => e1
		...
		case Pn => en
	}
	```
* One can combine constructors and use `_` and `|` in a pattern
	* `case ICons(x, INil()) | ICons(x, ICons(_, INil())) => ...`  
* The given value `e` is matched against the first pattern `P1`.  
If succeeds, evaluate `e1`.  
If fails, `e` is matched against `P2`.  
If succeeds, evaluate `e2`.  
If fails, ...
* The compiler checks exhaustiveness. You should not have missing pattern.

## 3월 30일

### Advanced Pattern Matching: An Example
```scala
def secondElmt(xs: IList): IOption = xs match {
		case INil() | ICons(_, INil()) => INone()
		case ICons(_, ICons(x, _)) => ISome(x)
}
```
Vs.

```scaladef secondElmt2(xs: IList): IOption = xs match {	case INil() | ICons(_, INil()) => INone() 
	case ICons(_, ICons(x, _)) => ISome(x) 
	case _ => INone()}
```

### Pattern Matching on Int
```scala
def factorial(n: Int): Int = n match {	case 0 => 1	case _ => n * factorial(n-1) 
}def fib(n: Int): Int = n match {	case 0 | 1 => 1	case _ => fib(n-1) + fib(n-2) 
}
```

### Pattern Matching with If
```scala
def f(n: Int): Int = n match {	case 0 | 1 => 1 
	case _ if (n <= 5) => 2
	case _ => 3}
def f(t: BTree): Int = t match {	case Leaf() => 0	case Node(n, _, _) if (n <= 10) => 1 
	case Node(_, _, _) => 2}
```

### Exercise
Write a function `find(t: BTree, x: Int)` that checks whether `x` is in `t`.

```scala
@tailrec
def find(t: BTree, x: Int): Boolean = t match {
	case Leaf() => false
	case Node(n, left, right) => 
		if (x == n) true
		else if (x < n) find(left, x)
		else find(right, x)
}

// if it is not binary search tree
def find(t: BTree, x: Int): Boolean = t match {
	case Leaf() => false
	case Node(n, left, right) => 
		if (x == n) true
		else if find(left, x) || find(right, x)    // evaluate right-hand side only if left returns false
}

// How about tail recursion
// I need to use List! It is really difficult.
def find(t: BTree, x: Int): Boolean = {
	@tailrec
	def nestedFind(treeList: List[BTree]): Boolean = treeList match {
		case Nil => false
		case t :: ts => t match {
			case Leaf() => nestedFind(ts)
			case Node(n, left, right) => 
				if (x == n) true
				else nestedFind(left :: right :: ts)
		} 
	}
	
	nestedFind(List(t))
}

// How about sum of all tree nodes with tail recursion
def sum(t: BTree): Int = {
	@tailrec
	def nestedSum(acc: Int, treeList: List[BTree]): Int = treeList match {
		case Nil => acc
		case t :: ts => t match {
			case Leaf() => nestedSum(acc, ts)
			case Node(n, left, right) => nestedSum(acc + n, left :: right :: ts)
		}
	}
	
	nestedSum(0, List(t))
}

def t: BTree = Node(5,Node(4,Node(2,Leaf(),Leaf()),Leaf()), Node(7,Node(6,Leaf(),Leaf()),Leaf()))find(t,7)
```

## Type Checking & Inference (Concept)
### What Are Types For?
* Typed Programming

	```scala
	def id1(x: Int): Int = x
	def id2(x: Double): Double =x
	```
	
	* At run time, type information is erased (ie, `id1` = `id2`)

* Untyped Programming

	```scala
	def id(x) = x
	```
	
	* Do not care about types at compile time.
	* But many such languages check types at run time paying cost.
	* Without run-time type check, errors can be badly propagated.

* Why is compile-time type checking for?
	* Can detect	type errors at compile time. 
	* Increase Readability (Give a good abstraction).
	* Soundness: Well-typed programs raise no type errors at run time.

### Type Checking and Inference
* Type Checking 

	```scala
	x1:T1, x2:T2, ..., xn:Tn => e: T
	```
	
	* `def f(x: Boolean): Boolean = x > 3` => Type error
	* `def f(x: Int): Boolean = x > 3` => OK. `f: (x: Int)Boolean`

* Type Inference

	```scala
	x1:T1, x2:T2, ..., xn:Tn => e: ?
	```  
	
	* `def f(x: Int) = x > 3` => Ok by type inference. `f: (x: Int)Boolean`
	* Too much type inferences is not good. Why?
		* 사람이 못알아 봐서 

## 4월 4일
## Parametric Polymorphism
### Parametric Polymorphism: Functions
* Problem

	```scala
	def id1(x: Int): Int = x
	def id2(x: Double): Double = x
	```
	
	* Can we avoid DRY?
	* Polymorphism to the rescue!

* Parametric Polymorphism (a.k.a. For-all Types)

	```scala
	def id[A](x: A): A = x
	```
	
	* The type of `id` is `[A](x: A)A`
	* `id` is a parametric expression
	* `id[T] _` is a value of type `T => T` for any type `T`.
		* 실험 했을 때는 `Nothing => Nothing` 이 되어 value로 만들 수 없었다.

		```scala
		def id[A](x: A): A = x
		val a = id _
		// a(3)  error
		```
	* There is no polymorphic type because of technical reason.

### Examples
```scala
def id[A](x: A) = x

id(3)
id("abc")

def applyn[A](f: A => A, n: Int, x: A): A = n match {
		case 0 => x
		case _ => f(applyn(f, n - 1, x))
}
	
applyn((x: Int) => x + 1, 100, 3)
applyn((x: String) => x + "!", 10, "gil")
applyn(id[String], 10, "hur")

def foo[A, B](f: A => A, x: (A, B)): (A, B) =
	(applyn[A](f, 10, x._1), x._2)
	
foo[String, Int]((x: String) => x + "!", ("abc", 10))

def gee[A, B](f: ((A, B)) => (A, B), x: (A, B)): (A, B) = 
	applyn[(A, B)](f, 10, x)
```

### Full Polymorphism using Scala's trick
```scala
type Applyn = { def apply[A](f: A => A, n: Int, x: A): A }

object applyn {
	def apply[A](f: A => A, n: Int, x: A): A = n match {
			case 0 => x
			case _ => f(apply(f, n - 1, x))
	}
}

applyn((x: String) => x + "!", 10, "gil")

def foo(f: Applyn): String = {
	val a: String = f[String]((x: String) => x + "!", 10, "gil")
	val b: Int = f[Int]((x: Int) => x + 2, 10, 5)
	a + b.toString()
}

foo(applyn)
```

만약 polymorphic function type을 argument로 넘기고 싶다면, record type으로 만들어서 넘기면 된다.

```scala
def foo(f[A]: A => A) = {
	f[String]("hi")
	f[Int](3)
}
```

위와 같은 함수는 존재 할 수 없다.

### Parametric Polymorphism: Datatypes
```scala
sealed abstract class MyOption[A]
case class MyNone[A]() extends MyOption[A]
case class MySome[A](some: A) extends MyOption[A]

sealed abstract class MyList[A]
case class MyNil[A]() extends MyList[A]
case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A]

sealed abstract class BTree[A]
case class Leaf[A]() extends BTree[A]
case class Node[A](value: A, left: BTree[A], right: BTree[A]) extends BTree[A]

def x: MyList[Int] = MyCons(3, MyNil())
def y: MyList[String] = MyCons("abc", MyNil())
```

### Exercise
```scala
BSTree[A] = Leaf
          | Node of Int * A * BSTree[A] * BSTree[A]
```

```scala
sealed abstract class BSTree[A]
case class Leaf[A]() extends BSTree[A]
case class Node[A](key: Int, value: A, left: BSTree[A], right: BSTree[A]) extends BSTree[A]

def lookup[A](t: BSTree[A], k: Int): MyOption[A] = t match {
		case Leaf() => MyNone()
		case Node(key, value, left, right) => 
			if (k == key) MySome(k)
			else if (k < key) lookup[A](left, k)
			else lookup[A](right, k)
}
	
def t: BSTree[String] =
	Node(5,"My5", Node(4,"My4",Node(2,"My2",Leaf(),Leaf()),Leaf()), Node(7,"My7",Node(6,"My6",Leaf(),Leaf()),Leaf()))
	
lookup(t, 7)
lookup(t, 3)
```

### A Better Way
```scala
sealed abstract class BTree[A]
case class Leaf[A]() extends BTree[A]
// set default value of left and right children
case class Node[A](value: A, left: BTree[A]=Leaf[A](), right: BTree[A]=Leaf[A]()) extends BTree[A]

type BSTree[A] = BTree[(Int, A)]

def lookup[A](t: BSTree[A], k: Int): MyOption[A] = t match {
		case Leaf() => MyNone()
		case Node(v, left, right) =>
			if (k == v._1) MySome(v._2)
			else if (k < v._1) lookup(left, k)
			else lookup(right, k)
}

def t: BSTree[String] = Node((5,"My5"), Node((4,"My4"),Node((2,"My2"))), Node((7,"My7"), Node((6,"My6"))))
	
lookup(t, 7)
lookup(t, 3)
```

### Polymorphic Option (Library)
* `Option[T]`
	* Intro:
	
	```scala
	None
	Some(x)
	Library functions
	```
	
	* Elim:

	```scala
	Pattern matching
	Library functions
	```

```scala
Some(3): Option[Int]
Some("abc"): Option[String]
None: Option[Int]
None: Option[String]
```

### Polymorphic List (Library)
* `List[T]`
	* Intro:

	```scala
	Nil
	x :: L
	Library functions
	```
	
	* Elim:
	
	```scala
	Pattern matching
	Library functions
	```

```scala
"abc"::Nil: List[String]
List(1,3,4,2,5) = 1::3::4::2::5::Nil: List[Int]
```   

# Part 2 Object-Oriented Programming with Subtypes
## 4월 6일
## SubType Polymorphism (Concept)
### Motivation
We want:

```scala
object tom {
	val name = "Tom"
	val home = "02-880-1234"
}

object bob {
	val name = "Bob"
	val mobile = "010-1111-2222"
}

def greeting(r: ???) = "Hi " + r.name + ", How are you?"
greeting(tom)
greeting(bob)
```
We Note that we have

```scalatom: { val name: String; val home: String }bob: { val name: String; val mobile: String }
```

### Sub Types to the Rescue!
```scala
type NameHome = { val name: String; val home: String } 
type NameMobile = { val name: String; val mobile: String } 
type Name = { val name: String }```
`NameHome <: Name` (NameHome is a sub type of Name) 
`NameMobile <: Name` (NameMobile is a sub type of Name)```scaladef greeting(r: Name) = "Hi " + r.name + ", How are you?" 
greeting(tom)greeting(bob)
```

### Sub Types
* The sub type relation is kind of the subset relation
* But they are **NOT** the same.
* `T <: S` Every element of `T` **can be used as** that of `S`.
* Cf. `T` is a subset of `S`. Every element of `T` **is** that of `S`.
* Why polymorphism? 
	* A function of type `S => R` can be used as `T => R` for many subtypes of `T` of `S`.
	* **Note that** `S => R <: T => R` **when** `T <: S` 
		* `T <: S` 라면 `S`의 자리에 `T`가 들어가도 된다는 뜻, 그렇다면 `S => R`은 `T`와 `S` 둘다 받을 수 있으므로 `T => R`의 자리에 `S => R`이 들어갈 수 있다.
	* **Note that** `R => T <: R => S` **when** `T <: S`
		* `T <: S` 라면 `S`의 자리에 `T`가 들어가도 된다는 뜻, 그렇다면 `R => S`는 `S`를 return 해야 하는데 `S`자리에 `T`가 쓰일 수 있으므로 `R => S` 자리에는 `R => T`가 들어갈 수 있다.  

### Two Kinds of Sub Types
* Structural Sub Types
	* The system implicitly determines the sub type relation by the structures of data types.
	* Structurally equivalent types are the same.

* Nominal Sub Types
	* The use explicitly specify the sub type relation using the names of data types.
	* Structurally equivalent types with different names may be different.  

## Structural Sub Types
### General Sub Type Rules
* Reflexivity: For any type `T`, we have:

	```scala
	T <: T
	```

* Transitivity: For any types `T`, `S`, `R`, we have:
	
	```scala
	T <: R      R <: S
	==================
	      T <: S
	``` 
	
### Sub Types for Special Types
* `Nothing`: The empty set
* `Any`: The set of all values
* For any type `T`, we have:

	```scala
	Nothing <: T <: Any
	```
	
* Example

	```scala
	val a: Int = 3
	val b: Any = a
	def f(a: Nothing): Int = a
	```
	
	`Any` 가 올 곳에는 어떠한 type 이 와도 되고, 어떠한 type 자리 에도 `Nothing` 이 들어갈 수 있다. 

### Sub Types for Records
* permutation

	```scala
	==========================================================
	{ ...; x: T_1; y:T_2; ... } <: { ...; y: T_2; x:T_1; ... }
	```
	
* Width
	
	```scala
	==================================
	{ ...; x: T; ... } <: { ...; ... }
	```
	
* Depth

	```scala
	                 T <: S
	========================================
	{ ...; x: T; ... } <: { ...; x: S; ... }
	```
	
* Example 

	```scala
	{ val x: { val y: Int; val z: String }, val w: Int }
	<:        (by permutation)
	{ val w: Int; val x: { val y: Int; val z: String } }
	<:        (by depth & width)
	{ val w: Int; val x: { val z: String } }
	```
	
### Sub Types for Functions
* Function Sub Type

	```scala
	 T <: T`      S <: S` 
	======================
	(T' => S) <: (T => S')
	```
	
* Example

	```scala
	def foo(s: { val a: Int; val b: Int }) :
		{ val x: Int; val y: Int } = {
		object tmp {
			val x = s.b
			val y = s.a
		}
		tmp
	}
	
	val gee:
		{ val a: Int; val b: Int; val c: Int } =>
		{ val x: Int } = 
		foo _
	```
It means `foo <: gee`.  `gee.input <: foo.input` by width. `foo.output <: gee.output` by width. `foo` 에다가 3개의 input을 넣어도 2개만 쓰면되고, `foo`가 두개를 return해도 하나만 쓰면 되므로, `gee`를 `foo`로 대체 가능하다.

## Classes
### Class: Parameterized Record
```scala
object gee {
	val a: Int = 10
	def b: Int = a + 20
	def f(z: Int): Int = b + 20 + z
}
type gee_type = { val a: Int; def b: Int; def f(z: Int): Int }

class foo_type(x: Int, y: Int) {
	val a: Int = x
	def b: Int = a + y
	def f(z: Int): Int = b + y + z
}
val foo: foo_type = new foo_type(10, 20) // value of foo_type not structural type

def zoo_type(x: Int, y: Int) = new {
	val a = x
	val b = a + y
	def f(z: Int): Int = b + y + z
}

class foo_type2(x: Int, y: Int) {
	val a: Int = x
	def b: Int = a + 20
	def f(z: Int): Int = b + 20 + z
}

val foo2: foo_type = foo_type2(10, 20) // type error
```

* use `foo.a` `foo.b` `foo.f`
* `foo` is a value of `foo_type`
* `gee` is a value of `gee_type`

Class looks like function. But there are some differences. Notion of class introduces another typing. Class is not structural type. There is no implicit sub typing between classes.

### Class: No Structural Sub Typing
* Records: Structural sub-typing
	
	```scala
	foo_type <: gee_type
	```

* Classes: Nominal sub-typing

	```scala
	gee_type !<: foo_type
	``` 
	
```scala
val v1: gee_type = foo
val v2: foo_type = gee    // type error
```

### Class: Can be Recursive!
```scala
class MyList[A](v: A, nxt: Option[MyList[A]]) {
	val value: A = v
	val next: Option[MyList[A]] = nxt
}
type YourList[A] = Option[MyList[A]]

val t: YourList[Int] = Some(new MyList(3, Some(new MyList(4, None))))
```

Structural type cannot be recursive. If we want recursive datatype, we need to use algebraic data type. Because syntatically structural data type has no name. And implicit sub typing makes recursive type declaration be impossible.

And implicit sub typing makes programmer can't understand code.

Class and Structural type always contains `null` value. But it is not type safe. Algebraic datatype 과 class 의 차이: class 는 오직 하나의 constructor field 만 존재한다. 반면 algebraic datatype 은 constructor field의 disjoint union 을 제공한다. `null` value 를 제공하는 이유는 이러한 disjoint union 을 class 에도 비슷하게 나마 제공하기 위함이다?

### Note on Null value
* `null`: The special element of every calss & structural type
* This value is needed to construct disjoint union types using classes in Java, which, however, is not as elegant and type safe as algebraic data types (ADTs):
	* Such disjoint union types can contain junk values (not elegant).
	* Null-point exception can be raised at run time (not type safe).

* For this reason, it is discouraged to use `null` in Scala although Scala supports `null` for compatibility with Java.
* Instead, it is encouraged to use ADTs, which themselves are classes and thus take advantaged of both ADT and class. 

### Simplification using Argument Members
```scala
class MyList[A](v: A, nxt: Option[MyList[A]]) {
	val value = v
	val next = nxt
}

class MyList[A](val value: A, val next: Option[MyList[A]]) {
} 

val a: MyList[Int] = new MyList[Int](3, None)
a.value // 3

class MyList[A](val value: A, val next: Option[MyList[A]])
```

### Simplification using Companion Object
```scala
class MyList[A](v: A, nxt: Option[MyList[A]]) {
	val value = v
	val next = nxt
} // can't be length 0

object MyList {
	def apply[A](v: A, nxt: Option[MyList[A]]) =
		new MyList(v, nxt)
}
type YourList[A] = Option[MyList[A]] // can be length 0

val t0 = None
val t1 = Some(new MyList(3, Some(new MyList(4, None))))
val t2 = Some(MyList(3, Some(MyList(4, None))))
```

### Exercise
Define a class "`MyTree[A]`" for binary trees:

```scala
MyTree[A] = 
	(value: A) * 
	(left: Option[MyTree[A]]) *
	(right: Option[MyTree[A]])
```

### Solution
```scala
class MyTree[A](v: A, lt: Option[MyTree[A]], rt: Option[MyTree[A]]) {
	val value = v
	val left = lt
	val right = rt
} // can't be length 0

type YourTree[A] = Option[MyTree[A]] // can be length 0

val t0: YourTree[Int] = None
val t1: YourTree[Int] = Some(new MyTree(3, None, None))
val t2: YourTree[Int] = Some(new MyTree(3, Some(new MyTree(4, None, None)), None))
```

## 4월 13일
## Nominal Sub Typing for Classes
### Nominal Sub Typing, a.k.a Inheritance
```scala
class foo_type(x: Int, y: Int) {
	val a: Int = x
	def b: Int = a + y
	def f(z: Int): Int = b + y + z
}

class gee_type(x: Int) extends foo_type(x+1, x+2) {
	val c: Int = f(x) + b
}
```

```scala
gee_type <: foo_type
```

```scala
(new gee_type(30)).c
def test(f: foo_type) = f.a + f.b
test(new foo_type(10, 20))
test(new gee_type(30))
```

### Overriding 1
```scala
class foo_type(x: Int, y: Int) {
	val a: Int = x
	def b: Int = a + y
	def f(z: Int): Int = b + y + z
}
class gee_type(x: Int) extends foo_type(x+1, x+2) {
	override def f(z: Int) = b + z
	// or, override def f(z: Int) = super.f(z) * 2
	val c: Int = f(x) + b
}
(new gee_type(30)).c
```

```scala
def test(f: too_type) = f.f(10)
test(new gee_type(30))
``` 

여기서 `gee_type` 의 `f` 를 사용한다. dynamically dispatch, type 이 아니라 value 에 의해 결정된다. 이게 class 가 안좋은 이유다. type 에 의해서 결정되는게 아니라서, table 을 봐서 function call 을 해야한다. structural type 의 경우 `f` 는 하나의 value 이고,  다른 value 가 들어오는 것이 자연스러운 것이다. 교수님의 포인트: class 안에 함수를 정의하는 것이 안좋다.

Q: Can we override `val`?  
Yes we can! 

```scala
class gee_type(x: Int) extends foo_type(x+1, x+2) {
	override val a = x + 100
	// or, override def f(z: Int) = super.f(z) * 2
	val c: Int = f(x) + b // f's a is x + 100
	// val d = a, super is only allowed to def 
}
``` 

`val`을 override 해버리면, 그 `val`을 사용하는 `super`에 존재하는 `def`도 바뀐 `val`을 사용하게 된다. 그러나 이것은 `def` 도 마찬가지이다. `def`는 share 될 수 있기 때문에 `super`가 가능하지만, `val`은 모든 객체가 따로 저장해야 하기 때문에 `super`가 불가능하다. 

Q: Can we override with a different type?  
A: Only return type is subtype. You can not change parameter type. If you want to change parameter type, you need to introduce new field.

```scala
override def f(z: Any): Int = 77        // No, arg: diff type (why not sub type?)
def f(z: Any): Int = 77                 // Yes, arg: diff type (but this is a overloading not overring)
override def f(z: Int): Nothing = ???   // Yes, ret: sub type
```

override 를 붙여줘야만 한다. Readibility 를 위한 제약. overriding은 constructor 를 바꾸는 것이다.

### Overriding 2
```scala
class foo_type(x: Int, y: Int) {
	val a: Int = x
	def b: Int = a + y
	def f(z: Int): Int = b + y + z
}

class gee_type(x: Int) extends foo_type(x+1, x+2) {
	override def b = 10
	val c: Int = f(x) + b
}

(new gee_type(30)).c // f 의 b 가 10 으로 바뀐다.
```

### Example: My List
```scala
class MyList[A](v: A, nxt: Option[MyList[A]]) {
	val value = v
	val next = nxt
}
type YourList[A] = Option[MyList[A]]
val t: YourList[Int] = Some(new MyList(3, Some(new MyList(4, None))))
```

Let's use sub typing

```scala
class MyList[A]()

class MyNil[A]() extends MyList[A]
object MyNil { def apply[A]() = new MyNil[A]() }

class MyCons[A](val hd: A, val tl: MyList[A]) extends MyList[A]
object MyCons { def apply[A](hd: A, tl: MyList[A]) = new MyCons[A](hd, tl) }

val t: MyList[Int] = new MyCons(3, (new MyCons(4, new MyNil())))
val t2: MyList[Int] = MyCons(3, MyNil())

def foo(x: MyList[Int]) = ???
```

`null`없이 disjoint union 을 구현할 수 있다. 그러나 `MyList`는 어떠한 field 도 없기 때문에 할 수 있는 작업이 없다. There is no elimination.

### Case Class
```scala
class MyList[A]() { ... }

case class MyNil[A]() extends MyList[A] { ... }
case class MyCons[A]()(hd: A, tl: MyList[A]) extends MyList[A] { ... }

val t: MyList[Int] = MyCons(3, MyNil())
```

**\+ Pattern Matching**

Cf. `sealed abstract class MyList[A]`

### Exercise
Define "`MyTree[A]`" using sub class.

```scala
class MyTree[A](v: A, lt: Option[MyTree[A]], rt: Option[MyTree[A]]) {
	val value =v 
	val left = lt
	val right =rt
}

type YourTree[A] = Option[MyTree[A]]
```

### Solution
```scala
sealed abstract class MyTree[A]
case class MyLeaf[A]() extends MyTree[A] 
case class MyNode[A](v: Int, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

val t: MyTree[Int] = MyNode(3, Node(4, MyLeaf(), MyLeaf()), MyLeaf())

t match {
	case MyLeaf() => 0
	case MyNode(v, l, r) => v
}
```

## Abstract Classes for Specification
### Abstract Class: Specification
* Abstract Classes
	* Can be used to abstract away the implementation details.
	* Does not have constructor.

**Abstract classes for Specification**  
**Concrete sub-classes for Implementation**

* Example Specification

```scala
abstract class Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

def sumElements(xs: Iter[Int]): Int = xs.getValue match {
	case None => 0
	case Some(n) => n + sumElements(xs.getNext)
}
```	 

### Concrete Class: Implementation
```scala
sealed abstract class MyList[A] extends Iter[A]
case class MyNil[A]() extends MyList[A] {
	def getValue = None
	def getNext = this
}
case class MyCons[A](hd: A, tl: MyList[A])  extends MyList[A] {
	def getValue =Some(hd)
	def getNext = tl
}

val t1 = MyCons(3, MyCons(5, MyCons(7, MyNil())))

sumElements(t1)
```

### Exercise
Define `IntCounter(n)` that implements the specification `Iter[A]`

```scala
class IntCounter(n: Int) extends Iter[Int] {
	def getValue = if (n >= 0) Some(n) else None
	def getNext = IntCounter(n - 1)
}

sumElements(new IntCounter(100))
```

## More on Abstract Classes
### Problem: Iter for MyTree
```scala
abstract class Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

sealed abstract class MyTree[A]
case class Empty[A]() extends MyTree[A]
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]
```

Q: Can `MyTree[A]` implement `Iter[A]`?

### Solution: Better Specification
```scala
abstract class Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

abstract class Iterable[A] {
	def iter: Iter[A]
}

def sumElementsGen(xs: Iterable[Int]): Int =
	sumElements(xs.iter)
```

### Let's Use MyList
```scala
sealed abstract class MyList[A] extends Iter[A]
case class MyNil[A]() extends MyList[A] {
	def getValue = None
	def getNext = this
}
case class MyCons[A](val hd: A, val tl: MyList[A]) extends MyList[A] {
	def getValue = Some(hd)
	def getNext = tl 
}
```

### MyTree <: Iterable (Try)
```scala
sealed abstract class MyTree[A] extends Iterable[A]
case class Empty[A]() extends MyTree[A] {
	def iter = MyNil()
}
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A] {
	// "val iter" is more specific than "def iter"
	// so it can be used in a sub type.
	// In this example, "val iter" is also
	// more efficient than "def iter".
	val iter = MyCons(value, ???(left.iter, right.iter))
}
```

### Extend MyList with append
```scala
sealed abstract class MyList[A] extends Iter[A] {
	def append(list: MyList[A]): MyList[A]
}
case class MyNil[A]() extends MyList[A] {
	def getValue = None
	def getNext = this
	def append(lit: MyList[A]) = list
}
case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A] {
	def getValue = Some(hd)
	def getNext = tl
	def append(lst: MyList[A]) = MyCons(hd, tl.append(lst))
}	
```

### MyTree <: Iterable
```scala
sealed abstract class MyTree[A] extends Iterable[A] {
	// more specific type than just iter
	def iter: MyList[A]
	/* Note:
	override def iter: Int // Type Error (no bug in Scala)
	              			// because not (Int <: Iter[A])
	*/
}
case class Empty[A]() extends MyTree[A] {
	def iter = MyNil()
}
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A] {
	// we can recursively define iter.
	val iter = MyCons(value, left.iter.append(right.iter))
}
```

코드를 직접 수정 하지 않아도 원하는 기능을 추가하거나 extends 하게 할 수 있다.

### Test
```scala
def sumElements(xs: Iter[Int]): Int = 
	xs.getValue match {
		case None => 0 
		case Some(n) => n + sumElements(xs.getNext)
	}

val t: MyTree[Int] =
	Node(3, Node(4,Node(2,Empty(),Empty()),
	Node(3,Empty(),Empty())),
	Node(5,Empty(),Empty()))
	
sumElementsGen(t)
```

## 4월 25일

### Iter <: Iterable
```scala
abstract class Iterable[A] {
	def iter: Iter[A]
}

abstract class Iter[A] extends Iterable[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
	def iter = this    // only for children
}

val lst: MyList[Int] =
	MyCons(3, MyCons(4, MyCons(2, MyNil())))

sumElementsGen(lst)
```

## Wrapper for Inheritance
### Using a Wrapper Class
```scala
abstract class Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

class ListIter[A](val list: List[A]) extends Iter[A] {
	def getValue = list.headOption
	def getNext = new ListIter(list.tail)
}
```

라이브러리에서 제공하는 `List` 가 `Iter` 를 extends 하게 하고 싶다면 wrapper 를 만들면 된다.

### MyTree Using ListIter
```scala
abstract class Iterable[A] {
	def iter: Iter[A]
}
sealed abstract class MyTree[A] extends Iterable[A] {
	override def iter: ListIter[A]
}
case class Empty[A]() extends MyTree[A] {
	val iter: ListIter[A] = new ListIter(Nil)
}
case class Node[A](value: A,
                   left: MyTree[A]
                   right: MyTree[A]) extends MyTree[A] {
	val iter: ListIter[A] = new ListIter(
		value :: (left.iter.list ++ right.iter.lit))
}
```

그러나 사용하기 위해서 unwrap 을 해야하므로 불편함이 있다.

### Test
```scala
def sumElements(xs: Iter[Int]): Int = xs.getValue match {
	case Noen => 0
	case Some(n) => n + sumElements(xs.getNext)
}
def sumElementsGen(xs: Iterable[Int]): Int = 
	sumElements(xs.iter)
	
val t : MyTree[Int] =  Node(3, Node(4,Node(2,Empty(),Empty()),    Node(3,Empty(),Empty())),    Node(5,Empty(),Empty()))

sumElementsGens(t)
```

## Abstract Classes With Abstract Types
### Using an Abstract Type
```scala
abstract class Iterable[A] {
	type iter_t    // abstract type
	def iter: iter_t
	def getValue(i: iter_t): Option[A]
	def getNext(i: iter_t): iter_t
}

def sumElements(xs: Iterable[Int]): Int {
	def sumElementsIter(i: xs.iter_t): Int = xs.getValue(i) match {
		case None => 0
		case Some(n) => n + sumElementsIter(xs.getNext(i))
	}
	sumElementsIter(xs.iter)
}
```

`Iter` 구현 없이 `Iterable` 을 구현 할 수 있다. 바로 `List` 를 사용가능 하다. Abstract type 은 abstract class 에서만 사용 가능하다.

### MyTree Using ListIter
```scala
sealed abstract class MyTree[A] extends Iterable[A] {
	type iter_t = List[A]
	def getValue(i: List[A]): Option[A] = i.headOption
	def getNext(i: List[A]): List[A] = i.tail
}
case class Empty[A]() extends MyTree[A] {
	val iter: List[A] = Nil
}
case class Node[A](value: A,
                   left MyTree[A],
                   right: MyTree[A]) extends MyTree[A] {
	val iter = value :: (left.iter ++ right.iter) // Pre-order
	// val iter = left.iter ++ (value :: right.iter) // In-order
	// val iter = left.iter ++ (right.iter ++ List(value)) // Post-order
}
```

### Test
```scala
val t : MyTree[Int] =  Node(3, Node(4,Node(2,Empty(),Empty()),    Node(3,Empty(),Empty())),    Node(5,Empty(),Empty()))
    sumElements(t)
```

## Abstract Classes with Arguments
### Abstract Class with Argument
```scala
abstract class Iterable[A](eq: (A, A) => Boolean) {
	type iter_t
	def iter: iter_t
	def getValue(i: iter_t): Option[A]
	def getNext(i: iter_t): iter_t
	def hasElement(a: A): Boolean = {
		def hasElementIter(i: iter_t): Boolean = getValue(i) match {
			case None => false
			case Some(n) => 
				if (eq(a, n)) true
				else hasElementIter(getNext(i))
		}
		hasElementIter(iter)
	}
}
```

The abstract class's arguments are also for thier children.

### MyTree
```scala
sealed abstract class MyTree[A](eq: (A, A) => Boolean) extends Iterable[A](eq) {
	type iter_t = List[A]
	def getValue(i: List[A]): Option[A] = i.headOption
	def getNext(i: List[A]): List[A] = i.tail
}
case class Empty[A](eq: (A, A) => Boolean) extends MyTree[A](eq) {
	val iter: List[A] = NIl
}
case class Node[A](eq: (A, A) => Boolean,
                   value: A,
                   left: MyTree[A]
                   right: MyTree[A]) extends MyTree[A](eq) {
	val iter: List[A] = value :: (left.iter ++ right.iter)
}
```

### Test
```scasla
val leq = (x: Int, y: Int) => x == y
val lEmpty = Empty(leq)
def INode(n: Int, t1: MyTree[Int], t2: MyTree[Int]) = Node(leq, n, t1, t2)

val t : MyTree[Int] =  INode(3, INode(4,INode(2,IEmpty,IEmpty),                   INode(3,IEmpty,IEmpty)),           INode(5,IEmpty,IEmpty))
           sumElements(t)
t.hasElement(5)
t.hasElement(10)
```

## More on Classes
### Motivating Example
```scala
class Primes(val prime: Int, val primes: List[Int]) {
	def getNext: Primes = { 
		val p = computeNextPrime(prime + 2)
		new Primes(p, primes ++ (p :: Nil))
	}
	def computeNextPrime(n: Int): Int = 
		if (primes.forall((p: Int) => n%p != 0)) n
		else computeNextPrime(n + 2)
}

def nthPrime(n: Int): Int = {
	def go(primes: Primes, k: Int): Int = 
		if (k <= 1) primes.prime
		else go(primes.getNext, k - 1)
	if (n == 0) 2 else go(new Primes(3, List(3)), n)
}
nthPrime(10000)
```

n 번째 prime number 를 구하기 위해서, client 에게 필요한 것은 `Prime(2, 2 :: Nil)` 이다. 이 것을 기본적으로 제공해 줄 수 없을까? Client 의 행동을 control 할 수 없을까?

### Multiple Constructor
```scala
class Primes(val prime: Int, val primes: List[Int]) { 
	def this() = this(3, List(3))
	def getNext: Primes = { 
		val p = computeNextPrime(prime + 2)
		new Primes(p, primes ++ (p :: Nil))
	}
	def computeNextPrime(n: Int): Int = 
		if (primes.forall((p: Int) => n%p != 0)) n
		else computeNextPrime(n + 2)
}

def nthPrime(n: Int): Int = {
	def go(primes: Primes, k: Int): Int = 
		if (k <= 1) primes.prime
		else go(primes.getNext, k - 1)
	if (n == 0) 2 else go(new Primes, n)
}
nthPrime(10000)
```

어느정도 해결은 됐지만 client 가 simple constructor 만 사용하게 하고 싶으면 어떻게 해야할까?

### Access Modifiers
* Access Modifiers
	* Private: Only the class can access the member
	* Protected: Only the class and its sub classes can access the member

### Using Access Modifiers
```scala
class Primes private (val prime: Int, protected val primes: List[Int]) { 
	def this() = this(3, List(3))
	def getNext: Primes = { 
		val p = computeNextPrime(prime + 2)
		new Primes(p, primes ++ (p :: Nil))
	}
	private def computeNextPrime(n: Int): Int = 
		if (primes.forall((p: Int) => n%p != 0)) n
		else computeNextPrime(n + 2)
}

def nthPrime(n: Int): Int = {
	def go(primes: Primes, k: Int): Int = 
		if (k <= 1) primes.prime
		else go(primes.getNext, k - 1)
	if (n == 0) 2 else go(new Primes, n)
}
nthPrime(10000)
```

`private` 를 추가함으로써 User 가 잘못 사용할 수 없게 만들 수 있다.

## Traits for Multiple Inheritance
### Multiple Inheritance Problems
* Multiple Inheritance
	* The famous "diamond problem"

	```scala
	class A(val a: Int)
	class B extends A(10)
	class C extends A(20)
	class D extends B, C
	```
	Problem 1: What is the value of `(new D).a`?  
	Problem 2: The constructor of `A` must be executed once becuase `A` may contain side effects such as sending messages over the network.
	
### Java's Solution: Interface
* Interface
	* An interface cannot contain any implementation but only types of its methods.
	* A class can inherit implementations from only one parent class but implement multiple interfaces. 
	
### Scala's Solution: Trait
* Traits 
	* A trait can implement any of its methods, but should have only one constructor with no arguments.
	* A trait can 
		* "**extend**" only one trait or (abstract) class with no arguments 
		* "**with**" multiple traits.
	* An (abstract) class can
		* "**extends**" only one trait or (abstract) class with any arguments
		* "**with**" multiple traits.   

	Arguments 가 있다면 diamond 상황에서 어떤 argument 를 사용해서 constructor 를 call 해야 할지 ambiguity 가 생긴다. Trait 에서의 dimaond problem 은 ordering 을 통해서 해결한다. 모든 constructor 는 한 번만 불린다.
	
```scala
object Main {
  // A -> B -> E -> C -> F -> D
  def main(args: Array[String]): Unit = {
    class A {
      println("I'm A")
    }

    class B extends A {
      println("I'm B")
    }

    trait E {
      println("I'm E")
      def hi = println("hi E")
    }

    trait C extends A with E {
      println("I'm C")
      override def hi = println("hi C")
    }

    trait F extends E {
      println("I'm F")
      override def hi = println("hi F")
    }

    class D extends B with C with F {
      println("I'm D")
      override def hi = println("hi E")
    }

    val d = new D
    d.hi
  }
}
```
`hi` 는 `D` 의 `hi` 가 불리었다. `D` 의 `hi` 를 지우면, `F` 의 `hi` 가 불린다. 마지막으로 방문한 constructor 의 method 가 불리는 듯

### Example
```scala
class A(val a: Int) {
	def this() = this(0)
}
trait B {
	def f(x: Int): Int = x
}
// C 가 A 를 상속 했으므로 C 를 사용하기 위해서는 무조건 먼저 A 를 상속해야 한다.
trait C extends A with B {    
	def g(x: Int): Int = x + a
}
trait D extends B {
	def h(x: Int): Int = f(x + 50)
}
class E extends A(10) with C with D {
	override def f(x: Int) = x * a
}

val e = new E
```

### Algorithm for Multiple Inheritance
* Algorithm
	* Give a linear order among all ancestors by "post-order" traversing without revisiting the same node.
	* Invoke the constructors once in that order.

N.B. Post-order traversal of a class C means 

* Recursively post-order traverse C's first parent;
* ...
* Recursively post-order traverse C's last parent; and 
* Visit C.

By post-order traversing from `D` in the previous example, we have the order: `A(10) -> B -> C -> D -> E`

```scala
val e = new E
e.a // 10
e.f(100) // 100 * 10
e.g(100) // 100 + 10
e.h(100) // (100 + 50) * 10, f is overrided
```

## A Simple Example With Traits
### Motivation
```scala
abstract class Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

class ListIter[A](val list: List[A]) extends Iter[A] {
	def getValue = list.headOption
	def getNext = new ListIter(list.tail)
}

abstract class Dict[K, V](eq: (K, K) => Boolean) {
	def add(k: K, v: V): Dict[K, V]
	def find(k: K): Option[V]
}
```

Q: How can we extends `ListIter` and impelemnt `Dict`?

### Specification using Traits
```scala
// abstract class Dick[K, V] {
//     def add(k: K, v: V): Dict[K, V]
//     def find(k: K): Option[V]
// }

trait Dict[K, V] {
	def add(k: K, v: V): Dict[K, V]
	def find(k: K): Option[V]
}
```

trait 은 argument 를 받을 수 없지만, 받지 않고 method 로 들고 있는 것과 별 차이가 없다.

### Implementing Traits
```scala
class ListIterDict[K, V](eq: (K, K) => Boolean, 
                         list: List[(K, V)]) extends ListIter[(K, V)](list)
                                             with Dict[K, V] {
    def add(k: K, v: V) = new ListIterDict(eq, (k, v) :: list)
    def find(k: K): Option[V] = {
    	def go(l: List[(K, V)]): Option[V] = l match {
    		case Nil => None
    		case (k1, v1) :: tl => 
    			if (eq(k, k1)) Some(v1) else go(t1) 
    	}
    go(list)
    }
}
```

### Test 
```scala
def sumElements[A](f: A => Int)(xs: Iter[A]): Int = xs.getValue match {
	case None => 0 
	case Some(n) => f(n) + sumElements(f)(xs.getNext)
}

def find3(d: Dict[Int, String]) = {
	d.find(3)
}

val d0 = new ListIterDict[Int, String]((x, y) => x == y, Nil)
val d1 = d0.add(4, "four").add(3, "three")

sumElements[(Int, String)](x => x._1)(d)
find3(d)
```

## Mixin with Traits
### Motivation: Mixin Functionality
```scala
trait Iter[A] {
	def getValue: Option[A]
	def getNext: Iter[A]
}

class ListIter[A](list: List[A]) extends Iter[A] {
	def getValue = list.headOption
	def getNext: Iter[A] = new ListIter(list.tail)
}

trait MRIter[A] extends Iter[A] {
	def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C =???
}
```

`mapReduce` 를 가지고 있는 `ListIter` 를 만들고 싶다.

### Mixin Composition
```scala
trait MRIter[A] extends Iter[A] {
	override def getNext: MRIter[A]
	def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C = getValue match {
		case None => ival
		case Some(v) => combine(f(v), getNext.mapReduce(combine, ival, f))
	}
}

class MRListIter[A](list: List[A]) extends ListIter(list) with MRIter[A] {
	override def getNext: MRIter[A] = new MRListIter(list.tail)    // ugly part
}

val mr = new MRListIter[Int](List(3, 4, 5))
mr.mapReduce[Int, Int]((b, c) => b + c, 0, a => a * a)
```

`getNext` 의 ambiguity 를 어떻게 제거 할까? Ordering 을 이용해서 제거함. 가장 마지막에 불린 constructor 의 method 를 사용함.

### Mixin Composition: A Better Way
```scala
trait MRIter[A] extends Iter[A] {
	def mapReduce[B, C](combine: (B, C) => C, ival: C, f: A => B): C = {
		def go(c: Iter[A]): C = c.getValue match {
			case None => ival
			case Some(v) => combine(f(v), go(c.getNext))
		}
		go(this)
	}
}

class MRListIter[A](list: List[A]) extends ListIter(iter) with MRIter[A]

val mr = new MRListIter[Int](List(3, 4, 5))

// or, val mr = new ListIter(List(3, 4, 5)) with MRIter[Int]

mr.mapReduce[Int, Int]((b, c) => b + c, 0, a => a * a)
```

## Stacking with Traits
### Typcial Hierarchy in Scala
![stack](Images/stack.png)

* **BASE**: Interface (trait or abstract class)
* **CORE**: Functionality (trait or concrete class)
* **CUSTOM**: Modifications (each in separate, composable trait)

### IntStack: Base
* **BASE**

```scala
trait IntStack {
	def get(): (Int, IntStack)
	def put(x: Int): IntStack
}
```

### IntStack: Core
* **CORE**

```scala
class BasicIntStack protected (xs: List[Int]) extends IntStack {
	def this() = this(Nil)
	protected def mkStack(xs: List[Int]): IntStack = new BasicIntStack(xs)
	def get(): (Int, IntStack) = (xs.head, mkStack(xs.tail))
	def put(x: Int): IntStack = mkStack(x :: xs)
}

val s0 = new BasicIntStack()
val s1 = s0.put(3)
val s2 = s1.put(-2)
val s3 = s2.put(4)
val (v1, s4) = s3.get()
val (v2, s5) = s4.get()
```

### IntStack: Custom Modifications
* **CUSTOM**

```scala
trait Doubling extends IntStack {
	abstract override def put(x: Int): IntStack = super.put(2 * x)
}

trait Incrementing extends IntStack {
	abstract override def put(x: Int): IntStack = super.put(x + 1)
}

trait Filtering extends IntStack {
	abstract override def put(x: Int): IntStack =
		if (x >= 0) super.put(x) else this
}
```

### IntStack: Stacking 
* **Stacking**

```scala
class DIFIntStack protected (xs: List[Int]) extends BasicIntStack(xs)
                                            with Doubling
                                            with Incrementing
                                            with Filtering {
	def this() = this(Nil)
	override def mkStack(xs: List[Int]): IntStack = 
		new DIFIntStack(xs)
}

val s0 = new DIFIntStack()
val s1 = s0.put(3)    // 8
val s2 = s1.put(-2)
val s3 = s2.put(4)    // 10
val (v1, s4) = s3.get()
val (v2, s5) = s4.get()
```

## Intersection Types
* Typing Rule

	```scala
	t1: T1   t: T2
	==============
	t1: T1 with T2
	```
	
* Example

	```scala
	trait A { val a: Int = 0 }
	trait B { val b: Int = 0 }
	class C extends A with B {
		override val a = 10
		override val b = 20
		val c = 30
	}
	
	val x = new C
	val y: A with B = x
	y.a // 10
	y.b // 20
	y.c // type error
	```

### Subtype Relation for "with"
The subtype relation for "with" is structural.

* Permutation

	```scala
	==================================================
	... with T1 with T2 ... <: ... with T2 with T1 ...
	```
	
* Width

	```scala
	=========================
	... with T ... <: ... ...
	```
	
* Depth

	```scala
	             T <: S
	================================
	... with T ... <: ... with S ...
	```

# Part 3 - Type-Oriented Programming

### OOP vs TOP
* OOP (Object-Oriented Programming)

	```scala
	class DF {
		val x: D1
		val y: D2
		def f(i: I1): O1
		def g(i: I2): O2
	}
	```
	
	* Associate functionality with data
* TOP (Type-Oriented Programming)

	```scala
	class D {
		val x: D1
		val y: D2
	}
	class F {
		def f(d: D)(i: I1): O1
		def g(d: D)(i: I2): O2
	}
	```
	
	* Associate functionality with types
	* Separate functionality from data
	* **TOP is particularly useful for writing specifications!**

## Specifications over Parameter Types

### Subtype Polymorphism
```scala
trait Ord {
	// this cmp that < 0 iff this < that
	// this cmp that > 0 iff this > that
	// this cmp that == 0 iff this == that
	def cmp(that: Ord): Int 
	
	def ===(that: Ord): Boolean = (this.cmp(that)) == 0
	def <(that: Ord): Boolean = (this cmp that) < 0
	def >(that: Ord): Boolean = (this cmp that) > 0
	def <=(that: Ord): Boolean = (this cmp that) <= 0
	def >=(that: Ord): Boolean = (this cmp that) >= 0
}

def max3(a: Ord, b: Ord, c: Ord): Ord = 
	if (a <= b) { if (b <= c) c else c }
	else { if (b <= c) c else b }
```

Problem: hard (almost impossible) to define `OrdInt <: Ord`

### Specification over Parameter Types
```scala
trait Ord[A] {
	def cmp(that: Ord[A]): Int
	def getValue: A
	
	def ===(that: Ord[A]): Boolean = (this.cmp(that)) == 0
	def <(that: Ord[A]): Boolean = (this cmp that) < 0
	def >(that: Ord[A]): Boolean = (this cmp that) > 0
	def <=(that: Ord[A]): Boolean = (this cmp that) <= 0
	def >=(that: Ord[A]): Boolean = (this cmp that) >= 0
}

def max3[A](a: Ord[A], b: Ord[A], c: Ord[A]): Ord[A] = 
	if (a <= b) { if (b <= c) c else b }
	else { if (a <= c) c else a }
	
class OInt(val getInt: Int) extends Ord[OInt] {
	def cmp(that: Ord[OInt]) = getInt.compare(that.getValue.getInt)
	def getValue = this
}

max3(new OInt(3), new OInt(2), new OInt(10)).getValue.getInt
```

### Further example: Ordered Bag
```scala
class Bag[A <: Ord[A]] protected (val toList: List[A]) {
	def this() = this(Nil)
	def add(x: A): Bag[A] = {
		def go(elmts: List[A]): List[A] = elmts match {
			case Nil => x :: Nil
			case e :: _ if (x < e) => x :: elmts
			case e :: _ if (x === e) => elmts
			case e :: rest => e :: go(rest)
		}
		new Bag(go(toList))
	}
}
val emp = new Bag[OInt]()
val b = emp.add(new OInt(3)).add(new OInt(2)).add(new OInt(10))
b.toList.map((x) => x.getInt)
```

**Works, but very awkward!**

## Type Classes
### Completely Separating Ord from Int
```scala
abstract class Ord[A] {
	def cmp(me: A, you: A): Int 
	
	def ===(me: A, you: A): Boolean = cmp(me, you) == 0
	def <(me: A, you: A): Boolean = cmp(me, you) < 0
	def >(me: A, you: A): Boolean = cmp(me, you) > 0
	def <=(me: A, you: A): Boolean = cmp(me, you) <= 0
	def >=(me: A, you: A): Boolean = cmp(me, you) >= 0
}

def max3[A](a: A, b: A, c: A)(implicit ord: Ord[A]) : A =
	if (ord.<=(a, b)) { if (ord.<=(b,c)) c else b }	else { if (ord.<=(a,c)) c else a }
	implicit val intOrd : Ord[Int] = new Ord[Int] { 
	def cmp(me: Int, you: Int) = me - you
}
	max3(3,2,10) // 10
```

### Syntatic Sugar: new A with B with C { ... }
```scala
new A with B with C{ 
	code}
is equivalent to{	class _tmp_ extends A with B with C {		code
	}	new _tmp_ 
}
```

### Implicit
* Implicit
	* An argument is given "implicitly"
	
	```scala
	def foo(s: String)(implicit t: String) = s + t 
	implicit val exclamation : String = "!!!!!!"	foo("Hi")	foo("Hi")("???") // can give it explicitly
	```

### Bag Example
```scala
class Bag[A] protected (val toList: List[A])(implicit proxy: Ord[A]) {
	def this()(implicit proxy: Ord[A]) = this(Nil)(proxy)
	
	def add(x: A): Bag[A] = {
		def go(elmts: List[A]): List[A] = elmts match {
			case Nil => x :: Nil
			case e :: _ if (proxy.<(x, e)) => x :: elmts
			case e :: _ if (proxy.===(x, e)) => elmts
			case e :: rest => e :: go(rest)
		}
		new Bag(go(toList))
	}
}
implicit val intOrd: Ord[Int] = new Ord[Int] { 
	def cmp(me: Int, you: Int) = me - you
}

(new Bag[Int]()).add(3).add(2).add(10).toList
```

### Bootstrapping Implicits
```scala
// lexicographic orderimplicit def tup2Ord[A, B](implicit ordA: Ord[A], ordB: Ord[B]) = { 
	new Ord[(A, B)] {		def cmp(me: (A, B), you: (A, B)) : Int = { 
			val c1 = ordA.cmp(me._1, you._1)			if (c1 != 0) c1			else { ordB.cmp(me._2, you._2) }		} 
	}}val b = new Bag[(Int,(Int,Int))]
b.add((3,(3,4))).add((3,(2,7))).add((4,(0,0))).toList
```

### With Different Orders

```scala
val inOrdRev: Ord[Int] = new Ord[Int] { 
	def cmp(me: Int, you: Int) = you - me
}
	
(new Bag[Int]()(IntOrdRev)).add(3).add(2).add(10).toList
```

## 5월 25일
`implicit` 을 너무 복잡하게 쓰면, 다른 사람이 쓰기 어려워서 안좋다.

## Type Classes With Multiple Parameters

### Iter
```scala
// trait Iter[A] {//     def getValue: Option[A]//     def getNext: Iter[A] 
// }abstract class Iter[I, A] {	def getValue(a: I): Option[A]	def getNext(a: I): I 
}
def sumElements[I](xs: I)(implicit proxy: Iter[I, Int]): Int = proxy.getValue(xs) match {	case None => 0	case Some(n) => n + sumElements(proxy.getNext(xs)) 
}
def printElements[I, A](xs: I)(implicit proxy: Iter[I,A]): Unit = proxy.getValue(xs) match {	case None =>	case Some(n) => {
		println(n)
		printElements(proxy.getNext(xs))
	}
}
```

### List 
```scala
implicit def listIter[A]: Iter[List[A], A] = new Iter[List[A], A] {	def getValue(a: List[A]) = a.headOption	def getNext(a: List[A]) = a.tail 
}val l = List(3,5,2,1)sumElements(l) //sumElements(l)(listIter[Int]) 
printElements(l) //printElements(l)(listIter[Int])
```

### Iterable
```scala
// trait Iterable[A] { 
//     def iter : Iter[A] 
// }
abstract class Iterable[R, I, A] { 
	def iter(a: R): I	def iterProxy: Iter[I, A]}
def sumElements2[R, I](xs: R)(implicit proxy: Iterable[R, I, Int]) =
	sumElements(proxy.iter(xs))(proxy.iterProxy) 
	//sumElements[I](proxy.iter(xs))(proxy.iterProxy)
def printElements2[R, I, A](xs: R)(implicit proxy: Iterable[R, I, A]) = 
	printElements(proxy.iter(xs))(proxy.iterProxy)	//printElements[I, A](proxy.iter(xs))(proxy.iterProxy)
```

### MyTree
```scala
sealed abstract class MyTree[A]case class Empty[A]() extends MyTree[A]case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]
implicit def treeIterable[A](implicit proxy: Iter[List[A], A]): Iterable[MyTree[A], List[A], A] = 
new Iterable[MyTree[A], List[A], A] {	def iter(a: MyTree[A]): List[A] = a match {		case Empty() => Nil		case Node(v, left, right) => v :: (iter(left) ++ iter(right)) 
	}    val iterProxy = proxy 
}
val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))sumElements2(t) //sumElements2(t)(treeIterable[Int]) 
printElements2(t) //printElements2(t)(treeIterable[Int])
```

### Iter being Iterable
```scala
implicit def iterIterable[I, A](implicit proxy: Iter[I, A]): Iterable[I, I, A] = new Iterable[I, I, A] { 	def iter(a: I) = a 
	val iterProxy = proxy}
// val l = List(3,5,2,1)sumElements2(l) //sumElements2(iterIterable(listIter[Int]))printElements2(l) //printElements2(iterIterable(listIter[Int]))
```

## Higher-kind Type Classes
### Iter
```scala
import scala.language.higherKinds
// trait Iter[I, A] {
//     def getValue(a: I): Option[A]
//     def getNext(a: I): I
// }
abstract class Iter[I[_]] {
	def getValue[A](a: I[A]): Option[A]
	def getNext[A](a: I[A]): I[A]
}
def sumElements[I[_]](xs: I[Int])(implicit itr: Iter[I]): Int = {
	itr.getValue(xs) match {
		case None => 0
		case Some(n) => n + sumElements(itr.getNext(xs))
	}
}
def printElements[I[_]], A](xs: I[A])(implicit itr: Iter[I]): Unit = {
	itr.getValue(xs) match {
		case None =>
		case Some(n) => { 
			println(n)
			printElements(itr.getNext(xs))
		}
	}
}
``` 

### List
```scala
implicit val listIter: Iter[List] = new Iter[List] {
	def getValue[A](a: List[A]) = a.headOption
	def getNext[A](a: List[A]) = a.tail
}

val l = List(3, 5, 2, 1)
sumElements(l) //sumElements(l)(listIter) 
printElements(l) //printElements(l)(listIter)
```

### Iterable
```scala
// trait Iterable[R, I, A] {//     def iter(a: R): I//     def iterProxy: Iter[I, A]// }
abstract class Iterable[R[_], I[_]] {
	def iter[A](a: R[A]): I[A]
	def iterProxy: Iter[I]
}

def sumElements2[R[_], I[_]](xs: R[Int])(implicit proxy: Iterable[R, I]) = 
	sumElemnts(proxy.iter(xs))(proxy.iterProxy)
	// sumElements[I](proxy.iter(xs))(proxy.iterProxy)

def printElements2[R[_], I[_], A](xs: R[A])(implicit proxy: Iterable[R, I]) =
	printElements(proxy.iter(xs))(proxy.iterProxy)
	// printElements[I, A](proxy.iter(xs))(proxy.iterProxy)
```

### MyTree
```scala
sealed abstract class MyTree[A]
case class Empty[A]() extends MyTree[A]
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

implicit val treeIterable: Iterable[MyTree, List] = new Iterable[MyTree, List] {
	def iter[A](a: MyTree[A]): List[A] = a match {
		case Empty() => Nil
		case Node(v, left, right) => v :: (iter(left) ++ iter(right)) 
	}
	val iterProxy = implicitly[Iter[List]]
}

val t: MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
sumElements2(t) //sumElements2(t)(treeIterable) 
printElements2(t) //printElements2(t)(treeIterable)
```

### Implicitly
* Definition
	
	```scala
	def implicitly[A](implicit proxy: A): A = proxy
	```
* Example

	```scala
	implicit val treeIterable: Iterable[MyTree, List] = new Iterable[MyTree, List] {
		def iter[A](a: MyTree[A]): List[A] = a match {
			case Empty() => Nil
			case Node(v, left, right) => v :: (iter(left) ++ iter(right)) 
		}
		val iterProxy = implicitly[Iter[List]]
	}
	
	implicit def treeIterable(implicit proxy: Iter[List]): Iterable[MyTree, List] = new Iterable[MyTree, List] {
		def iter[A](a: MyTree[A]): List[A] = a match {
			case Empty() => Nil
			case Node(v, left, right) => v :: (iter(left) ++ iter(right))
		}
		val iterProxy = proxy
	}
	```
	
### Iter being Iterable
```scala
implicit def iterIterable[I[_]](implicit proxy: Iter[I]): Iterable[I, I] = new Iterable[I, I] {
	def iter[A](a: I[A]) = a
	def iterProxy = proxy
}

// val l = List(3,5,2,1)sumElements2(l) //sumElements2(l)(iterIterable(listIter)) 
printElements2(l) //printElements2(l)(iterIterable(listIter))
```

### Example: Functor Specification
```scala
trait Functor[F[_]] {
	def map[A, B](f: A => B)(x: F[A]): F[B]
}

def compose[F[_], A, B, C](g: B => C)(f: A => B)(a: F[A])(implict proxy: Functor[F]): F[C] = 
	proxy.map(g)(proxy.map(f)(a))
```

### Example: Functor Implementation
```scala
sealed abstract class MyTree[A]
case class Empty[A]() extends MyTree[A]
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

implicit val ListFunctor: Functor[List] = new Functor[List] {
	def map[A, B](f: A => B)(x: List[A]) = x.map(f)
}
implicit val MyTreeFunctor: Functor[MyTree] = new Functor[MyTree] {
	def map[A, B](f: A => B)(x: MyTree[A]): MyTree[B] = x match {
		case Empty() => Empty()
		case Node(v, l, r) => Node(f(v), map(f)(l), map(f)(r))
	}
}

compose((x: Int) => x * x)((x: Int) => x + x)(List(1, 2, 3))

val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))compose((x: Int)=> x * x)((x: Int) => x + x)(t)
```

### Even Higher Kinds
```scala
// Iter: (* -> *) -> *
abstract class Iter[I[_]] {
	def getValue[A](a: I[A]): Option[A]
	def getNext[A](a: I[A]): I[A]
}

// Foo: ((* -> *) -> *) -> *
abstract class Foo[I[_[_]]] {
	def get: I[List]
}

def f(x: Foo[Iter]): Iter[List] = x.get
```

## Turning Type Classes into OO Classes
### Iter
```scala
// triat Iter[A] {
//     def getValue: Option[A]
//     def getNext: Iter[A]
// }

abstract class Iter[I, A] {
	def gatValue(i: I): Option[A]
	def getNext(i: I): I
}

def sumElements[I](xs: I)(implicit proxy: Iter[I, Int]): Int = proxy.getValue(xs) match {
	case None => 0
	case Some(n) => n + sumElements(proxy.getNext(xs)) 
}

def printElements[I, A](xs: I)(implicit proxy: Iter[I, A]): Unit = proxy.getValue(xs) match {
	case None =>
	case Some(n) => {
		println(n)
		printElements(proxy.getNext(xs))
	}
}
```

### List
```scala
implicit def listIter[A]: Iter[List[A], A] = new Iter[List[A], A] {
	def getValue(a: List[A]) = a.headOption
	def getNext(a: List[A]) = a.tail
}

val l = List(3, 5, 2, 1)

sumElements(I) // sumElements(I)(listIter[Int])
printElements(I) // printElements(I)(listIter[Int])
```

### How to return data satisfying a specification?
```scala
def incIter(max: Int): Iter[Int, Int] = new Iter[Int, Int] {
	def getValue(i: Int) = if (i <= max) Some(i) else None
	def getNext(i: Int) = i + 1
}

def getMyIter(isInc: Boolean): ? = {
	if (isInc) ? // want to return 0 with incIter(10)
	else ? // want to return List(3, 1, 4) with listIter
}

val i1 = getMyIter(true)
printElements(i1)
val i2 = getMyIter(false)
printElements(i2)
```

What can we do?

### Turing Type Classes into OO Classes
```scala
import scala.language.higherKinds
import scala.language.implicitConversions

abstract class Dyn2[S[_, _], A] {
	type Data
	val d: Data
	val i: S[Data, A]
}

object Dyn2 {
	implicit def apply[S[_, _], D, A](dd: D)(implicit ii: S[D, A]): 
	Dyn2[S, A] = new Dyn2[S, A] {
		type Data = D
		val d = dd
		val i = ii
	}
	implicit def methods[S[_, _], A](d: Dyn2[S, A]): S[d.Data, A] = d.i
}
```

### Test
```scala
def incIter(max: Int): Iter[Int, Int] = new Iter[Int, Int] {
	def getValue(i: Int) = if (i <= max) Some(i) else None
	def getNext(i: Int) = i + 1
}

def getMyIter(isInc: Boolean): Dyn2[Iter, Int] = {
	if (isInc) Dyn2(0)(incIter(10)) // Dyn2.apply(0)(incIter(10))
	else List(3, 1, 4) // Dyn2(List(3, 1, 4))(listIter[Int])
}

val i1 = getMyIter(true)
printElements(i1.d)(i1.i)
i1.getValue(i1.getNext(i1.d)) // i1.i.getValue(i1.i.getNext(i1.d))

val i2 = getMyIter(false)
printElements(i2.d)(i2.i)
i2.getValue(i2.getNext(i2.d))
```

### Can define "Dyn" for different kinds of specs
```scala
abstract class Dyn[S[_]] {
	type Data
	val d: Data
	val i: S[Data]
}

object Dyn {
	implicit def apply[D, S[_]](dd: D)(implicit ii: S[D]):
	Dyn[S] = new Dyn[S] {
		type Data = D
		val d = dd
		val i = ii
	}
	implicit def methods[S[_]](d: Dyn[S]): S[d.Data] = d.i
}
```

### Iterable 
```scala
// abstract class Iterable[R, I, A] {
//     def iter(a: R): I
//     def iterProxy: Iter[I, A]
// }

abstract class Iterable[R, A] {
	def iter(a: R): Dyn2[Iter, A]
}

def sumElements2[R](xs: R)(implicit proxy: Iterable[R, Int]) = {
	val cs = proxy.iter(xs)
	sumElements(cs.d)(cs.i)
}

def printElements2[R, A](xs: R)(implicit proxy: Iterable[R, A]) = {
	val cs = proxy.iter(xs)
	printElements(cs.d)(cs.i)
}
```

### MyTree
```scala
sealed abstract class MyTree[A]
case class Empty[A]() extends MyTree[A]
case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

implicit def treeIterable[A]: Iterable[MyTree[A], A] = new Iterable[MyTree[A], A] {
	def iter(a: MyTree[A]) = {
		def go(I: MyTree[A]: List[A] = I match {
			case Empty() => Nil
			case Node(v, left, right) => v :: (go(left) ++ go(right)) 
		}
		go(a) // Dyn2(go(a))(listIter[A])
	}
}

val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
sumElements2(t) //sumElements2(t)(treeIterable[Int]) 
printElements2(t) //printElements2(t)(treeIterable[Int])
```

### Iter being Iterable
```scala
implicit def iterIterable[I, A](implicit proxy: Iter[I, A]): Iterable[I, A] = new Iterable[I, A] {
	def iter(a: I) = a // Dyn2(a)(proxy)
}

// val l = List(3, 5, 2, 1)
sumElements2(l)
printElements2(l)
```

## Stacking with Type Classes
### IntStack Spec & Modifying Traits
```scala 
trait IntStack[A] {
	def empty: A
	def get(s: A): (Int, A)
	def put(s: A)(x: Int): A
}

trait Doubling[A] extedns IntStack[A] {
	abstract override def put(s: A)(x: Int): A = super.put(s)(2 * x)
}

trait Incrementing[A] extends IntStack[A] {
	abstract override def put(s: A)(x: Int): A = super.put(s)(x + 1)
}

trait Filtering[A] extends IntStack[A] {
	abstract override def put(s: A)(x: Int): A = 
		if (x >= 0) super.put(s)(x) else s
}
```

### Implementation using List
```scala
trait ListStackImpl extends IntStack[List[Int]] {
	val empty = List()
	def get(s: List[Int]) = (s.head, s.tail)
	def put(s: List[Int])(x: Int) = x :: s
}

val stkDIF: IntStack[List[Int]] = new ListStackImpl
                                    with Doubling[List[Int]]
                                    with Incrementing[List[Int]]
                                    with Filtering[List[Int]]
											
val s0 = stkDIF.emptyval s1 = stkDIF.put(s0)(3) 
val s2 = stkDIF.put(s1)(-2) 
val s3 = stkDIF.put(s2)(4) 
val (v1,s4) = stkDIF.get(s3) 
val (v2,s5) = stkDIF.get(s4)
```

### Implementation using SortedIntStack
```scala
class SortedStack protected (xs: List[Int]) {
	override val toString = "Stack: " + xs.toString
	def this() = this(Nil)
	def get = (xs.head, new SortedStack(xs.tail))
	def put(x: Int) = {
		def go(l: List[Int]): List[Int] = l match {
			case Nil => x :: Nil
			case hd :: tl => if (x <= hd) x :: l else hd :: go(tl)
		}
		new SortedStack(go(xs))
	}
}

trait SortedStackImpl extends IntStack[SortedStack] {
	val empty = new SortedStack()
	def get(s: SortedStack) = s.get
	def put(s: SortedStack)(x: Int) = s.put(x)
}
```

### Implementation using SortedIntStack: Test
```scala
val sortedDIF : IntStack[SortedStack] = new SortedStackImpl                                        with Doubling[SortedStack]
                                        with Incrementing[SortedStack] 
                                        with Filtering[SortedStack]val s0 = sortedDIF.emptyval s1 = sortedDIF.put(s0)(3)
val s2 = sortedDIF.put(s1)(-2)
val s3 = sortedDIF.put(s2)(4) 
val (v1,s4) = sortedDIF.get(s3) 
val (v2,s5) = sortedDIF.get(s4)
```

### Separating methods from SortedIntStack
```scala
class SortedStack protected (private val xs: List[Int]) {
	override val toString = "Stack: " + xs.toString
	def this() = this(Nil)
}
object SortedStack {
	trait Impl extends IntStack[SortedStack] {
		val empty: SortedStack = new SortedStack
		def get(s: SortedStack) = (s.xs.head, new SortedStack(s.xs.tail))
		
		def put(s: SortedStack)(x: Int) = {
			def go(l: List[Int]): List[Int] = l match {
				case Nil => x :: Nil
				case hd :: tl => if (x <= hd) x :: l else hd :: go(tl)
			}
			new SortedStack(go(s.xs))
		}
	}
} 
```

### Separating methods from SortedIntStack: Test
```scala
val sortedDIF: IntStack[SortedStack] = new SortedStack.Impl
                                       with Doubling[SortedStack]
                                       with Incrementing[SortedStack]
                                       with Filtering[SortedStack]

val s0 = sortedDIF.emptyval s1 = sortedDIF.put(s0)(3)
val s2 = sortedDIF.put(s1)(-2)
val s3 = sortedDIF.put(s2)(4) 
val (v1,s4) = sortedDIF.get(s3) 
val (v2,s5) = sortedDIF.get(s4)
```

# PART 4 Imperative Programming with Memory Updates
### Mutable Variables
* Mutable Variables
	* Use `var` instead of `val` and `def `
	* We can update tha value stored in a variable.

	```scala
	class Main(i: Int) {
		var a = i
	}
	
	val m = new Main(10)
	m.a
	m.a = 20
	m.a += 5
	m.a 
	```
	
### While loop
* While loop
	* Syntax: `while (cond) body`  
	Executes *body* while *cond* holds.
	* It is equivalnet to:

	```scala
	def myWhile(cond: =>Boolean)(body: =>Unit): Unit = 
		if (cond) { body; myWhile(c)(body) } else ()
	```

* Example

	```scala
	var i = 0
	var sum = 0
	while (i <= 100) {
		sum += i
		i += 2
	}
	sum
	```
	
### For loop
* For loop
	* Syntax: `for (i <- collection) body`  
	Executes *body* for each `i` in *collection*.
	* It is equivalent to:
	
	```scala
	def myFor[A](xs: Traversable[A])(f: A => Unit): Unit = xs.foreach(f)
	``` 

* Example

	```scala
	var sum = 0
	for (i <- 0 to 100 by 2) {
		sum += i
	}
	sum 
	```