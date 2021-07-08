package com.eac.diagnostic;

import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.Instance;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;

public class TestAWS {
	public static void main(String[] args) {

		
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJDO55KZDZEX5YEUQ", "Oa8LyKKtzKs5y/DmpGZDc70S4bvQKSQPmyZyS0Ro");
		  ClientConfiguration clientConfig = new ClientConfiguration();
	  		clientConfig.setProtocol(Protocol.HTTPS);
	  		clientConfig.setProxyHost("proxy.tcs.com");
	  		clientConfig.setProxyPort(8080);
	  	 
	    AmazonElasticLoadBalancingClient elb = new AmazonElasticLoadBalancingClient(credentials,clientConfig);
	    elb.setRegion(Region.getRegion(Regions.EU_WEST_1));
	    //elb.setEndpoint("https://eu-west-1.console.aws.amazon.com");
	    
	    AmazonEC2 ec2 =new AmazonEC2Client(credentials, clientConfig);
	    ec2.setRegion(Region.getRegion(Regions.EU_WEST_1));
	   // AmazonEC2 ec2 = Region.getRegion(Regions.US_WEST_2).
	    //ec2.setEndpoint("https://ec2.eu-west-1.amazonaws.com");
	    DescribeLoadBalancersResult lbs = elb.describeLoadBalancers();

	    List<LoadBalancerDescription> descriptions = lbs.getLoadBalancerDescriptions();
	    for (LoadBalancerDescription loadBalancerDescription : descriptions) {
	        System.out.println("Name: " + loadBalancerDescription.getLoadBalancerName());
	        System.out.println("DNS Name: " + loadBalancerDescription.getDNSName());
	        System.out.println("Instances:");

	        for (Instance instance : loadBalancerDescription.getInstances()) {
	            System.out.println("\t" + instance.getInstanceId());
	   /*        
	    * Working
	    *  DescribeInstancesRequest request =  new DescribeInstancesRequest();
	            
	            List<String> instanceList=new ArrayList<String>();
	            instanceList.add(instance.getInstanceId());
	    		request.setInstanceIds(instanceList);
	    		
	            DescribeInstancesResult result = ec2.describeInstances(request);
	            List<Reservation> reservations = result.getReservations();

	            for(Reservation res : reservations){
	                
	                for(com.amazonaws.services.ec2.model.Instance ins : res.getInstances()){
	                   
	                   System.out.println("value of :::::::::::"+ins.getPublicIpAddress());
	                }
	                }
	                */
	            String publicDNS=getInstancePublicDnsName(instance.getInstanceId(), ec2);
	            System.out.println("publicDNS :::::::"+publicDNS);
	            }
	           

	}
		//S3Util s3Bucket = new S3Util();
		//s3Bucket.bucketName="elt-premium-download";
		//s3Bucket.listFilesFromS3("TESTT");
		/*File file = new File("D:/test.txt");
		if(s3Bucket.uploadFlieToS3(file, "test")){
			logger.info("Success");
		}*/
		/*File fal = s3Bucket.downloadFileFromS3("02a606e0-ced2-4a95-847c-716bc9ea4346/file1.mp3");
		logger.info(fal);*/
		//s3Bucket.deleteFileFromS3("TESTT");
	
	}
private static String getInstancePublicDnsName(String instanceId,AmazonEC2 ec2) {
		
	    DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
	    List<Reservation> reservations = describeInstancesRequest.getReservations();
	
	    for (Reservation reservation : reservations) {
	      for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
	        if (instance.getInstanceId().equals(instanceId))
	          return instance.getPublicDnsName();
	      }
	    }
	    return null;
	}
}
