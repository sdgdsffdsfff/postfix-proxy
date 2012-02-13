package postfixproxy.mailserver.config;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 邮件服务配置的额外信息
 * 
 * @author hongze.chi@gmail.com
 * 
 */
public class MailServerExtraInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int id;

	private final int creator;

	private final Date createTime;

	private final String desc;

	public MailServerExtraInfo(int id, int creator, Date createTime, String desc) {
		this.id = id;
		this.creator = creator;
		this.createTime = createTime;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public int getCreator() {
		return creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if(obj instanceof MailServerExtraInfo) {
			MailServerExtraInfo extraInfo = (MailServerExtraInfo) obj;
			return extraInfo.getId() == this.id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
