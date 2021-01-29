# Distributed Interference Testbench

---

## Table of contents
   1. [Installations](#installations)
   1. [Manual](#manual)


## Installations required
### Install DUST: (also the java plugin)
1. Open a new terminal and download this repository: (do not checkout release/2.0 keep the master!)
- `https://olympus.idlab.uantwerpen.be/dust/docs/core/installation`
2. Install Device Monitor stack
- `https://olympus.idlab.uantwerpen.be/bitbucket/projects/COORD/repos/devicemonitor/browse/Readme.md`
3. Initialise submodules
- `git submodule update --init --recursive`
4. Go to the dust-core build directory and copy the two .so files to a directory (will be needed later!)
### Install MQTT:
1. Open a new terminal and download this repository:
- `sudo apt-get -y --no-install-recommends install libssl-dev`
- `git clone https://github.com/eclipse/mosquitto.git`
- `cd mosquitto`
- `git checkout v1.6.9`
- `mkdir build && cd build`
- `cmake -DCMAKE_BUILD_TYPE=Release -DWITH_BUNDLED_DEPS=ON -DDOCUMENTATION=OFF -DWITH_PIC=ON -DWITH_STATIC_LIBRARIES=ON ..`
- `sudo make -j4 install`
- `cd ../..`
- `rm -rfd mosquitto`
- `sudo apt install mosquitto-dev`
- `git clone https://olympus.idlab.uantwerpen.be/bitbucket/scm/dustm/mqtt-module.git`
- `cd mqtt-module`
- `mkdir build && cd build`
- `cmake -DCMAKE_BUILD_TYPE=Release ..`
- `make -j4`
2. Copy out the build directory the libmqtt_module.so and paste it in the same directory you copied the .so files from the dust-core
### Install ZMQ:
1. Open a new terminal:
- `sudo apt-get install libtool pkg-config build-essential autoconf automake`
- `sudo apt-get install libzmq3-dev`
- `git clone https://github.com/zeromq/libzmq.git`
- `cd libzmq`
- `mkdir build && cd build` 
- `cmake ..`
- `sudo make -j4 install`
### Install cppzmq:
1. Open a new terminal and download this repository:
- `git clone https://github.com/zeromq/cppzmq.git`
- `mkdir build && cd build`
- `cmake ..`
- `sudo make -j4 install`
### Install zmq-module:
1. Open a new terminal and download this repository:
- `git clone https://olympus.idlab.uantwerpen.be/bitbucket/scm/dustm/zmq-module.git`
- `cd zmq-module`
- `mkdir build && cd build`
- `cmake -DCMAKE_BUILD_TYPE=Release ..`
- `make -j4`
- copy the libzmq_module.so to the same directory where your other .so files are located.
### Install the needed files to configure the Testbench:
1. Open a new terminal and navigate to a place on the system you want the files located:
- `git clone https://github.com/ua7605/TestbenchNeededFiles.git`
- Move the .so files directory (you created earlier) to the same loaction.
- `cd TestbenchNeededFiles`
- open config.toml
- chane the path after "module_path" to the path you have saved the .so files.
- change the paht after "config_file_path" to the path were the configuration.json file is located (it is in the same directory as the conf.toml file)

## Common errors
- When running the Testbench with a VPN the log files will output an error on the fysical links.
- When the .so files are not properly installed the Testbench will crash.
- When the module_path in the config.toml file is not set up correct the Testbench will crash.
- When the config_file_path is not set proparly the Testbench will not read out all the DUST channels were the application sends data out on.
- If you not specify in the DUSTconfig.json file all the channels were the application that needs to be test sends on the Testbench will not read out the DUST channels.
- Do not start the Testbench with the sudo command. The output files will not be correctly generated.
- If you start the Testbench (the jar file) always give as argument the path to the were the config.toml is located on the system. If you don't do this the Testbench will crash.


## Docker
We made a demo application to that you can use to to determine the metrics of using the testbench: You can download the Docker image: `docker pull onevincentway/dustrepo:systemstressdustapplication`

### Starting the Docker container 
- `sudo docker run --rm --network host onevincentway/dustrepo:systemstressdustapplication`
You see that we run  the docker with local host. This is needed to recieve the DUST messages because a docker is by default an environment on its own.  

## Manual
### How to start the Testbench: !make sure you followed all the steps above!
1. Option one: from a prebuild JAR file:
-  `cd TestbenchNeededFiles`
-  `cd TestbenchJAR`
-  `java -jar Testbench.jar here/you/specify/your/path/to/the/config.toml`
-  `the Tetstbench will run on your machine.`
2. Option two from source code:
-  Open a new terminal and download this repository:
- `git clone https://olympus.idlab.uantwerpen.be/bitbucket/scm/coord/application-testbench.git`
- `git checkout Integration_DeviceMonitor`
- `cd application-testbench`
-  If you have IntelliJ IDEA you can open the code with that. run in terminal `mvn clean compile assembly:single`
-  If you just want to make a JAR file: run in the cli: `mvn clean compile assembly:single`
-  The JAR file will be located in the results directory.  
3. Usage:
When you want to use the Testbench it is important that you use it correctely:
-  Make sure you have installed the Dust-core [here](#Install-DUST:-(also-the-java-plugin))
-  Make sure you have installed MQTT, ZMQ, cppzmq
-  Make sure you have installed the required files to configure the Testbench. If you have installed them it is important that you correctely specify in the config.toml file the correct paht locations to the directory of the .so files.
When you start the Testbench with the JAR always include the path were the config.toml file is located. 
-  If you want get the metrics of specific DUST channels you need to setup the configuration.json file and in the DUSTconfig.json you specify the name of the channels were the Testbench needs to lisen on. 
-  If you completed all the steps the Testbench will work proparly. But never start the Testbench with the sudo command!
### Description of the Testbench

The goal of the Testbench is to determine what the load of an application is on a particular device. Such that you know how many resources your application consumes. The Testbench can calculate the metrics on memory, processor, Links and individual DUST channels.  

1. Where the output files will be generated:
The testbench will always place his output files in the home directory of the machine it is running on. He will create a directory "TestBenchFiles". In this directory he will create two sub directories "logFiles" and "outputFiles". In the logfiles directory you will find all the measurements he has performed. It makes an xml and json file for every metric. In the output files you will find the worst-case metrics of the application that has been tested.
The testbench will also generate an output file in the directory you have started the JAR file. That are .csv files.

### The working of the Testbench 
How the testbench actually works is as followed. 
#### Configuration files for the Testbench
1. config.toml:
In the config.toml you specify the following things:
-  module_path: here you specify the paht were the directory on your machine were the libbatch_module.so, libmqtt_module.so, libsocket_module.so and the libzmq_module.so is located.
-  `config_file_path`: here you specify the paht to the (DUST) configuration.json file.
-  `pathFile_toDUSTConfig`: here you specify the path to the DUSTconfig.json file.
-  `doAmeasurementEveryInMicroSeconds`: here you specify in microseconds how often the testbench needs to do a measurement.
-  `totalTimeOfTheMeasurementInSeconds`: here you specify how long the testbench needs to run. After that time is exceeded the Testbench will close it self
-  `startPhraseToStartMonitoring`: here you can specify what commando you want to use and type in the cli when the Testbench ask you that you turned on your application that needs to be tested.
2. configuration.json:
-  This is the DUST configuration file were you specifiy on what channels the application can listen. This is the configuration file of the application that needs to be tested. So you can copy it over. Then it is important that you make the correct changes: because now you have the configuration.json file of the application that needs to be tested. This means that you need to subscribe on the channel were in the configuration.json file as publisher are stated. So you need to make the correct changes. For example the Testbench in case of TCP/UDP is always the host_server so that needs to be true. And on the application side you need te change it to false. If your application is running in a Docker container you can use the mount option to mount a configuration.json file you have changed accouringly with the configuration.json file in the container itself. Also you need to change the IP addresses to local host so "127.0.0.1". Because the communication will run localy on the host machine. If you uses dockers to run the application make sure you link the host network with the docker network such that they are directly mapped on eachother.
3. DUSTconfig.json:
-  Here you specify the channel names, every channel you specify in here the testbench will lisen on and will calculate the meterics of and will be listed in the output file.
#### Generated output files of the Testbench
The testbench will generate his output files to the home directory of the machine he is running on. Herefore he makes a directory "TestBenchFiles". Were the output and log files are located. For conviency when the Testbench is done with the measurements he will specify the path were the directory is located and you can go to there immediately with a "cd" command. 

#### How the testbench works and under the hood:
So how the Testbench works, it will aways say to the user when he can start his application. The reason herefore is that before the application that needs to be tested is started the machine is clean. So it is not running any other "main" program (so make sure you are not running other programs). What the Testbench actually will do is, it will perform several measurements per metric you want to determine the worst case of. Such that he can find for every metric he has measured out of all measurements the lowest one. Then the Testbench will ask the user to start the Application that needs to be tested if the application has started the user needs to put in the command line the query the Testbench asks. After that the Testbench will be running parallel with the application. The Testbench will measure at a read the user has specified. And the Testbench will run a time the user has specified. At the end the Testbench will search for the every metric out of all measurements the highest one. Then he will substract the highest with the lowest. And so you have determined what the load is of the application.  
