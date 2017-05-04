object Main {
  def main(args: Array[String]): Unit = {
    class A {
      println("I'm A")
      def hello = println("hello A")
    }

    class B extends A {
      println("I'm B")
      override def hello = println("hello B")
    }

    trait E {
      println("I'm E")
      def hi = println("hi E")
    }

    trait C extends A with E {
      println("I'm C")
      override def hi = println("hi C")
      override def hello = println("hello C")
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
    d.hello
  }
}
