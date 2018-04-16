javac DNS/*.java LocalDNS/*.java client/*.java HisCinema/*.java HerCDN/*.java
start cmd.exe /k java LocalDNS/LocalDNS
start cmd.exe /k java HisCinema/HisCinemaAuthDNS
start cmd.exe /k java HisCinema/HisCinemaHTTPServer
start cmd.exe /k java HerCDN/HerCDNAuthDNS
start cmd.exe /k java HerCDN/HerCDNHTTPServer
start cmd.exe /k java client/Browser