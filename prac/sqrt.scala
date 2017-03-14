def isGoodEnough(guess: Double, x: Double) = 
  ((guess * guess - x).abs/x < 0.001)
          
def improve(guess: Double, x: Double) = 
  (guess + x/guess)/2.0
              
def sqrtIter(guess: Double, x: Double): Double = 
  if (isGoodEnough(guess, x)) guess
  else sqrtIter(improve(guess, x), x)
                        
def sqrt(x: Double) =
  sqrtIter(1, x)
                            
println(sqrt(17))
