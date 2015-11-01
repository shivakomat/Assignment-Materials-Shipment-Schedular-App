package com.garner.tool.schedule.materials

import com.garner.tool.parser._
import com.garner.tool.conversions._
import java.io.File
import scala.util.Try
import java.util.Date

sealed case class Shipment(id:Int,weight:Double,product:String,priority:Int)

sealed class IceRoadShipmentSchedular(shipmentsfile:File,outputfile:File) extends ShipmentSchedular{
      
    private val leastPriority = 4
    private val maxShipmentsFirst15Days = 2520 //((7 days * 24 hrs) * 15 days)
    private val maxTonsForEachLoadFirst15Days:Double = 15.0 // max load size for first 15 days   
    private val outputFileHeader= List("day","hour","slot","id")    
    private var shipmentCounter = 0  
    private var dayCounter = 0 
    private var monthCounter = 0 
    private var hourCounter = 0  
    private var slot = 1 
    private var wroteHeader = false
    
    
    
    def getSchedule(startdate:Date):Boolean={
      reset
      if(outputfile.exists) outputfile.delete
      shipmentsfile.exists match {
        case true  =>
          val remainingLoads = getFirstFifteenDaysLoads(maxTonsForEachLoadFirst15Days,startdate)      
          dayCounter = 15;hourCounter=0;
          getAfterFifteenDaysLoads(maxTonsForEachLoadFirst15Days,startdate)
          addRemainingLoads(remainingLoads,startdate)
          println("log: completed building schedule ->\nplesase check output file ("+outputfile.getAbsolutePath+")")
          true
        case false =>
          println("log: specified shipments file doesnt exist ")
          false
      }      
    }
    
    
    
    private def tryToInt( s: String ) = Try(s.toInt).toOption
    
    
    
    private def reset = shipmentCounter = 0;dayCounter = 0;monthCounter = 0;hourCounter = 0;slot = 1;wroteHeader=false;
    
    
    
    private def addRemainingLoads(data:Seq[List[Shipment]],date:Date)= generateShipmentSchedule(outputfile,data,date)
    
    
    
    private def getAfterFifteenDaysLoads(min:Double,date:Date)={      
      val data = (1 to 4 by 1).map(p => readData(p,CSVFileReader(shipmentsfile,true))
    		  				  .filter(_.weight > min).sortWith(_.weight > _.weight))      			 
      generateShipmentSchedule(outputfile,data,date)
    }
    
    

    private def getFirstFifteenDaysLoads(max:Double,date:Date):Seq[List[Shipment]]={
      val data = (1 to 4 by 1).map(p => readData(p,CSVFileReader(shipmentsfile,true))
    		  				 .filter(e => (e.weight <= max && e.weight > 0)).sortWith(_.weight < _.weight))
      data.size match {
        case i if(i>2520)      => 
          generateShipmentSchedule(outputfile,data.take(maxShipmentsFirst15Days),date)
          data.drop(maxShipmentsFirst15Days)          
        case i if(i>0&&i<2520) =>
          generateShipmentSchedule(outputfile,data,date);Seq()
        case _ => 
          println("log: no shipments for the first 15 days");Seq()
      }                 
    }
    
    
    
    private def generateShipmentSchedule(file:File,data:Seq[List[Shipment]],date:Date)={
      val rowwriter = CSVFileWriter(file,true)
      try { 
        if(wroteHeader==false) rowwriter.writeHeader(outputFileHeader);wroteHeader=true;
        data.foreach(dset => 
          dset.foreach(m => {
            val day = List((date.getYear + 1900),(date.getMonth+1)+monthCounter,(date.getDay +1)+dayCounter).mkString("-")
            val hour =((date.getHours) + hourCounter).toString
            rowwriter.write(List(day,hour,slot.toString,m.id.toString))
            slot += 1;shipmentCounter += 1
            if(shipmentCounter == 7) { hourCounter += 1; shipmentCounter = 0;}
            if(hourCounter == 24) { dayCounter += 1; hourCounter = 0;}
            if(dayCounter == 30) {dayCounter = 0; monthCounter += 1;}
          }))}
      finally {
        rowwriter.close}
    }
    
    
    
    private def getShipment(row:List[String]):Shipment={
      val priority = tryToInt(row(4).trim).getOrElse(leastPriority)
      val weight  = WeightUnitConversion.toTons(row(2).toDouble,row(3))
      val id = row(0).toInt
      val product = row(1)
      Shipment(id,weight,product,priority)
    }

    

	private def readData(priority:Int,rowsource:Reader[List[String]],rows:List[Shipment] = List()):List[Shipment]={	 
	 try{ 	  
	  rowsource.hasNext match {	  
	    case true =>
	      val row = rowsource.next
	      val shipment = getShipment(row)		      
	      if(shipment.priority == priority){ 
	        readData(priority,rowsource,rows ++ List(shipment))
	      }
	      else {
	        readData(priority,rowsource,rows)	      
	      }
	    case false =>
	      rows
	 }}
	 finally{ 
	  rowsource.close}
	}
	
	
}