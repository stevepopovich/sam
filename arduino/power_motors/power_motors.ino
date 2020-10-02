int motorPin0 = 5;
int motorPin1 = 6;
int speed = 0;
 
void setup() 
{ 
  pinMode(motorPin0, OUTPUT);
  pinMode(motorPin1, OUTPUT);
  Serial.begin(9600);
} 
 
 
void loop() 
{ 
  analogWrite(motorPin0, 0);
  analogWrite(motorPin1, 0);
  delay(1000);
} 
