#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"

const int trigPin = 10;
const int echoPin = 11;

const byte numChars = 64;
char receivedChars[numChars];

boolean newData = false;
boolean crawlingForward = false;

int currStep = 0;

int distance = 0;
long duration = 0;

Adafruit_MotorShield AFMS = Adafruit_MotorShield(); 

Adafruit_DCMotor *motor1 = AFMS.getMotor(1);
Adafruit_DCMotor *motor2 = AFMS.getMotor(2);

void setup() {
    Serial.begin(9600);

    AFMS.begin();
    
    stopMoving();

    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
}

void loop() {
    recvWithEndMarker();
    processData();
    readDistance();
    crawlForward();
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
          //goForward();
          crawlingForward = true;
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
          crawlingForward = false;
          stopMoving();
        } else { // expecting a number -255 to 255
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
}

void crawlForward() {
    if (crawlingForward == true && distance > 15) {
      motor1->setSpeed(110);
      motor2->setSpeed(110);
        
      motor1->run(FORWARD);
      motor2->run(FORWARD);
     } else if (crawlingForward == true && distance < 15) {
      stopMoving();  
     }
}

void goForward() { 
    if (distance > 12) {
      motor1->setSpeed(90);
      motor2->setSpeed(90);
      
      motor1->run(FORWARD);
      motor2->run(FORWARD);
    } else {
      stopMoving();  
    }
}

void goBackward() {
  motor1->setSpeed(255);
  motor2->setSpeed(255);

  motor1->run(BACKWARD);
  motor2->run(BACKWARD);
}

void spinClockwise(int spinSpeed) {
  motor1->setSpeed(spinSpeed);
  motor2->setSpeed(spinSpeed);

  motor1->run(BACKWARD);
  motor2->run(FORWARD);
}

void spinCounterClockwise(int spinSpeed) {
  motor1->setSpeed(spinSpeed);
  motor2->setSpeed(spinSpeed);

  motor1->run(FORWARD);
  motor2->run(BACKWARD);
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
  motor1->setSpeed(0);
  motor2->setSpeed(0);  
}
