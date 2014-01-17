import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;



import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class testS3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		speedTest();
	}
	
	private static AmazonS3 s3client;
	static {
		AWSCredentials creds = new BasicAWSCredentials("AKIAJXBSOGEUZLIQ3GAQ", "RYr+PbOm1aEGJCl2/qV8trvlk+xjn1+NjWxngvgM");
		s3client = new AmazonS3Client(creds);
	}
	
	public static void speedTest() {
		int objectSize=10000;
		String content="";
		ObjectListing listing =s3client.listObjects("dataAccessTest");
		while (true) {
			List<S3ObjectSummary> objectList = listing.getObjectSummaries();
			for (S3ObjectSummary summary:objectList) {
				String name=summary.getBucketName();
				String key=summary.getKey();
				

				for (int i=1;i<=200;i++){
					long totalTime=0;
					for (int j=1;j<=10;j++){
						GetObjectRequest rangeObjectRequest = new GetObjectRequest(name, key);
						rangeObjectRequest.setRange(0, objectSize*i);
						long getStartTime=0;
						long getEstimatedTime=0;		
						try{
						getStartTime = System.nanoTime();
						S3Object objectPortion = s3client.getObject(rangeObjectRequest);
						getEstimatedTime = System.nanoTime() - getStartTime;
						InputStream objectData = objectPortion.getObjectContent();
						objectData.close();}
						catch(Exception e){}
						totalTime+=getEstimatedTime;
					}
					long averageTime=totalTime/10;
					content+=averageTime+"\n\r";
					//content+="File "+key+" in bucket"+name+" read "+objectSize*i+" bytes data time is: "+averageTime+"\n\r";
				}
				
			}
			if (listing.isTruncated()) {
				listing = s3client.listNextBatchOfObjects(listing);
			}
			else {
				break;
			}
		}
		System.out.println(content);
		ObjectMetadata Omd = new ObjectMetadata();
		Omd.setContentType("text/html");
		Omd.setContentLength(content.length());		
		ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());	
		PutObjectRequest Request = new PutObjectRequest("dataAccessTest", "testResult100.txt", is, Omd);
		s3client.putObject(Request);
	}
}
