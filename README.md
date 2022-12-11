Setting up environmental variables: \
Intelij: Run -> Edit Configurations... -> Environment Variables
```
DB_USER=[user];DB_PASSWORD=[password]
```
Using a database via ssh-tunnel:
```
DB_USER=postgres;DB_PASSWORD=root;SSH_Key=[keyContent];...//TODO
```

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
RestartSec=20
User=pi
Environment=DB_USER=[user] #TODO
Environment=DB_PASSWORD=[psw] #TODO
Environment=DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/postgres
ExecStart=java -jar $PWD/SpringServer-1.jar

[Install]
WantedBy=multi-user.target
```
Service starten mit:
```
sudo systemctl start server.service
```
Service fÃ¼r nach boot autostart:
```
sudo systemctl enable server
```
Einstellungen speichern mit:
```
 sudo systemctl daemon-reload
```

Ardiono IDE ESP8266 Library: http://arduino.esp8266.com/stable/package_esp8266com_index.json

Tile CSS: https://1linelayouts.glitch.me/

sudo nano /var/log/postgresql/postgresql-13-main.log - to see postgres logs

postgres login unix:  sudo su - postgres
-> Probleme in "/pg_hba.conf" der Datenbank müssen Loginmethoden für die Lokale Benutzung verändert werden.