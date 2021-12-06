# Repository Structure
This pipeline is currently setup in a way where each environment that it is deployed in has it's own branch. If you are making changes to a specific environment or are looking for documentation/procedures for them, please switch to the retrospective environment branch. General documentation can be found here in the master.

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
Procedures for updating already deployed versions of this pipeline can be found in the retrospective environment branches.

# Fresh Deployment
Please follow the below instructions prior to a brand-new deployment of this pipeline.

### Documentation
- Create a new branch in this repository with the branch name being the environment you are deploying to (i.e dev, stage, prod)
- Copy the files from one of the current environment branches and update as necessary
- Two files of note which will need updated:
    - readme.md file
    - /configs/environment-name.json (environment-name being replaced with your new environment name)

### SSM Parameters 
- Check the SSM Parameter store and make sure it already has the parameters created that the stack will use
- Note down the Parameter name format and affix as these will need to be input as CF Parameters upon deployment
    - For example the /EAC-CommonComponents-common/ is used in the Dev account. This will require the following CF Parameter:
        - UpstreamStack: EAC-CommonComponents-common

### GitHub Parameters
- Make sure you have the correct GitHub details prior to deployment. You will need
    - Repository Name, Branch Name, Repository Owner & Authorisation Token (Auth token soon to be removed in place of GitHub app)

### Deployment
- Navigate to CloudFormation and select Create Stack > With new resources (standard)
- In the specify a template section upload the `pipeline-template/componentbuilder.yml` file > Next
- Enter all of the parameters with the data gathered in the previous sections > Next > Next
- Accept the IAM Capability warnings and click 'Create Stack'

# Useful Links
- [CHQ GDrive Folder](https://drive.google.com/drive/folders/1ZFyiNBvl1q3CWFzWcuzOgRQRKIH3K3ue)
- [GoldenAMI Pipeline Repository](https://github.com/OUP/eac-core-golden-AMI)
- [CHQ Jira Ticket](https://cirrushq.atlassian.net/browse/OUPEAC-5043)