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
  
  void ref(float x,float y){
    xMin = x;
    xMax = x;
    yMin = y;
    yMax = y;
  }
  
  void setX(float v){
    if(v<xMin){
      xMin=v;
    }
    if(v>xMax){
      xMax=v;
    }
  }
  
  void setY(float v){
    if(v<yMin){
      yMin=v;
    }
    if(v>yMax){
      yMax=v;
    }
  }
  
  void draw(){
    stroke(255);
    noFill();
    rect(x,y,w,h);
    
    fill(0);
    text("xMin : "+xMin+"  xMax : "+xMax,x+10,y+20);
    text("yMin : "+yMin+"  yMax : "+yMax,x+10,y+40);
  }
    
}