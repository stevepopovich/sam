int enA = 9;
int in1 = 8;
int in2 = 7;

int enB = 3;
int in3 = 5;
int in4 = 4;

const byte numChars = 64;
char receivedChars[numChars];

boolean newData = false;

void setup() {
    Serial.begin(9600);

    // Set all the motor control pins to outputs
    pinMode(enA, OUTPUT);
    pinMode(enB, OUTPUT);
    pinMode(in1, OUTPUT);
    pinMode(in2, OUTPUT);
    pinMode(in3, OUTPUT);
    pinMode(in4, OUTPUT);
    
    stopMoving();
}

void loop() {
    recvWithEndMarker();
    processData();
    delay(250);
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
        if (strcmp(receivedChars, "backward") == 0) {
          goBackward();
        }
        if (strcmp(receivedChars, "left") == 0) {
          spinCounterClockwise();
        }
        if (strcmp(receivedChars, "right") == 0) {
          spinClockwise();
        }
        if (strcmp(receivedChars, "stop") == 0) {
          stopMoving();
        }
        
        newData = false;
    }
}

void spinCounterClockwise() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
  
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void spinClockwise() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
  
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void goForward() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
  
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void goBackward() {
  analogWrite(enA, 255);
  analogWrite(enB, 255);
  
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void stopMoving() {
  analogWrite(enA, 0);
  analogWrite(enB, 0);
}
