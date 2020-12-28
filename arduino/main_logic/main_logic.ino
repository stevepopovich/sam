const int enA = 9;
const int in1 = 8;
const int in2 = 7;

const int enB = 3;
const int in3 = 5;
const int in4 = 4;

const int trigPin = 13;
const int echoPin = 12;

const byte numChars = 64;
char receivedChars[numChars];

boolean newData = false;

float duration, distance;

void setup() {
    Serial.begin(9600);

    // Set all the motor control pins to outputs
    pinMode(enA, OUTPUT);
    pinMode(enB, OUTPUT);
    pinMode(in1, OUTPUT);
    pinMode(in2, OUTPUT);
    pinMode(in3, OUTPUT);
    pinMode(in4, OUTPUT);

    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
    
    stopMoving();
}

void loop() {
    recvWithEndMarker();
    processData();
    readDistance();
//    stopIfDistanceIsShort(); // Having the phone near the ultrasonic is causing the ultrasonic to short, reading zero
}

void recvWithEndMarker() {
    static byte ndx = 0;
    char endMarker = '\n';
    char rc;
   
    while (Serial.available() > 0 && newData == false) {
        rc = Serial.read();

        if (rc != endMarker) {
            receivedChars[ndx] = rc;
            ndx++;
            if (ndx >= numChars) {
                ndx = numChars - 1;
            }
        }
        else {
            receivedChars[ndx] = '\0'; // terminate the string
            ndx = 0;
            newData = true;
        }
    }
}

void processData() {
    if (newData == true) {
        if (strcmp(receivedChars, "forward") == 0) {
          goForward();
        }
        else if (strcmp(receivedChars, "backward") == 0) {
          goBackward();
        }
        else if (strcmp(receivedChars, "left") == 0) {
          spinCounterClockwise(255);
        }
        else if (strcmp(receivedChars, "right") == 0) {
          spinClockwise(255);
        }
        else if (strcmp(receivedChars, "stop") == 0) {
          stopMoving();
        } else { // expecting just a number -255 - 255
          spinByNumber(atoi(receivedChars));
        }
        
        newData = false;
    }
}

void readDistance() {
  // Clears the trigPin condition
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin HIGH (ACTIVE) for 10 microseconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2; // Speed of sound wave divided by 2 (back and forth)
//  Serial.print("Distance: ");
//  Serial.print(distance);
//  Serial.println(" cm");
}

void stopIfDistanceIsShort() {
  if (distance < 10) {
    stopMoving();  
  }
}

void goForward() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
    
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void goBackward() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
  
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void spinClockwise(int spinSpeed) {
  analogWrite(enA, spinSpeed);
  analogWrite(enB, spinSpeed);
  
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void spinCounterClockwise(int spinSpeed) {
  analogWrite(enA, spinSpeed);
  analogWrite(enB, spinSpeed);
  
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

// 50 is about the lowest value this can take without stalling
void spinByNumber(int value) {
  if (value == 0) {
    stopMoving();
  } else if (value > 0) {
    spinClockwise(value);
  } else {
    spinCounterClockwise(-value);
  }   
}

void stopMoving() {
  analogWrite(enA, 0);
  analogWrite(enB, 0);
}
