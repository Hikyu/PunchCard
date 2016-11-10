package space.kyu.punchcard.net;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 发送打卡结果短信
 * @author yukai
 *
 */
public class SmsMsgSender {
	private static final String URL = "http://gw.api.taobao.com/router/rest";
	private static final String APPKEY = "23529034";
	private static final String SECRET = "d5a8ddabb1adb6a954136bc62351b586";
	private static final String SIGNNAME = "于凯";
	private static final String PHONE = "13116075090";
	private static final String SMS_TEMPLATE_CODE = "SMS_25730597";
	public static boolean sendSmsMsg(String time, String tryTime, String next) {
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
}
