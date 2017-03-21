def sqrt(x: Double) = {
  def isGoodEnough(guess: Double) = 
    ((guess * guess - x).abs/x < 0.001)
          
  def improve(guess: Double) = 
    (guess + x/guess)/2
                
  def sqrtIter(guess: Double): Double = 
    if (isGoodEnough(guess)) guess
    else sqrtIter(improve(guess))

    sqrtIter(1)
}

println(sqrt(2))
