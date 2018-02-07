package space.kyu.punchcard.puchcard.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import space.kyu.punchcard.util.Config;

public class SmsMsgListener implements PunchCardListener {
	private static final String URL = "http://gw.api.taobao.com/router/rest";
	private static final String APPKEY = "23529034";
	private static final String SECRET = "d5a8ddabb1adb6a954136bc62351b586";
	private static final String SIGNNAME = "于凯";
	private static final String PHONE = Config.PHONE;
	private static final String SMS_TEMPLATE_CODE = "SMS_25730597";

	public static boolean sendSmsMsg(String time, String tryTime, String next) {
		if (PHONE == null || "".equals(PHONE)) {
			return false;
		}
		boolean success = false;
		TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(SIGNNAME);
		StringBuilder param = new StringBuilder();
		param.append("{time:'")
		     .append(time)
		     .append("',trytime:'")
		     .append(tryTime)
		     .append("',next:'")
		     .append(next)
		     .append("'}");
		req.setSmsParamString(param.toString());
		req.setRecNum(PHONE);
		req.setSmsTemplateCode(SMS_TEMPLATE_CODE);
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
			if (rsp != null && !rsp.isSuccess()) {
				success = false;
			} else {
				success = true;
			}
		} catch (ApiException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
		
	}
	
	@Override
	public void done(boolean success, Date nextTime, int tryTime) {
		String time = "fail";
		String trytime = "fail";
		String next = "fail";
		if (success) {
			SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
			time = df.format(new Date());
			// 暂不支持
			trytime = "-1";
			next = df.format(nextTime);
		}
		boolean sendSmsMsg = sendSmsMsg(time, trytime, next);
		if (sendSmsMsg) {
			System.out.println("短信成功发送~");
		} else {
			System.err.println("短信发送失败...");
		}

	}

}
