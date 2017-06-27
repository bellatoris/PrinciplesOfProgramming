package pp201701.mid6

/*
 *  Midterm of Principles of Programming 2017
 *  
 *  General Instructions:
 *  - './compile.sh' compiles your code
 *  - './run.sh' runs a simple test case given in Test.scala
 *  - Before submission, check your code by running './run.sh'. Uncompilable code will get 0 points.
 *  - Do not use imperative features. (ex. for, while, var, etc)
 *  - Compress the 'src' folder with '.zip' format for submission.
 */

object Main {
  /*
   Problem 6: Boolean expression

   Define an abstract data type (ADT) for boolean expressions.
   Your ADT should express:
   - True
   - False
   - 5 variables ( A, B, C, D, E )
   - AND expression
   - OR expression
   - NOT expression
   */

  sealed abstract class Bexp
  case class True() extends Bexp
  case class False() extends Bexp
  case class AND(left: Bexp, right: Bexp) extends Bexp
  case class OR(left: Bexp, right: Bexp) extends Bexp
  case class NOT(exp: Bexp) extends Bexp
  case class A() extends Bexp
  case class B() extends Bexp
  case class C() extends Bexp
  case class D() extends Bexp
  case class E() extends Bexp

  /* Complete Bexp here */

  /*
   Define values and functions for constructing Bexp values.
   */
  val mkTrue : Bexp = True()

  val mkFalse : Bexp = False()

  // If x is not the given variables, make it False.
  def mkVar(x:String) : Bexp = x match {
    case "A" => A()
    case "B" => B()
    case "C" => C()
    case "D" => D()
    case "E" => E()
    case _ => mkFalse
  }

  def mkAnd(exp1:Bexp, exp2 :Bexp) : Bexp = exp1 match {
    case True() => exp2
    case False() => mkFalse
    case _ => AND(exp1, exp2)
  }

  def mkOr(exp1:Bexp, exp2: Bexp) : Bexp =  exp1 match {
    case True() => mkTrue
    case False() => exp2
    case _ => OR(exp1, exp2)
  }

  def mkNot(exp1:Bexp) : Bexp = exp1 match {
    case True() => mkFalse
    case False() => mkTrue
    case _ => NOT(exp1)
  }

  /*
   Implement a function that evaluates the given Bexp under the given environment[env].
   The environment assigns values to variables (A, B, C, D, E) during the evaluation.
   */
  def eval(exp:Bexp, env:(Boolean, Boolean, Boolean, Boolean, Boolean)) : Boolean = {
    def nested(exp: Bexp): Boolean = exp match {
      case A() => env._1
      case B() => env._2
      case C() => env._3
      case D() => env._4
      case E() => env._5
      case AND(left, right) => nested(left) && nested(right)
      case OR(left, right) => nested(left) || nested(right)
      case NOT(exp) => !nested(exp)
      case True() => true
      case False() => false
    }
    nested(exp)
  }

}
