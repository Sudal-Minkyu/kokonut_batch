#!/bin/bash

sudo su -

cd /root/kokonut_batch/

# 프로세스 종료
PORT=8055
echo "프로세스 종료용 포트조회 : $PORT"

PID=$(lsof -i :$PORT -t)

if [ -z "$PID" ]; then
    echo "종료할 프로세스가 없습니다. $PORT"
else
    echo "해당 프로세스를 종료합니다. $PORT is: $PID"
    kill -9 $PID
    sleep 5
fi

# 파일 삭제
# 새로운 파일 복사
cp /opt/codedeploy-agent/deployment-root/$DEPLOYMENT_GROUP_ID/$DEPLOYMENT_ID/deployment-archive/kokonut*.jar /root/kokonut*.jar

source ~/.bashrc

# 새로운 프로세스 시작
mkdir /root/kokonut_batch/logs

nohup java -jar -Dserver.port=8055 batch-0.0.1-SNAPSHOT.jar > /root/kokonut_batch/logs/$(date +%Y-%m-%d).log 2>&1 &

exit
