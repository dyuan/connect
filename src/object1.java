
public class object1 {

	private String bucketName;
	private byte [] data;
	private String storagePath;
	private String mimeType="text/html";

	public void setBucketName(String bucketName) {
		/**
		 * S3 prefers that the bucket name be lower case.  While you can
		 * create buckets with different cases, it will error out when
		 * being passed through the AWS SDK due to stricter checking.
		 */
		this.bucketName = bucketName.toLowerCase();
	}

	public String getBucketName() {
		return bucketName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data=data;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	/**
	 * Convenience method to construct the URL that points to
	 * an object stored on S3 based on the bucket name and
	 * storage path.
	 * @return the S3 URL for the object
	 */
	public String getAwsUrl () {
		return "http://"+getBucketName()+".s3.amazonaws.com/"+getStoragePath();
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
