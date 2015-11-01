package com.garner.tool.testcases


import org.junit.Test
import org.scalatest.junit.JUnitRunner
import org.scalatest.Assertions._
import org.scalatest.FlatSpec
import com.garner.tool.schedule.materials.IceRoadShipmentSchedular
import java.text.SimpleDateFormat
import java.io.File
import com.garner.tool.schedule.materials.Shipment
import com.garner.tool.parser.CSVFileReader
import com.garner.tool.parser.CSVFileWriter

/**End to End automated test cases*/
class TestShipmentSchedularApp extends FlatSpec{
  
  val testDir = "../testData/"
  val header = List("id","description","weight","unit","priority")

  "Test first15 days with priority" should "return shipments schedule with each load <= 15 tons ordered by priority (1 to 3 & 0)" in {

    val shipments = Seq(List("1","cement","13","ton","1"),
                        List("4","bricks","10","ton","1"),
                        List("5","fuel","8","ton","2"),
    				    List("27","drill bits","15","ton"," "),
    				    List("29","steel bars","9","ton","3"))
    				    
    val expected  = List(List("2015-2-1","8","1","4"),
    		             List("2015-2-1","8","2","1"),
    					 List("2015-2-1","8","3","5"),
    					 List("2015-2-1","8","4","29"),
    					 List("2015-2-1","8","5","27"))
    
    val shipmentsfile = new File(testDir+"testcasefirst15.csv")
    
    if(shipmentsfile.exists) shipmentsfile.delete
    
    val rowwriter = CSVFileWriter(shipmentsfile,true)
    
    rowwriter.writeHeader(header)
    
    shipments.foreach(l => rowwriter.write(l))
    
    rowwriter.close
    
    assert (true === shipmentsfile.exists)

    val outputFile = new File(testDir+"testcasefirst15_Results.csv")
    
    val sdf = new SimpleDateFormat("dd-M-yyyy hh:mm")
    val date = sdf.parse("01-02-2015 08:00:00")
    val completed = new IceRoadShipmentSchedular(shipmentsfile,outputFile).getSchedule(date)
   
    assert (true === completed)
  
    val rowsource = CSVFileReader(outputFile,true)
  
    val calcualtedShipments = rowsource.map(row => row)
    
    val output = calcualtedShipments.toList
    
    assert (expected === output)
    
    if(outputFile.exists) outputFile.delete
    if(shipmentsfile.exists) shipmentsfile.delete
            
  }
  
  
  "Test after15 days with priority" should "return shipments schedule with each load > 15 tons ordered by priority (1 to 3 & 0)" in {
    
    val shipments = Seq(List("1","cement","16","ton","1"),
                        List("5","fuel","20","ton","2"),
                        List("4","bricks","35","ton","1"),
                        List("50","plastic","15.5","ton","2"),
    				    List("27","drill bits","17","ton"," "),
    				    List("29","steel bars","22","ton","3"),
    				    List("40","empty barrels","30","ton","3"),
    				    List("13","cranes","27","ton"," "))
    				    
    val expected  = List(List("2015-2-16","8","1","4"),
    		             List("2015-2-16","8","2","1"),
    					 List("2015-2-16","8","3","5"),
    					 List("2015-2-16","8","4","50"),
    					 List("2015-2-16","8","5","40"),
    					 List("2015-2-16","8","6","29"),
    					 List("2015-2-16","8","7","13"),
    					 List("2015-2-16","9","8","27"))
   
    
    val shipmentsfile = new File(testDir+"testcaseAfter15.csv")
    
    if(shipmentsfile.exists) shipmentsfile.delete
    
    val rowwriter = CSVFileWriter(shipmentsfile,true)
    
    rowwriter.writeHeader(header)
    
    shipments.foreach(l => rowwriter.write(l))
    
    rowwriter.close
    
    assert (true === shipmentsfile.exists)

    val outputFile = new File(testDir+"testcaseAfter15_Results.csv")
    
    val sdf = new SimpleDateFormat("dd-M-yyyy hh:mm")
    val date = sdf.parse("01-02-2015 08:00:00")
    val completed = new IceRoadShipmentSchedular(shipmentsfile,outputFile).getSchedule(date)
   
    assert (true === completed)
  
    val rowsource = CSVFileReader(outputFile,true)
  
    val calcualtedShipments = rowsource.map(row => row)
    
    val output = calcualtedShipments.toList
    
    assert (expected === output)
    
    
    if(outputFile.exists) outputFile.delete
    if(shipmentsfile.exists) shipmentsfile.delete
        
    
  }
  
  
  "Test full schedule with priority" should "return shipments schedule with first15 each <= 15 tons and after15 each load > 15" in {
    
    
    val shipments = Seq(List("10","cement","13","ton","1"),    				    
    				    List("289","steel bars","9","ton","3"),
    				    List("1","cement","16","ton","1"),
                        List("5","fuel","20","ton","2"),
                        List("4","bricks","35","ton","1"),
                        List("270","drill bits","15","ton"," "),
                        List("50","plastic","15.5","ton","2"),
    				    List("27","drill bits","17","ton"," "),
    				    List("632","bricks","10","ton","1"),
    				    List("29","steel bars","22","ton","3"),
    				    List("40","empty barrels","30","ton","3"),
    				    List("400","fuel","8","ton","2"),
    				    List("13","cranes","27","ton"," "))
    				    
    				    
    				    
    val expected  = List(List("2015-2-1","8","1","632"),
    		             List("2015-2-1","8","2","10"),
    					 List("2015-2-1","8","3","400"),
    					 List("2015-2-1","8","4","289"),
    					 List("2015-2-1","8","5","270"),    					 
    					 List("2015-2-16","8","6","4"),
    		             List("2015-2-16","8","7","1"),
    					 List("2015-2-16","9","8","5"),
    					 List("2015-2-16","9","9","50"),
    					 List("2015-2-16","9","10","40"),
    					 List("2015-2-16","9","11","29"),
    					 List("2015-2-16","9","12","13"),
    					 List("2015-2-16","9","13","27"))
    					 

    
    val shipmentsfile = new File(testDir+"testcaseFull.csv")
    
    if(shipmentsfile.exists) shipmentsfile.delete
    
    val rowwriter = CSVFileWriter(shipmentsfile,true)
    
    rowwriter.writeHeader(header)
    
    shipments.foreach(l => rowwriter.write(l))
    
    rowwriter.close
    
    assert (true === shipmentsfile.exists)

    val outputFile = new File(testDir+"testcaseFull_Results.csv")
    
    val sdf = new SimpleDateFormat("dd-M-yyyy hh:mm")
    val date = sdf.parse("01-02-2015 08:00:00")
    val completed = new IceRoadShipmentSchedular(shipmentsfile,outputFile).getSchedule(date)
   
    assert (true === completed)
  
    val rowsource = CSVFileReader(outputFile,true)
  
    val calcualtedShipments = rowsource.map(row => row)
    
    val output = calcualtedShipments.toList
    
    assert (expected === output)
    
    if(outputFile.exists) outputFile.delete
    if(shipmentsfile.exists) shipmentsfile.delete
            
    
  }
  
  


}
