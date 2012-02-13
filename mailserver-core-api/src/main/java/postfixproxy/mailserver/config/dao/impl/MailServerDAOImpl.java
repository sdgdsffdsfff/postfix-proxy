package postfixproxy.mailserver.config.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import postfixproxy.mailserver.config.MailServer;
import postfixproxy.mailserver.config.MailServerExtraInfo;
import postfixproxy.mailserver.config.dao.MailServerDAO;
import postfixproxy.utils.MapSqlParamSourceBuilder;
import postfixproxy.utils.RowMapperBuilder;

@Repository
public class MailServerDAOImpl extends NamedParameterJdbcDaoSupport implements MailServerDAO{
	
	private final String TABLE_NAME = "`mail_server`";
	
	private final String BASIC_PROPS = "`host`, `port`, `username`, `password` ";
	
	private final String EXTRA_PROPS = "`creator`, `create_time`, `desc`";
	
	private final String INSERT_PROPS = BASIC_PROPS + ", " + EXTRA_PROPS;
	
	private final String INSERT = "insert into " + TABLE_NAME + " (" + INSERT_PROPS + ") values (:host, :port, :username, :password, :creator, :create_time, :desc)";
	
	private final String GET_MAIL_SERVER = "select `id`, " + BASIC_PROPS +  " from " + TABLE_NAME + " where `id` = :id limit 1";
	
	private final String GET_EXTRA_INFO = "select `id`, " + EXTRA_PROPS + " from " + TABLE_NAME + " where `id` = :id limit 1";
	
	private final String GET_BY_ID_LIST = "select `id`, " + BASIC_PROPS + " from " + TABLE_NAME + " where `id` in (:id_list)";
	
	private final String GET_EXTRA_BY_ID_LIST = "select `id`, " + EXTRA_PROPS + " from " + TABLE_NAME + " where `id` in (:id_list)";
	
	private final String UPDATE_BASIC = "update " + TABLE_NAME + " set `host` = :host, `port` = :port, `username` = :username, `password` = :password where `id` = :id limit 1";
	
	private final String UPDATE_EXTRA = "update " + TABLE_NAME + " set `creator` = :creator, `create_time` = :create_time, `desc` = :desc where `id` = :id limit 1";
	
	private final RowMapper<MailServer> mailServerRowMapper = RowMapperBuilder.buildRowMapper(MailServer.class, "`id`, " + BASIC_PROPS);
	
	private final RowMapper<MailServerExtraInfo> mailServerExtraRowMapper = RowMapperBuilder.buildRowMapper(MailServerExtraInfo.class, "`id`, " + EXTRA_PROPS);
	
	@Autowired
	@Qualifier("mailServerDataSource")
	void injectDataSource(DataSource dataSource){
		this.setDataSource(dataSource);
	}
	
	public MailServerDAOImpl() {
		
	}

	@Override
	public int insert(MailServer mailServer, MailServerExtraInfo extraInfo) {
		MapSqlParameterSource sqlParamSrc = MapSqlParamSourceBuilder.getInstance().build(mailServer, extraInfo);
		return this.getNamedParameterJdbcTemplate().update(INSERT, sqlParamSrc);
	}

	@Override
	public MailServer getMailServer(int id) {
		try{
			return this.getNamedParameterJdbcTemplate().queryForObject(GET_MAIL_SERVER, MapSqlParamSourceBuilder.getInstance().add("id", id).build(), this.mailServerRowMapper);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public MailServerExtraInfo getExtraInfo(int id) {
		try{
			return this.getNamedParameterJdbcTemplate().queryForObject(GET_EXTRA_INFO, MapSqlParamSourceBuilder.getInstance().add("id", id).build(), this.mailServerExtraRowMapper);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public List<MailServer> getServerListByIdList(List<Integer> idList) {
		if(!CollectionUtils.isEmpty(idList)) {
			try{
				return this.getNamedParameterJdbcTemplate().query(GET_BY_ID_LIST, MapSqlParamSourceBuilder.getInstance().add("id_list", idList).build(), this.mailServerRowMapper);
			}catch(EmptyResultDataAccessException e){
				return Collections.emptyList();
			}
		}
		return Collections.emptyList();
	}

	@Override
	public Map<Integer, MailServer> getServerMapByIdList(List<Integer> idList) {
		if(!CollectionUtils.isEmpty(idList)){
			List<MailServer> serverList = this.getServerListByIdList(idList);
			if(!CollectionUtils.isEmpty(serverList)) {
				Map<Integer, MailServer> result = new HashMap<Integer, MailServer>();
				for(MailServer ms : serverList) {
					result.put(ms.getId(), ms);
				}
				return result;
			}
		}
		return Collections.emptyMap();
	}

	@Override
	public Map<Integer, MailServerExtraInfo> getExtraMapByIdList(
			List<Integer> idList) {
		if(!CollectionUtils.isEmpty(idList)) {
			try{
				List<MailServerExtraInfo> infoList = this.getNamedParameterJdbcTemplate().query(GET_EXTRA_BY_ID_LIST, MapSqlParamSourceBuilder.getInstance().add("id_list", idList).build(), this.mailServerExtraRowMapper);
				if(!CollectionUtils.isEmpty(infoList)) {
					Map<Integer, MailServerExtraInfo> result = new HashMap<Integer, MailServerExtraInfo>();
					for(MailServerExtraInfo i : infoList) {
						result.put(i.getId(), i);
					}
					return result;
				}
			}catch(EmptyResultDataAccessException e){
				return Collections.emptyMap();
			}
		}
		return Collections.emptyMap();
	}

	@Override
	public int update(MailServer mailServer) {
		return this.getNamedParameterJdbcTemplate().update(UPDATE_BASIC, MapSqlParamSourceBuilder.getInstance().build(mailServer));
	}

	@Override
	public int update(MailServerExtraInfo extraInfo) {
		return this.getNamedParameterJdbcTemplate().update(UPDATE_EXTRA, MapSqlParamSourceBuilder.getInstance().build(extraInfo));
	}

	
}
