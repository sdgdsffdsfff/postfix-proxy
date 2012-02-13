package postfixproxy.mailserver.config.dao;

import java.util.List;
import java.util.Map;

import postfixproxy.mailserver.config.MailServer;
import postfixproxy.mailserver.config.MailServerExtraInfo;

public interface MailServerDAO {

	public int insert(MailServer mailServer, MailServerExtraInfo extraInfo);

	public MailServer getMailServer(int id);

	public MailServerExtraInfo getExtraInfo(int id);

	public List<MailServer> getServerListByIdList(List<Integer> idList);

	public Map<Integer, MailServer> getServerMapByIdList(List<Integer> idList);

	public Map<Integer, MailServerExtraInfo> getExtraMapByIdList(
			List<Integer> idList);

	public int update(MailServer mailServer);

	public int update(MailServerExtraInfo extraInfo);
}
