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
* Name Binding (= Programming)은 expression과 이름을 binding 시키는 것이다. Named은 just idenitifier. Functional Programming에서 name이라 하면 value를 말하는 것, name을 evaluation하면 언제나 같은 결과를 준다. Name never change.
	
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
def one(x: Int, y: =>Int) = 1
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

and(false, loop==1)
~ if (false) loop==1 else false
~ false

and(true, loop==1)
~ if (true) loop==1 else false
~ loop==1 ~ loop==1 ...
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
	val a = b    // raise error. a is value, the compiler wants to evalute b. 
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
	* Can write two definitions/expressions in a single line using `;`
	* Can write one definition/expression in two lines using `()`, but can omit `()` when clear

```scala
// OK
val r = {
	val t = 10; val s = square(5); t +
	a }
// Not OK
val r = {
	val t = 10; val s = sqaure(5); t +
	s }
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

f1(true, {println("ok"); 100+100+100+100}) // ok 0f1(false, {println("ok"); 100+100+100+100}) // ok 64000000

def f2(c: Boolean, iv: =>Int): Int = {
	if (c) 0
	else iv * iv * iv
}

f2(true, {println("ok"); 100+100+100+100}) // 0f2(false, {println("ok"); 100+100+100+100}) // ok ok ok 64000000

def f3(c: Boolean, i: =>Int): Int = {
	lazy val iv = i
	if (c) 0
	else iv * iv * iv
}

f3(true, {println("ok"); 100+100+100+100}) // 0f3(false, {println("ok"); 100+100+100+100}) // ok 64000000
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
def sum(f: Int=>Int, n: Int): Int =	if (n <= 0) 0 else f(n) + sum(f, n-1)
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
	(x: Int => x*x)(100) // 10000
	```  
	
```scala
def sumLinear(n: Int) = sum((x:Int)=>x, n)def sumSquare(n: Int) = sum((x:Int)=>x*x, n)def sumCubes(n: Int) = sum((x:Int)=>x*x*x, n)
```Or simply

```scaladef sumLinear(n: Int) = sum((x)=>x, n)def sumSquare(n: Int) = sum((x)=>x*x, n)def sumCubes(n: Int) = sum((x)=>x*x*x, n)
```

### Exercise
```scala
def sum(f: Int=>Int, a: Int, b: Int): Int = 	if (a <= b) f(a) + sum(f, a+1, b) else 0def product(f: Int=>Int, a: Int, b: Int): Int = 
	if (a <= b) f(a) * product(f, a+1, b) else 1
```
DRY (Do not Repeat Yourself) using a higher-order function, called "mapreduce"

```scala
def mapReduce(map: Int=>Int, reduce: (Int, Int)=>Int, a: Int, b: Int, init: Int): Int = 
	if (a <= b) reduce(map(a), mapReduce(map, reduce, a+1, b, init)
	else init
/*	call-by-name map and reduce function
def mapReduce(map: =>(Int=>Int), reduce: =>((Int, Int)=>Int), a: Int, b: Int, init: Int): Int = 
	if (a <= b) reduce(map(a), mapReduce(map, reduce, a+1, b, init)
	else init
*/

def sum(f: Int=>Int, a: Int, b: Int): Int =
	mapReduce(f, _+_, a, b, 0)

def product(f: Int=>Int, a: Int, b: Int): Int = 
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
`g` is a parameterized expression. But the `f` is value. So what't the value of function? 또한 `def`는 자신이 정의 됐던 environment에서 evaluation 되야 한다. `g`를 `f`에 그냥 copy 하는 것은 문제가 생긴다. (만약 `val f`가 아니라 `def f`라면 그런 문제가 안생기나? 애초에 closure 이전의 정의는 `def` 정의 안에 `val`이 들어가는 것이 가능하긴 하나?)

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
2: def f(x: =>Int) = t + x // x is treated as x()
3: val r = {
4:     val t = 10
5:     f(t * t) }          // t*t is treated as () => t*t
```

* Evaluation with Closures

```scala
[],1 
~ [t=0],2 
~ [...,f=(x:=>Int)t+x],3 
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
def sum(f: Int => Int): (Int, Int)=>Int = 
	(a, b) => if (a <= b) f(a) + sum(f)(a+1, b) else 0
```

Or more simply:

```scala
def sum(f: Int => Int)(a: Int, b: Int): Int =
	if (a <= b) f(a) + sum(f)(a+1, b) else 0
```
If I use `sum(square)`, is it a currying? And then, I can use like `sum(_: Int => Int)(1, 10)`? Yes!

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
f1(1,2,3)  // 8f2(1,2,3)  // 8f3(1,2)(3) // 8
```

### Exercise
Curry the `mapReduce` functions.

```scala
def mapReduce(reduce: (Int, Int) => Int, init: Int)(map: Int => Int(a: Int, b: Int): Int = {
	if (a <= b) mapReduce(reduce, init)(map)(a+1, b)
	else init
}

// need to make a closure since mapReduce is param. code.
def sum = mapReduce(_+_, 0) _

// val is better than def. Think about why.
val product = mapReduce(_*_, 1) _
```

`val`이 더 나은 이유는 `def`로 name binding 하면 함수를 call 할 때 마다 currying 다시 하기 때문이 아닐까. `val` binding 하면, Function value로 return 되기 때문에 선언시에만 currying이 이뤄진다.