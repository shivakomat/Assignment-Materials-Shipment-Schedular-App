package com.garner.tool.main

import com.garner.tool.schedule.materials.IceRoadShipmentSchedular
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import com.garner.tool.schedule.materials.ShipmentSchedular


/** How to use the app */
object MainApp extends App{  
  val materialFile = new File("./testData/materials.csv")
  val outputFile = new File("./testData/outputFile.csv")    
  val sdf = new SimpleDateFormat("dd-M-yyyy hh:mm")
  val date = sdf.parse("01-02-2015 08:00:00")
  val isCompleted = ShipmentSchedular(materialFile,outputFile).getSchedule(date)
}
