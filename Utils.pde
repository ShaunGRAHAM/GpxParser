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


//"IMG_3491.JPG"
PTImage createPTImage(String name) {
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
          //String[] s1=splitTokens(allTags.getDescription(), "\"\' °,");
          //gpsLat=float(s1[0]+"."+s1[1]+s1[2]+s1[3]);
          gpsLat = convertHourToDecimal(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Longitude")) {
          //String[] s1=splitTokens(allTags.getDescription(), "\"\' °,");
          //gpsLong=float(s1[0]+"."+s1[1]+s1[2]+s1[3]);
          gpsLong = convertHourToDecimal(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Speed")) {//GPS Speed
          gpsSpeed=float(allTags.getDescription());
        }
        if (allTags.getTagName().equals("GPS Altitude")) {//GPS Speed
          String[] s1=splitTokens(allTags.getDescription(), " ");
          gpsAltitude=float(s1[0]);
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
    String[] s1=splitTokens(degree, "\"\' °");
    println(">>> "+s1[0]+" "+s1[1]+" "+s1[2]);
    s1[2] = s1[2].replace(',','.');
    
    return float(s1[0])+float(s1[1])/60+float(s1[2])/3600;
}


String[] listFileNames(String dir,String key) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    
    StringList inventory = new StringList();

    for (int i = 0; i < names.length; ++i) {
      String [] analyse=split(names[i],".");
      if(analyse[1].equals(key)){
        //println(names[i]);
        inventory.append(names[i]);
      }
    }
    
    return inventory.array();
  } else {
    // If it's not a directory
    return null;
  }
}