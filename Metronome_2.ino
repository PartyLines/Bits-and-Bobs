#include <LiquidCrystal.h>

LiquidCrystal lcd(2, 3, 4, 5, 6, 7);
unsigned long delayStart = 0;
unsigned long delayTime = 0;
unsigned long delayMillis = 0;
unsigned long beatTime = 0;
int delayMicro = 0;
int bpm = 60;
int tapCount = 0;
byte state = 0; //0 = inactive, 1 = running, 2 = tap time
byte toggleOn = 0;
byte tapOn = 0;
int pot = 0;
boolean tapped = false;

void setup() {
    pinMode(10, 1);
    pinMode(9, 0);
    pinMode(8, 0);
    lcd.begin(16, 2);
    lcd.setCursor(0, 0);
    lcd.print("Off     ");
    lcd.setCursor(0, 1);
    lcd.print("BPM: ");
    Serial.begin(9600);
}

void loop() {
  if(state == 1){
      Serial.println(micros() - beatTime); 
      beatTime = micros();
      digitalWrite(10, 1);
      delay(30);
      digitalWrite(10, 0);
      delay(delayMillis-32);
      delayMicroseconds(delayMicro+960);
      if(digitalRead(9)){
          state = 0;
          lcd.setCursor(0, 0);
          lcd.print("Off   ");
          tapped = false;
          toggleOn = 1;
      }
  }else if(state == 0 && digitalRead(9) == 1 && toggleOn == 0){
      state = 1;
      delayTime = 60000000/bpm;
      delayMicro = delayTime%1000;
      delayMillis = (delayTime - delayMicro)/1000;
      lcd.setCursor(0, 0);
      lcd.print("Active");
  }else if(state == 0 && pot != analogRead(A0) && !tapped){
      pot = analogRead(A0);
      bpm = 50 + (pot/5);
      lcd.setCursor(4, 1);
      lcd.print(bpm);
      if(bpm < 100){
          lcd.print(" ");
      }
  }else if (digitalRead(8) == 1 && tapOn == 0){
      state = 2;
      tapCount +=1;
      if(tapCount == 1){
          lcd.setCursor(0, 0);
          lcd.print("Tap");
          delayStart = micros();
      }
      lcd.setCursor(tapCount+2, 0);
      lcd.print(".");
      if(tapCount == 5){
          tapped = true;
          state = 0;
          delayTime = (micros() - delayStart)/4;
          bpm = 60000000/delayTime;
          tapCount = 0;
          delay(150);
          lcd.setCursor(0, 0);
          lcd.print("Off     ");
          lcd.setCursor(4, 1);
          lcd.print(bpm);
          if(bpm < 100){
              lcd.print(" ");
          }
      }
      tapOn = 1;
  }else{
      tapOn = digitalRead(8);
      toggleOn = digitalRead(9);
      pot = analogRead(A0);
  }
  delay(1);
}
