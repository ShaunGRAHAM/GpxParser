import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.*; 
import java.util.*; 
import com.drew.imaging.ImageMetadataReader; 
import com.drew.imaging.ImageProcessingException; 
import com.drew.lang.GeoLocation; 
import com.drew.metadata.exif.GpsDirectory; 
import com.drew.metadata.Directory; 
import com.drew.metadata.Metadata; 
import com.drew.metadata.*; 
import com.drew.metadata.Tag; 
import com.drew.metadata.*; 
import com.drew.imaging.jpeg.JpegMetadataReader; 

import com.adobe.xmp.*; 
import com.adobe.xmp.impl.*; 
import com.adobe.xmp.impl.xpath.*; 
import com.adobe.xmp.options.*; 
import com.adobe.xmp.properties.*; 
import com.drew.imaging.bmp.*; 
import com.drew.imaging.jpeg.*; 
import com.drew.imaging.png.*; 
import com.drew.imaging.psd.*; 
import com.drew.lang.*; 
import com.drew.metadata.*; 
import com.drew.metadata.exif.*; 
import com.drew.metadata.exif.makernotes.*; 
import com.drew.metadata.gif.*; 
import com.drew.metadata.iptc.*; 
import com.drew.metadata.jfxx.*; 
import com.drew.metadata.jpeg.*; 
import com.drew.metadata.photoshop.*; 
import com.drew.metadata.png.*; 
import com.drew.metadata.webp.*; 
import com.drew.tools.*; 
import com.drew.imaging.*; 
import com.drew.imaging.tiff.*; 
import com.drew.metadata.adobe.*; 
import com.drew.metadata.file.*; 
import com.drew.metadata.icc.*; 
import com.drew.metadata.jfif.*; 
import com.drew.metadata.pcx.*; 
import com.drew.metadata.tiff.*; 
import com.drew.imaging.riff.*; 
import com.drew.metadata.bmp.*; 
import com.drew.metadata.ico.*; 
import com.drew.metadata.xmp.*; 
import com.drew.imaging.gif.*; 
import com.drew.lang.annotations.*; 
import com.drew.imaging.ico.*; 
import com.drew.imaging.pcx.*; 
import com.drew.imaging.raf.*; 
import com.drew.imaging.webp.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gpxParser5 extends PApplet {

//Course_pied_le_midi_28-05-2017.gpx




ArrayList<pGPX> tabPoint;
ArrayList<PTImage> tabImage;
XML xml;

Map m;

public void setup() {
  //size(900,900,P2D);
  
  surface.setLocation(0,0);
   
  xml = loadXML("Course_pied_nocturne2.gpx");

  m = new Map(10, 10, width-20, height-20);

  XML track=xml.getChild("trk").getChild("trkseg");
  tabPoint = new ArrayList<pGPX>();

  XML[] point=track.getChildren("trkpt");

  for (int i=0; i<point.length; i++) {
    pGPX p = new pGPX(i);
    p.lat = point[i].getFloat("lat");
    p.lon = point[i].getFloat("lon");
    p.ele = PApplet.parseInt(PApplet.parseFloat(point[i].getChild("ele").getContent()));
    p.time = point[i].getChild("time").getContent();
    try {
      p.temp = PApplet.parseFloat(point[i].getChild("extensions").getChild("gpxtpx:TrackPointExtension").getChild("gpxtpx:atemp").getContent());
      p.cardio = PApplet.parseInt(PApplet.parseFloat(point[i].getChild("extensions").getChild("gpxtpx:TrackPointExtension").getChild("gpxtpx:hr").getContent()));
    }
    catch(Exception e) {
      print("CARDIO NOT FOUND");
    }
    //p.print();
    tabPoint.add(p);

    if (i==0) {
      m.ref(p.lat, p.lon);
    }
    m.setX(p.lat);
    m.setY(p.lon);
  }
  tabImage = new ArrayList<PTImage>();
  tabImage.add(createPTImage("IMG_3491.JPG"));
  tabImage.add(createPTImage("IMG_3492_v.JPG"));
  tabImage.add(createPTImage("IMG_3372.JPG"));
}

float px=0;
float py=0;
float x=0;
float y=0;

float lat=0;
float lon=0;
float plat=0;
float plon=0;
int index=0;

public void draw() {
  background(0);
  m.draw();
  
  
  
  
  for (int i=0; i<tabPoint.size(); i++) {
    pGPX p =tabPoint.get(i);

    float car = map(p.cardio, 100, 180, 5, 240);
    //float car2 = map(p.cardio, 80, 100, 30, 3);

    fill(255, car);
    fill(255, 100);

    noStroke();
    ellipse(
      map((p.lat), m.xMin, m.xMax, m.x, m.w), 
      map((p.lon), m.yMin, m.yMax, m.y, m.h), 
      3, 3);
  }


  

  /*
  float gx = map(mouseX,m.x, m.w,m.xMin, m.xMax);
   float gy = map(mouseY,m.y, m.h,m.yMin, m.yMax);
   fill(0);
   text(gx+"\n"+gy,mouseX+10,mouseY+10);
   */

  if (frameCount%1==0) {
    pGPX pa =tabPoint.get(index);

    plat = pa.lat;
    plon = pa.lon;

    px = map((pa.lat), m.xMin, m.xMax, m.x, m.w);       
    py = map((pa.lon), m.yMin, m.yMax, m.y, m.h);       

    index++;
    if (index>tabPoint.size()-1)index=0;
  }

  x-=(x-px)*0.1f;
  y-=(y-py)*0.1f;

  lat-=(lat-plat)*0.1f;
  lon-=(lon-plon)*0.1f;


  fill(255, 0, 0);
  ellipse(x, y, 10, 10);
  fill(255);
  text(lat+"\n"+lon, x+10, y+10);
  
  imageDraw();
}
class PTImage {
  PImage ima;
  float gpsLat;
  float gpsLong;
  float gpsAltitude;
  float gpsSpeed;
  String time;
  PTImage(PImage ima, float gpsLat, float gpsLong, float gpsAltitude, float gpsSpeed, String time) {
    this.ima=ima;
    this.gpsLat=gpsLat;
    this.gpsLong=gpsLong;
    this.gpsAltitude=gpsAltitude;
    this.gpsSpeed=gpsSpeed;
    this.time=time;
  }
  
  
}
class pGPX{
  int index;
  float lat;
  float lon;
  int ele;
  String time;
  float temp;
  float cardio;
  
  pGPX(int index){
    this.index=index;
  }
  public void print(){
    println("--------------------------------------------");
    println("index : "+index);
    println("lon : "+lon);
    println("lat : "+lat);
    println("ele : "+ele);
    println("cardio : "+cardio);
  }
}












//"IMG_3491.JPG"
public PTImage createPTImage(String name) {
  PImage ima = loadImage(name);
  float gpsLat=0;
  float gpsLong=0;
  float gpsAltitude=0;
  float gpsSpeed=0;
  String time="";

  try {
    String path = sketchPath()+"/data/"+name;
    println(path);
    File jpegFile = new File(path);
    Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
    println(metadata.toString());

    for (Directory directory : metadata.getDirectories()) {
      for (Tag allTags : directory.getTags()) {
        /*
        println("-------------------------------------");
        println(">> "+allTags.getTagName());
        println("-------------------------------------");
        println(allTags.getDescription());
        */
        if (allTags.getTagName().equals("GPS Latitude")) {
          //String[] s1=splitTokens(allTags.getDescription(), "\"\' \u00b0,");
          //gpsLat=float(s1[0]+"."+s1[1]+s1[2]+s1[3]);
          gpsLat = convertHourToDecimal(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Longitude")) {
          //String[] s1=splitTokens(allTags.getDescription(), "\"\' \u00b0,");
          //gpsLong=float(s1[0]+"."+s1[1]+s1[2]+s1[3]);
          gpsLong = convertHourToDecimal(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Speed")) {//GPS Speed
          gpsSpeed=PApplet.parseFloat(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Altitude")) {//GPS Speed
          String[] s1=splitTokens(allTags.getDescription(), " ");
          gpsAltitude=PApplet.parseFloat(s1[0]);
        }
        if (allTags.getTagName().equals("File Modified Date")) {//GPS Speed
          time=allTags.getDescription();
        }
      }
    }
  } 
  catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  PTImage pt = new PTImage(ima,gpsLat,gpsLong,gpsAltitude,gpsSpeed,time);
  
  println("--------------------------------------");
  println(name);
  println(gpsLat);
  println(gpsLong);
  println(gpsSpeed);
  println(gpsAltitude);
  println(time);
  
  return pt;
}



public float convertHourToDecimal(String degree) { 
    //degree.matches("(-)?[0-6][0-9]\"[0-6][0-9]\'[0-6][0-9](.[0-9]{1,5})?");
    String[] s1=splitTokens(degree, "\"\' \u00b0");
    println(">>> "+s1[0]+" "+s1[1]+" "+s1[2]);
    s1[2] = s1[2].replace(',','.');
    
    return PApplet.parseFloat(s1[0])+PApplet.parseFloat(s1[1])/60+PApplet.parseFloat(s1[2])/3600;
}
class Map {

  float xMin=0;
  float xMax=0;
  float yMin=0;
  float yMax=0;

  float x;
  float y;
  float w;
  float h;

  Map(float x,float y,float w,float h) {
    this.x=x;
    this.y=y;
    this.w=w;
    this.h=h;
  }
  
  public void ref(float x,float y){
    xMin = x;
    xMax = x;
    yMin = y;
    yMax = y;
  }
  
  public void setX(float v){
    if(v<xMin){
      xMin=v;
    }
    if(v>xMax){
      xMax=v;
    }
  }
  
  public void setY(float v){
    if(v<yMin){
      yMin=v;
    }
    if(v>yMax){
      yMax=v;
    }
  }
  
  public void draw(){
    stroke(255);
    noFill();
    rect(x,y,w,h);
    
    fill(0);
    text("xMin : "+xMin+"  xMax : "+xMax,x+10,y+20);
    text("yMin : "+yMin+"  yMax : "+yMax,x+10,y+40);
  }
    
}
public void imageDraw(){
  for (int i=0; i<tabImage.size(); i++) {
    PTImage p =tabImage.get(i);

    float imagex =map((p.gpsLat), m.xMin, m.xMax, m.x, m.w);
    float imagey =map((p.gpsLong), m.yMin, m.yMax, m.y, m.h);
    //float car = map(p.cardio, 100, 180, 5, 240);
    //float car2 = map(p.cardio, 80, 100, 30, 3);

    fill(0, 255, 0, 100);

    noStroke();
    ellipse(
      map((p.gpsLat), m.xMin, m.xMax, m.x, m.w), 
      map((p.gpsLong), m.yMin, m.yMax, m.y, m.h), 
      10, 10);

    /*
    tint(255,100);
     image(p.ima,
     map((p.gpsLat), m.xMin, m.xMax, m.x, m.w), 
     map((p.gpsLong), m.yMin, m.yMax, m.y, m.h), 
     p.ima.width/10, p.ima.height/10);
     */
    if (dist(x, y, imagex, imagey)<255) {
      
      float val = map(constrain(dist(x, y, imagex, imagey), 0, 255),0,255,255,0);
      stroke(255, val);
      line(x, y, imagex, imagey);
      tint(255, val);
      image(p.ima, 0, 0, width, height);
    }
  }
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "gpxParser5" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
