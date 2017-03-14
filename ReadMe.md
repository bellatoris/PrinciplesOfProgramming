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

## Rewriting for blocks
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
[f=(x)t+g(x),g=(x)x*x],1 
~ [...,t=0],2
~ [...],3 
~ [...],4 
~ [...,x=25],5 
~ [...]:[],6 
~ [...]:[t=10],7~ [...]:[...,s=25],8 
~ [...,r=35],9 
~ [...,y=35],10
4: [f=..., g=..., t=0]:[x=5], t+g(x) ~...~ [...]:[...], 25
7: [f=..., g=..., t=0]:[x=5], t+g(x) ~...~ [...]:[...], 25
```