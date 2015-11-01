package com.garner.tool.schedule.materials

import java.io.File


/** 
 *  Ice Road Shipments Scheduler
 *  
 *  Author : Shivakanth Komatreddy
 *  */
trait ShipmentSchedular {
   
   /** returns the generates the schedule and writes it to the 
    *  output file specified by the user at the creation of the object*/
   def getSchedule(data:java.util.Date):Boolean
 
}


/** options to use the ice road scheduler */
object ShipmentSchedular{
  def apply(materialsFile:File,ouputFile:File):ShipmentSchedular={
    new IceRoadShipmentSchedular(materialsFile,ouputFile)
  }
  
  def apply(materialsFile:String,outputFile:String):ShipmentSchedular={
    new IceRoadShipmentSchedular(new File(materialsFile),new File(outputFile))
  }
}