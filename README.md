
Port forwarding: 
```
sudo apt-get install iptables
sudo nano /etc/rc.local
```
And add this line before exit 0
```
iptables -t nat -A PREROUTING -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080
```
Service starten mit:
```
sudo systemctl start rc.local
```


Service erstellt in: 
```
sudo nano /etc/systemd/system/server.service 
```
```
[Unit]
Description=Spring Server for sensors
After=network.target
StartLimitIntervalSec=0

[Service]
Restart=always
RestartSec=1
User=pi
ExecStart=java -jar /home/pi/stuff/SpringServer-1.jar

[Install]
WantedBy=multi-user.target
```
Service starten mit:
```
sudo systemctl start server.service
```
Service für nach boot autostart:
```
sudo systemctl enable server
```
Einstellungen speichern mit:
```
 sudo systemctl daemon-reload
```