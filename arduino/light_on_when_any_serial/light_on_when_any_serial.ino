void setup() {
  Serial.begin(9600);
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);
}

// the loop function runs over and over again forever
void loop() {
  while (Serial.available() > 0) {
    digitalWrite(LED_BUILTIN, HIGH);
    Serial.read();
    delay(250);
   }

  digitalWrite(LED_BUILTIN, LOW);
}
