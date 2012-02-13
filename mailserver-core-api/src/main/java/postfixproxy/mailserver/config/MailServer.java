package postfixproxy.mailserver.config;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 邮件服务器配置
 * 
 * @author hongze.chi@gmail.com
 * 
 */
public class MailServer implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int id;

	private final String host;

	private final int port;

	private final String username;

	private final String password;

	public MailServer(int id, String host, int port, String username,
			String password, int groupId) {
		this.id = id;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;

	}

	public MailServer(int id, String host, int port, int groupId) {
		this(id, host, port, "", "", groupId);
	}

	public int getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof MailServer) {
			MailServer mailServer = (MailServer) obj;
			return mailServer.getId() == this.getId();
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
