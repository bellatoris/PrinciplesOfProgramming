package pp201701.mid3

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
   Problem 3: Structural Sub Type
   Related to HW3-3

   Complete the definition of MyClass
   DO NOT USE "Any" anywhere in your code.
   */

  class MyClass[A,B,C,D,E,F]() {
    type Ty1 = {
      val apply: { val func: { val a: A } => { val b: B } ; val c: C } => { val b: B ; val d: D }
      val a: A
      val b: B
    }

    type Ty2 = {
      val apply: { val func: { val b: B } => { val a: A } ; val e: E } => { val b: B ; val f: F }
      val a: A
      val c: C
    }

    /*
     Find a suitable common supertype of Ty1 and Ty2,
     and replace "Any" with it.
     */
    type CommonTy = {
      val apply: { val func: { } => { val a: A; val b: B };  val c: C; val e: E } => { val b: B }
      val a: A
    }

    /*
     Construct a proper value for the argument of x.apply, and then apply it.
     */
    def apply(x: CommonTy, _a: A, _b: B, _c: C, _d: D, _e: E, _f: F) =
	    x.apply(new {
          val func = (input: { }) => new { val a = _a; val b = _b }
          val c = _c
          val e = _e
        })
  }
}
