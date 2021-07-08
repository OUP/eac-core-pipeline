#!/bin/bash
logfile='/opt/image-stopper/log/error-log'

instancefinder() {
        local instance=$(aws ec2 describe-instances --filter "Name=tag-key,Values=aws:cloudformation:stack-name" "Name=tag-value,Values=$stack_name*" "Name=tag-key,Values=aws:cloudformation:logical-id" "Name=tag-value,Values=*ImageServer*" --region eu-west-1)
        echo $instance

}

stack_name="|STACKNAME|"

instance=$(instancefinder)
InstanceId=$(echo $instance | jq '.Reservations[].Instances[].InstanceId' | sed  's/"//g')
InstState=$(echo $instance | jq '.Reservations[].Instances[].State.Name' | sed  's/"//g')


if [[ "$InstState" = "running" ]]
then
        echo "$(date)    Instance $InstanceId running - trying to stop it " >> $logfile
        aws ec2 stop-instances --instance-ids $InstanceId --region eu-west-1  >> $logfile
        sleep 300
        instance=$(instancefinder)
        InstanceId=$(echo $instance | jq '.Reservations[].Instances[].InstanceId' | sed  's/"//g')
        InstState=$(echo $instance | jq '.Reservations[].Instances[].State.Name' | sed  's/"//g')
        if [[ "$InstState" = "stopped" ]]
        then
                echo "$(date)   Instance  $InstanceId stopped - success "  >> $logfile
        else
                echo "$(date)   Instance  $InstanceId is still not stopped please check "  >> $logfile

        fi

fi