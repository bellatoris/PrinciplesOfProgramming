val t = 0
def square(x: Int) = t + x * x
val x = square(5)
val r = {
  val t = 10
  val s = square(5)
  println(s)
  t + x
}
println(r)
val y = t + r
