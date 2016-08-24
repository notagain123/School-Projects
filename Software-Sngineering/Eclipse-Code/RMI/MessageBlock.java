import org.json.simple.JSONObject;

public class MessageBlock {
	
	
	private final long mSenderId;
	private final long mRecipientId;
	private final String mMessage;
	
	
	MessageBlock(long sid, long rid, String msg){
		mSenderId = sid;
		mRecipientId = rid;
		mMessage = msg;
	}
	
	public JSONObject toJSON(){
		JSONObject resp = new JSONObject();
		
		resp.put("status","success");
		resp.put("senderid",mSenderId);
		resp.put("recipientid", mRecipientId);
		resp.put("message", mMessage);
		
		
		
		return resp;
		
	}
	
	public long getRecipient(){
		return mRecipientId;
	}
	
	public String toJSONString(){
		return toJSON().toJSONString();
	}
	
	
}
