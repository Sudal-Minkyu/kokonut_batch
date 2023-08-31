#!/bin/bash
sudo su -
cd /root/kokonut_batch/

# 프로세스 종료
# ps aux | grep java | grep -v grep | awk '{print $2}' | xargs kill -9
ps -ef | grep java | grep -v sonarqube | grep -v grep | awk '{print $2}' | xargs kill

# 10초간 대기합니다.
sleep 10

# 파일 삭제
rm -rf /root/kokonut_backend/kokonut*.jar

# 새로운 파일 복사
cp /opt/codedeploy-agent/deployment-root/$DEPLOYMENT_GROUP_ID/$DEPLOYMENT_ID/deployment-archive/kokonut*.jar /root/kokonut_batch/kokonut*.jar

source ~/.zshrc

# 새로운 프로세스 시작
mkdir /root/kokonut_batch/logs

nohup java -jar kokonut_batch-0.0.1-SNAPSHOT.jar > /root/kokonut_batch/logs/$(date +%Y-%m-%d).log 2>&1 &

exit
