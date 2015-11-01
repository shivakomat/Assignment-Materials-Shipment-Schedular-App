package com.garner.tool.conversions

/** Handles all weight unit conversions
 *  calculated with imperial tons units 
 *  */
sealed class WeightUnitConversion {
  
  def lbsToton(value:Double):Double= if(value >=0) (value * 0.000446429) else 0.00
  
  
  def kgToton(value:Double):Double= if(value >= 0) (value * 0.000984207) else 0.00 
  

  def tonTokg(value:Double):Double= if(value >= 0) (value * 907.185) else 0.00
  
  
  def lbsTokg(value:Double):Double= if(value >=0) (value * 0.453592) else 0.00
   
}


object WeightUnitConversion{

  def kgToton(value:Double):Double=  new WeightUnitConversion().kgToton(value)
  
  
  def lbsToton(value:Double):Double= new WeightUnitConversion().lbsToton(value)

  
  def toTons(value:Double,unit:String):Double={
    unit.toLowerCase match {
	    case "lbs" => lbsToton(value)
	    case "kg"  => kgToton(value)
	    case "ton" => value
	    case  _    => println("log: "+ unit + "-> not supported"); 0.0
	  }
  }
}