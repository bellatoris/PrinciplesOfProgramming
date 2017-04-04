object Main {
  def main(args: Array[String]): Unit = {

    object tom {
      val name = "Tom"
      val home = "02-880-1234"
    }

    object bob {
      val name = "Bob"
      val mobile = "010-1111-2222"
    }

    type NameHome = { val name: String; val home: String }
    type NameMobile = { val name: String; val mobile: String }
    type Name = { val name: String }

    def greeting(r: Name) = "Hi " + r.name + ", How are you?"
    println(greeting(tom), greeting(bob))
  }
}
