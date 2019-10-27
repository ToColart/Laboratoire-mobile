# How to open DB ?
DB is contained in file test.mv.db in folder API.
To open it, use H2 console with username sa and empty password.

# How to locally deploy the API with IntelliJ ? 
You can follow instructions given in https://www.jetbrains.com/help/idea/sbt-support.html
To run it, create a new Configuration of type sbt and in field Tasks, just type ~run
To test it out, type localhost:9000 in your browser

# How to test API on another device ? 
Imao the easiest way to do so is to enable Windows 10 mobile hotspot. Once done, your computer IP address shoud default to 192.168.137.1. 
Connect your device to your computer hotspot and add a firewall inbound and outbound rule allowing traffic from the subnetwork 192.168.137.0 mask 255.255.255.0 to port 9000