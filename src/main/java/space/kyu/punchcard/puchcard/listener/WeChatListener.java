package space.kyu.punchcard.puchcard.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

public class WeChatListener implements PunchCardListener {
	public WeChatListener(final String qrPath) {
		final IMsgHandlerFace msgHandler = new FakeMsgHandler();
		Thread wechatThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Wechat wechat = new Wechat(msgHandler, qrPath);
				wechat.start();
			}
		});

		wechatThread.setDaemon(true);
		wechatThread.start();
	}

	@Override
	public void done(boolean success, Date nextTime, int tryTime) {
		if (WechatTools.getWechatStatus()) {
			StringBuilder text = new StringBuilder();
			if (success) {
				text.append("打卡成功!").append("\n");
			} else {
				text.append("打卡失败!").append("\n");
			}
			SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
			text.append("下次打卡时间: ").append(df.format(nextTime));
			MessageTools.sendMsgById(text.toString(), Core.getInstance().getUserName());
		}
	}

	class FakeMsgHandler implements IMsgHandlerFace {

		@Override
		public String textMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public String picMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public String voiceMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public String viedoMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public String nameCardMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public void sysMsgHandle(BaseMsg msg) {
		}

		@Override
		public String verifyAddFriendMsgHandle(BaseMsg msg) {
			return null;
		}

		@Override
		public String mediaMsgHandle(BaseMsg msg) {
			return null;
		}

	}

}
