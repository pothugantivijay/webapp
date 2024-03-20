# Create the directory (if it does not exist)
sudo cp /tmp/systemd-service.service /etc/systemd/system/systemd-service.service
#this will allow us to log in for the first time
sudo yum update
sudo yum install java-17-openjdk-devel -y
#This will set the user details for the mysql
#sudo mysql -u root -p
#this will setup the connection with mysql

# Create a new group
sudo groupadd csye6225
# Create a new user with the group, no login shell, and create a home directory
sudo useradd -s /usr/sbin/nologin -g csye6225 csye6225
sudo mv /tmp/assignment-0.0.1-SNAPSHOT.jar /home/csye6225
sudo ls /home/csye6225

curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

sudo mv /tmp/OpsAgentConfig.yaml /etc/google-cloud-ops-agent/config.yaml

# Change the ownership of the directory
sudo chown -R csye6225:csye6225 /home/csye6225

sudo systemctl daemon-reload
sudo systemctl enable systemd-service.service
