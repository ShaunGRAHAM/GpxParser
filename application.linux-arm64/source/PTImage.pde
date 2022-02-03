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