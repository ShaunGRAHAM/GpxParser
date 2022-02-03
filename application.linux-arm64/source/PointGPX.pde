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
  void print(){
    println("--------------------------------------------");
    println("index : "+index);
    println("lon : "+lon);
    println("lat : "+lat);
    println("ele : "+ele);
    println("cardio : "+cardio);
  }
}