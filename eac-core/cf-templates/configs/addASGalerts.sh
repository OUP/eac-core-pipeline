#!/bin/bash
# chkconfig: 2345 20 80
# description: Description comes here....

# Source function library.
. /etc/init.d/functions

LOCKFILE=/var/lock/subsys/addASGalerts
env="|ENVIRONMENT|"
namespace="|GROUP|"
region="|REGION|"

start() {
    touch ${LOCKFILE}
    instanceId=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)
    snstopic="|SNSTOPIC|"

    /usr/bin/aws cloudwatch put-metric-alarm --alarm-name High-MemoryUsage-Alerter-$env-$namespace-$instanceId --alarm-description "Alarm when Memory Usage exceeds 80 percent" --metric-name MEMORY_USAGE --namespace $namespace --statistic Average --period 60 --threshold 80 --comparison-operator GreaterThanThreshold  --dimensions "Name=InstanceId,Value=$instanceId" "Name=Environment,Value=$env" --evaluation-periods 5 --alarm-actions $snstopic --unit Percent --ok-actions $snstopic --region $region

    /usr/bin/aws cloudwatch put-metric-alarm --alarm-name High-DiskUsage-Alerter-$env-$namespace-$instanceId --alarm-description "Alarm when Disk Usage exceeds 80 percent" --metric-name DISK_USAGE --namespace $namespace --statistic Average --period 60 --threshold 80 --comparison-operator GreaterThanThreshold  --dimensions "Name=InstanceId,Value=$instanceId" "Name=Environment,Value=$env" --evaluation-periods 5 --alarm-actions $snstopic --unit Percent --ok-actions $snstopic --region $region
}

stop() {
    rm ${LOCKFILE}
    instanceId=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)
    /usr/bin/aws cloudwatch delete-alarms --alarm-names High-MemoryUsage-Alerter-$env-$namespace-$instanceId --region $region
    /usr/bin/aws cloudwatch delete-alarms --alarm-names High-DiskUsage-Alerter-$env-$namespace-$instanceId --region $region
}

case "$1" in
    start)
       start
       ;;
    stop)
       stop
       ;;
    *)
       echo "Usage: $0 {start|stop}"
       exit 1
esac
exit 0
