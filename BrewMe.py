#Setup
import glob
import time
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(18,GPIO.OUT) #Green LED
GPIO.setup(12,GPIO.OUT) #RED LED
GPIO.setup(13,GPIO.OUT) #Yellow LED
GPIO.setup(23,GPIO.OUT) #Buzzer

base_dir = '/sys/bus/w1/devices/'
device_folder = glob.glob(base_dir + '28*')[0]
device_file = device_folder + '/w1_slave'


def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines
 
def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c, temp_f
    
def LED_color(): #hard coded limits on temperature - Green = within limits, Yellow = near limits, Red = outside limits
    temp = read_temp()
    if temp[0] < 5 or temp[0] > 30:
        GPIO.output(18,GPIO.LOW)
        GPIO.output(13,GPIO.LOW)
        GPIO.output(12,GPIO.HIGH)
        GPIO.output(23,GPIO.HIGH)
        return "Temprature is outside of limits"
    if temp[0] < 15 and temp[0] > 5 or temp[0] < 30 and temp[0] > 20:
        GPIO.output(12,GPIO.LOW)
        GPIO.output(18,GPIO.LOW)
        GPIO.output(23,GPIO.LOW)
        GPIO.output(13,GPIO.HIGH)
        return "Temperature is near the limits"
    if temp[0] < 20 and temp[0] > 15:
        GPIO.output(12,GPIO.LOW)
        GPIO.output(13,GPIO.LOW)
        GPIO.output(23,GPIO.LOW)
        GPIO.output(18,GPIO.HIGH)
        return "Temperature is within the range"
    
 
while True:
    temp = 0
    print(read_temp())
    print(LED_color())
    time.sleep(1)