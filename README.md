# How to open DB ?
DB is contained in file test.mv.db in folder API.
To open it, use H2 console with username sa and empty password.

# How to locally deploy the API with IntelliJ ? 
You can follow instructions given in https://www.jetbrains.com/help/idea/sbt-support.html
To run it, create a new Configuration of type sbt and in field Tasks, just type ~run
To test it out, type localhost:9000 in your browser

# How to test API on another device ? 
Imao the easiest way to do so is to enable Windows 10 mobile hotspot. Once done, your computer IP address shoud default to 192.168.137.1. 
Connect your device to your computer hotspot and add a firewall inbound and outbound rule allowing traffic from the subnetwork 192.168.137.0 mask 255.255.255.0 to port 9000. 

# Test user credentials
The DB contains a test user whose email is testAPI@test.com and password is Test123
	
# Available controllers
## Destination controller
### /destination/getDestinations
Response : a list of all available destinations
Fields : 
 - id (Int)
 - name (String)
 - description (String)
 - audio (String) : link of the audio file, which won't be hosted on the same server
 - coordx (Double)
 - coordy (Double)

### /destination/getDestination/id
Parameter : 
 - id : the id of the requested Destination
Fields : 
 - id (Int)
 - name (String)
 - description (String)
 - audio (String) : link of the audio file, which won't be hosted on the same server
 - coordx (Double)
 - coordy (Double)