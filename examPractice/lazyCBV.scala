object Main {
  def main(args: Array[String]): Unit = {

    println("Call by value")
    def f1(c: Boolean, iv: Int): Int = {
      if (c) 0
      else iv * iv * iv 
    }

    println(f1(true, { println("ok"); 100+100+100+100 }))
    println(f1(false, { println("ok"); 100+100+100+100 }))
    
    println("Call by name")
    def f2(c: Boolean, iv: => Int): Int = {
      if (c) 0
      else iv * iv * iv 
    }

    println(f2(true, { println("ok"); 100+100+100+100 }))
    println(f2(false, { println("ok"); 100+100+100+100 }))

    println("Lazy call by value")
    def f3(c: Boolean, i: => Int): Int = {
      lazy val iv = i
      if (c) 0
      else iv * iv * iv 
    }

    println(f3(true, { println("ok"); 100+100+100+100 }))
    println(f3(false, { println("ok"); 100+100+100+100 }))
  }
}
