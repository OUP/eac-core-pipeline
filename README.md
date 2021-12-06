# Warning
This is the `infra` branch of this repository. The readme for this branch as been written for the infra environment, if you are working on a different environment, please switch to the correct branch for your retrospective environment.

# Overview
This is the second part of a two-pipeline deployment process. This repository contains the files & templates that will take a GoldenAMI ID from SSM Parameter Store (created in the first pipeline), and install/configure/test the EAC application. This pipeline will take a the GoldenAMI ID from SSM Parameter Store which was created in the first pipeline & install/configure/test the eac application, finally publishing the final AMI ID to AWS SSM Parameter Store & creating an EventBridge Event.

# Pipeline Walkthrough
Below is a brief overview of the pipeline, it's stages and how it interacts with all the files contained inside this repository.

- Source Stage
    - This stage looks to this GitHub repository and will automatically trigger the pipeline upon any commits to the retrospective environment branches
- Build Stage
    - This stage uses CodeBuild with the `buildspec.yml` file to install/build the EAC app & dependancies. App files located in `/eac-core/eac/`
- Test Stage
    - Under construction currently.
- AMIBuild Stage (soon to be Deploy Stage)
    - This stage uses CodeBuild with the `amibuild.yaml` file to install Packer and then validate and run the `/eac-core/packer/packer.json` file to create a final AMI with the built app from the Build Stage 
    - The final AMI is then published to SSM Parameter Store and an EventBridge Event is created (EventBridge to be implimented)

# Pipeline Update Procedure

## Pipeline files/config update
If any of the pipeline configuration files have been updated, including the buildspec's, app code or the packer files, commit the changes to the retrospective environment branch and the pipeline will automatically trigger and run with the updated configs.

## CloudFormation Stack Update
Please follow the below instructions prior to a CloudFormation Stack update of an already deployed version of this pipeline.

- Update the parameter list in the `configs/infra.json` folder with any new or updated parameters
- Commit any CloudFormation template changes to the `pipeline-template/` folder of this repository
- Deploy changes manually through CLI or Console (as there is no pipeline builder pipeline for this project)

# Fresh Deployment Procedure/Requirements
Please follow the steps listed in the master branch of this repository for instructions on how to perform a deployment of this pipeline to a new environment.

# Useful Links
- [CHQ GDrive Folder](https://drive.google.com/drive/folders/1ZFyiNBvl1q3CWFzWcuzOgRQRKIH3K3ue)
- [GoldenAMI Pipeline Repository](https://github.com/OUP/eac-core-golden-AMI)
- [CHQ Jira Ticket](https://cirrushq.atlassian.net/browse/OUPEAC-5043)