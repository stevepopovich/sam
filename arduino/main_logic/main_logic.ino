#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"

const int trigPin = 13;// these will not work
const int echoPin = 12; // the motor shield or limit switches are using these

const byte numChars = 64;
char receivedChars[numChars];

boolean newData = false;

int currStep = 0;

Adafruit_MotorShield AFMS = Adafruit_MotorShield(); 

Adafruit_DCMotor *motor1 = AFMS.getMotor(1);
Adafruit_DCMotor *motor2 = AFMS.getMotor(2);
Adafruit_StepperMotor *stepper = AFMS.getStepper(200, 2);

int limitSwitchUpper = 13; // this one works great
int limitSwitchLower = 0; // analog read 

void setup() {
    Serial.begin(9600);

    AFMS.begin();
    
    stopMoving();

    stepper->setSpeed(1);
    stepper->step(1, FORWARD, DOUBLE);

    pinMode(limitSwitchUpper, INPUT); // 0 is closed
    pinMode(limitSwitchLower, INPUT); // 0 is closed
}

void loop() {
    recvWithEndMarker();
    processData();
//    readDistance();
  //    stopIfDistanceIsShort(); // Having the phone near the ultrasonic is causing the ultrasonic to short, reading zero
//    Serial.println(digitalRead(limitSwitchUpper)); 
//    Serial.println(analogRead(limitSwitchLower)); 

//    stepper->step(1, FORWARD, DOUBLE); // foward is down
    articulateFaceIfLegal(-200);
}

void articulateFaceIfLegal(int value) { // positive is look down
  if (value > 0 && analogRead(limitSwitchLower) != 0) {
    stepper->setSpeed(value);
    stepper->step(1, FORWARD, DOUBLE);
  } else if (value < 0 && digitalRead(limitSwitchUpper) != 0){
    stepper->setSpeed(abs(value));
    stepper->step(1, BACKWARD, DOUBLE);
  } else  {
     // Do I need to do anything?
  }
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
        } else { // expecting just a number `-255/-255` - `255/255`
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
//  duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
//  distance = duration * 0.034 / 2; // Speed of sound wave divided by 2 (back and forth)
//  Serial.print("Distance: ");
//  Serial.print(distance);
//  Serial.println(" cm");
}

void stopIfDistanceIsShort() {
//  if (distance < 10) {
//    stopMoving();  
//  }
}

void goForward() { 
  motor1->setSpeed(255);
  motor2->setSpeed(255);

  motor1->run(FORWARD);
  motor2->run(FORWARD);
}

void goBackward() {
  motor1->setSpeed(255);
  motor2->setSpeed(255);

  motor1->run(BACKWARD);
  motor2->run(BACKWARD);

//  stepper->setSpeed(255);
//  stepper->step(10, FORWARD, MICROSTEP);
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
