package pp201701.fin1
import pp201701.fin1.Data.DataBundle._

/*
 * ** The submitted code should be runnable. Before upload, you MUST check it
      whether Test.scala runs successfully. Otherwise you may get 0 points for
      all problems.

 * ** Compress the 'src' folder only. Don't put .sh files in the zip file.
      You can put the .iml file together, if you feel difficulty for some reasons.

 * ** Use only the features taught in class. Especially, don't use imperative
      features such as while, for, return, and so on. You may get deduction
      in your points.

 * ** Do not use equivalent built-in features in Scala. The core logic should be
      implemented by you.
 */

object Main {
  // Problem 1
  /*
   Implement 'BiIterable' list.

   It should support bi-directional iteration, so you can freely move
   the iterator forward(getNext) or backward(getPrev) at any moment.

   At each end, your BiIter cannot go further, but should be able to go back
   to the reversed direction.

   For example:
   When you are on the first element,
   - getPrev.getValue gives None.
   - getPrev.getPrev.getValue also gives None.
   - getPrev.getPrev.getPrev.getPrev.getNext.getValue gives Some(first-element)

   Similarly, when you are on the last element,
   - getNext.getValue gives None.
   - getNext.getNext.getValue also gives None.
   - getNext.getNext.getNext.getNext.getPrev.getValue gives Some(last-element)

   You may create your own classes for this exercise.
   */

  class ListBiIter[A](val head: List[A], 
                      val value: Option[A],
                      val tail: List[A]) extends BiIter[A] {
    def getValue: Option[A] = value
    
    def getNext: ListBiIter[A] = tail match {
      case hd :: tl => value match {
        case None => new ListBiIter(head, Some(hd), tl)
        case Some(n) => new ListBiIter(n :: head, Some(hd), tl)
      }
      case Nil => value match {
        case None => this
        case Some(n) => new ListBiIter(n :: head, None, Nil)
      }
    }

    def getPrev: ListBiIter[A] = head match {
      case hd :: tl => value match {
        case None => new ListBiIter(tl, Some(hd), tail)
        case Some(n) => new ListBiIter(tl, Some(hd), n :: tail)
      }
      case Nil => value match {
        case None => this
        case Some(n) => new ListBiIter(Nil, None, n :: tail)
      }
    }
  }

  class BiIterableList[A](val data: List[A]) extends BiIterable[A] {
    def biIter: ListBiIter[A] = data match {
      case Nil => new ListBiIter(Nil, None, Nil)
      case hd :: tl => new ListBiIter(Nil, Some(hd), tl)
    }
  }

}
