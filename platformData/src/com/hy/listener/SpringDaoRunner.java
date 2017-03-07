package com.hy.listener;

import java.sql.Connection;

import javax.sql.DataSource;

import org.nutz.dao.ConnCallback;
import org.nutz.dao.impl.DaoRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * nutz事务交给spring
 * @author yong
 *
 */
public class SpringDaoRunner implements DaoRunner
{

	public void run(DataSource dataSource, ConnCallback callback)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.doGetConnection(dataSource);
			callback.invoke(con);
		}
		catch (Exception e)
		{
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
		}
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}
}