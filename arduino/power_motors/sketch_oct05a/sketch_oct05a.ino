
int leftPWMPort = 5;
int rightPWMPort = 10;

int leftDigital1 = 6;
int leftDigital2 = 7;

int rightDigital1 = 9;
int rightDigital2 = 8;

void setup() {
  pinMode(leftPWMPort, OUTPUT);
  pinMode(leftDigital1, OUTPUT);
  pinMode(leftDigital2, OUTPUT);
  pinMode(rightPWMPort, OUTPUT);
  pinMode(rightDigital1, OUTPUT);
  pinMode(rightDigital2, OUTPUT);
}

void loop() {
  digitalWrite(leftDigital1, LOW);
  digitalWrite(leftDigital2, HIGH);
  digitalWrite(rightDigital1, HIGH);
  digitalWrite(rightDigital2, LOW);
  
  analogWrite(leftPWMPort, 255);
  analogWrite(rightPWMPort, 255);
  delay(1000);
}
