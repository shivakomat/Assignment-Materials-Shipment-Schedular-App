package com.garner.tool.parser

import java.io.File
import java.io.PrintWriter
import java.io.FileWriter

sealed case class CSVWriter(file:File,append:Boolean=false) extends Writer[List[String]]{
  
   private val writer = new FileWriter(file,append)
   private val seperator = ","
   private val newline = "\n"
    
   def writeHeader(header:List[String]) = writer.write(header.mkString(seperator))
   
   def write(data:List[String]) = writer.write(newline+data.mkString(seperator))
   
   def close = writer.close()
}

