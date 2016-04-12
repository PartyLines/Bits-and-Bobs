const int leds = 6;
int buttonDown = 0;
int active = 0;
int buttonState;

void setup(){
  pinMode(A0, 0);
  pinMode(A1, 0);
  pinMode(2, 0);
  pinMode(13, 1);
  for(int i=3; i<=(3+leds); i++){
    pinMode(i, 1);
  }
  digitalWrite(13, 0);
}

void loop(){
  buttonState = digitalRead(2);
  if(buttonDown == 0 && buttonState == 1){
    active = 1 - active;
    buttonDown = 1;
  }else if(buttonDown == 1 && buttonState == 0){
    buttonDown = 0;
  }
  if(active == 1){
    for(int i=3; i<(3+leds); i++){
      digitalWrite(i, 1);
    }
    delay(10+(analogRead(A0)/4));
    for(int i=3; i<(3+leds); i++){
      digitalWrite(i, 0);
    }
    delay(10+(analogRead(A1)/4));
  }else{
    delay(1);
  }
}
