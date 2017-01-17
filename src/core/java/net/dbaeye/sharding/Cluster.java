/**
 * @(#)Cluster.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.dao.support.sql.JdbcUtils;

/**
 * <p>
 * <a href="Cluster.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Cluster.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Cluster {
	//~ Static fields/initializers =============================================
	
	private static final Logger logger = LoggerFactory.getLogger(Cluster.class);
	
	//~ Instance fields ========================================================
	
	private Properties properties;
	private DataSource clusterDataSource;
	private DataSourceFactory dataSourceFactory;
	
	private ShardResolutionStrategy shardResolutionStrategy;
	private ShardSelectionStrategy shardSelectionStrategy;
	
	private Shard globalShard = null;
	private List<Shard> shards = new ArrayList<Shard>();
	private Map<Integer, Shard> shardsMap = new HashMap<Integer, Shard>();

	//~ Constructors ===========================================================
	
	public Cluster() {}
	
	//~ Methods ================================================================
	
	public void initialize(Properties props) throws Exception {
		logger.info("Initialize Cluster...");

		properties = props;
		
		initShards();
		
		shardResolutionStrategy.setShards(shards);
		shardResolutionStrategy.setGlobalShard(globalShard);
		shardSelectionStrategy.setShards(shards);
	}
	
	/**
	 * Load all shards into memory
	 */
	private void initShards() {
		logger.info("Initializing shards...");
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select id, global from pn_shards");
			
			while (rs.next()) {
				Shard shard = new Shard();
				shard.setId(rs.getInt(1));
				shard.setGlobalShard(rs.getInt(2) == 1);
				shardsMap.put(shard.getId(), shard);
				
				if (!shard.isGlobalShard()) {
					shards.add(shard);
				} else {
					globalShard = shard;
				}
			}
			
			rs.close();
			
			rs = stmt.executeQuery(
					"select id, shard_id, driver, username, password, url from pn_databases order by id");
			
			while (rs.next()) {
				Database db = new Database();
				db.setId(rs.getInt(1));
				db.setShardId(rs.getInt(2));
				db.setDirver(rs.getString(3));
				db.setUsername(rs.getString(4));
				db.setPassword(rs.getString(5));
				db.setUrl(rs.getString(6));
				
				Shard shard = shardsMap.get(db.getShardId());
				if (shard != null) {
					logger.info("Add database {}", db);
					db.setDataSource(new ShardedDataSource(createDataSource(db), shard));
					shard.addDatabase(db);
				} else {
					logger.error("Missing shard #{}", db.getShardId());
				}
			}
			
			rs.close();
			
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing cluster", e);
		} catch (Exception e) {
			logger.error("Exception occurred while initializing cluster", e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeConnection(conn);
		}
	}
	
	
	public List<Shard> getShards() {
		return shards;
	}
	
	public Shard getShard(ShardingClue<?> clue) {
		logger.debug("Resolving Shard of clue: {}", clue);
		Shard shard = shardResolutionStrategy.resolveShard(clue);
		
		if (shard == null) {
			throw new ShardingException("Can not resolve Shard of clue: " + clue);
		}
		
		logger.debug("Shard#{} returned for clue: {}", shard.getId(), clue);
		return shard;
	}
	
	public Shard selectShard(ShardingClue<?> clue) {
		logger.info("Selecting Shard of clue: {}", clue);
		Shard shard = shardSelectionStrategy.selectShard(clue);
		bindShard(clue, shard);
		return shard;
	}
	
	public void bindShard(ShardingClue<?> clue, Shard shard) {
		logger.info("Bind Shard[{}] width clue: {}", shard.getId(), clue);
		shardResolutionStrategy.bind(clue, shard);
	}
	
	public void updateShardBinding(ShardingClue<?> oldClue, ShardingClue<?> newClue, Shard shard) {
		logger.info("Bind Shard[{}] width clue: {}", shard.getId(), newClue);
		shardResolutionStrategy.updateBinding(oldClue, newClue, shard);
	}
	
	public void unbindShard(ShardingClue<?> clue) {
		logger.info("Unbind Shard[?] width clue: {}", clue);
		shardResolutionStrategy.unbind(clue);
	}
	
	protected DataSource createDataSource(Database db) throws Exception {
		Properties props = new Properties(properties);
		if (db != null) {
			props.put("driverClassName", db.getDirver());
			props.put("username", db.getUsername());
			props.put("password", db.getPassword());
			props.put("url", db.getUrl());
		}
		return dataSourceFactory.createDataSource(props);
	}
	
	private Connection getConnection() throws SQLException {
		return clusterDataSource.getConnection();
	}
	
	//~ Accessors ==============================================================

	/**
	 * @param clusterDataSource the clusterDataSource to set
	 */
	public void setClusterDataSource(DataSource clusterDataSource) {
		this.clusterDataSource = clusterDataSource;
	}
	
	/**
	 * @param shardResolutionStrategy the shardResolutionStrategy to set
	 */
	public void setShardResolutionStrategy(
			ShardResolutionStrategy shardResolutionStrategy) {
		this.shardResolutionStrategy = shardResolutionStrategy;
	}
	
	/**
	 * @param shardSelectionStrategy the shardSelectionStrategy to set
	 */
	public void setShardSelectionStrategy(
			ShardSelectionStrategy shardSelectionStrategy) {
		this.shardSelectionStrategy = shardSelectionStrategy;
	}
	
	/**
	 * @param dataSourceFactory the dataSourceFactory to set
	 */
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
}
