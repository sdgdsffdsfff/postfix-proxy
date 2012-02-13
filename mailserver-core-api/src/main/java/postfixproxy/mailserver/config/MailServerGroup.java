package postfixproxy.mailserver.config;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 邮件服务器组
 * 
 * @author hongze.chi@gmail.com
 * 
 */
public class MailServerGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int id;

	private final String name;

	public MailServerGroup(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof MailServerGroup) {
			MailServerGroup mailServerGroup = (MailServerGroup) obj;
			return mailServerGroup.getId() == this.getId();
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
