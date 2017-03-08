# Principles of Programming 
* Lecture & Practice Session
	* [LectureHompage](https://github.com/snu-sf-class/pp201701)

Imperative Programming:

* Sequence of read/write operations

Functional Programming:

* More declarative, more high-level description
* More reliability, and safe


## Type-Oriented Programming
스칼라는 directly 하게 type class를 다루지는 않는다. 오직 Object-oriendted 만을 지원한다. 그러나 이를 사용해서 type class비슷하게 할수 있다. not so bad.
Object-oriendted의 경우 data와 method라면, type class에서는 type과 method이다. 아래 코드에서 `Point`는 type이다. `plus`이는 오직 두개의 type을 받아들인다. oop에서 method는 직접 자신의 data에 접근하지만, type class 에서는 그러지 않는다. oop에서는 객체 자체가 method를 가지고 있지만, type class에서는 type자체가 method를 가지고 있다.

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

## Functional Programming with Function Applications

### Values, Expression, Names
* Type은 value들의 집합이다.  
* Expression은 value와 name, primitive operation의 조합.
* Name Binding (= Programming)은 expression과 이름을 binding 시키는 것이다.
	
	```scala
	def a = 1 + (2 + 3)
	def b = 3 + a * 4
	```
	
### Evaluation
Evaluation은 expression을 value로 reducing하는 것이다.  
name이나 operator를 밖에서 부터 안쪽으로 (?) 취한다. name의 경우 binding된 expression으로 바꾸고 expression을 evaluation한다.  
operator의 경우 operands들을 evaluation하고 (left to right) operands에 operator를 적용한다.