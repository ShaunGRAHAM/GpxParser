//Course_pied_le_midi_28-05-2017.gpx
import java.io.*;
import java.util.*;


ArrayList<pGPX> tabPoint;
ArrayList<PTImage> tabImage;
XML xml;

Map m;

String [] folder={
  "COURSE_DU_05-06-2016", 
  "COURSE_DU_14-01-2017", 
  "COURSE_DU_15-04-2017", 
  "COURSE_DU_22-12-2016", 
  "COURSE_DU_23-08-2016", 
  "COURSE_DU_29-05-2016", 
  "COURSE_DU_29-08-2016", 
  "COURSE_DU_31-07-2016"
};
int indexCourse=0;

void setup() {
  size(800,800,P2D);
  //fullScreen(P2D);
  surface.setLocation(0, 0);

  loadCourse(folder[indexCourse]);
}
//
void loadCourse(String folder) {


  String [] listGPX = listFileNames(sketchPath()+"/data/"+folder+"/", "gpx");

  xml = loadXML(folder+"/"+listGPX[0]);

  m = new Map(10, 10, height-20, height-20);

  XML track=xml.getChild("trk").getChild("trkseg");
  tabPoint = new ArrayList<pGPX>();

  XML[] point=track.getChildren("trkpt");

  for (int i=0; i<point.length; i++) {
    pGPX p = new pGPX(i);
    p.lat = point[i].getFloat("lat");
    p.lon = point[i].getFloat("lon");
    p.ele = int(float(point[i].getChild("ele").getContent()));
    p.time = point[i].getChild("time").getContent();
    try {
      p.temp = float(point[i].getChild("extensions").getChild("gpxtpx:TrackPointExtension").getChild("gpxtpx:atemp").getContent());
      p.cardio = int(float(point[i].getChild("extensions").getChild("gpxtpx:TrackPointExtension").getChild("gpxtpx:hr").getContent()));
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


  String [] list = listFileNames(sketchPath()+"/data/"+folder+"/", "JPG");
  println(list);
  for (int i=0; i<list.length; i++) {
    tabImage.add(createPTImage(folder+"/"+list[i]));
  }
}


//-----------------------------------------------------------------------------------------------------------
float px=0;
float py=0;
float x=0;
float y=0;

float lat=0;
float lon=0;
float plat=0;
float plon=0;
int index=0;

void draw() {
  //fill(0,20);
  //rect(0,0,width,height);
  translate(width/2-height/2, 0);
  background(0);
  m.draw();


  for (int i=0; i<tabPoint.size(); i++) {
    pGPX p =tabPoint.get(i);

    //float car = map(p.cardio, 100, 180, 5, 240);
    float car = map(p.cardio, 80, 100, 30, 3);

    fill(255, car);
    fill(255, 100);

    noStroke();
    if (i%4==0)
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
    if (index>tabPoint.size()-1){
      index=0;
      indexCourse++;
      if(indexCourse>folder.length-1)indexCourse=0;
      loadCourse(folder[indexCourse]);
    }
  }

  x-=(x-px)*0.2;
  y-=(y-py)*0.2;

  lat-=(lat-plat)*0.2;
  lon-=(lon-plon)*0.2;


  fill(255, 0, 0);
  ellipse(x, y, 10, 10);
  fill(255);
  text(lat+"\n"+lon, x+10, y+10);

  imageDraw();
}
