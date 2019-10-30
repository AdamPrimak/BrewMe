#Setup
import glob
import time
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(18,GPIO.OUT) #Green LED
GPIO.setup(12,GPIO.OUT) #RED LED
GPIO.setup(13,GPIO.OUT) #Yellow LED


print ("LED on") #How to turn them on
GPIO.output(18,GPIO.HIGH)
GPIO.output(12,GPIO.HIGH)
GPIO.output(13,GPIO.HIGH)
time.sleep(1)# Time on duration
print ("LED off") #How to turn them off
GPIO.output(18,GPIO.LOW)
GPIO.output(12,GPIO.LOW)
GPIO.output(13,GPIO.LOW)
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
 
while True:
    print(read_temp())
    time.sleep(60)