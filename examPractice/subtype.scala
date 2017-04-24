object Main {
  def main(args: Array[String]): Unit = {
    object tom {
      val name = "Tom"
      val home = "dasfjsdfkl"
    }
  
    object bob {
      val name = "Bob"
      val mobile = " dsfasdf"
    }

    def greeting(r: { val name: String }) = "Hi " + r. name
    println(greeting(tom))
    println(greeting(bob))

    val a: Int = 3
    val b: Any = a
    def f(a: Nothing): Int = a

    def foo(s: { val a: Int; val b: Int}): { val x: Int; val y: Int } = {
      new {
        val x = s.b
        val y = s.a
      }
    }
    // foo <: gee
    val gee: { val a: Int; val b: Int; val c: Int } => { val x: Int } = foo _

    type Foo = { val a: Int; val b: Int } => { val x: Int; val y: Int }
    type Gee = { val a: Int; val b: Int; val c: Int } => { val x: Int }

    def zoo(gee: Gee) = gee
    println(zoo(gee)(new { val a = 3; val b = 4; val c = 5 }).x)
    zoo(foo _)
  }
}
