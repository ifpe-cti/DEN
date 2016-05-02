package br.edu.ifpe.pdt.repositorios.util;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.util.LoggerPTD;

@Configuration
@Import(PropertyPlaceholderConfig.class)
@ComponentScan({ "br.edu.ifpe.pdt" })
@EnableJpaRepositories("br.edu.ifpe.pdt.repositorios")
@EnableTransactionManagement
public class PersistenceContext {

	@Value("${db.JdbcUrl}")
	private String databaseUrl;
	@Value("${db.user}")
	private String databaseUser;
	@Value("${db.password}")
	private String databasePassword;
	@Value("${db.driver}")
	private String driver;
	
	@Value("${db.dumpCommand}")
	private String dumpCommand;
	@Value("${db.backupTime}")
	private String backupTime;
	@Value("${db.redirectOutput}")
	private String redirectOutput;
	
	@Bean
	public DataSource dataSource() {

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			// Connect Configuration
			Class.forName(driver);
			dataSource.setDriverClass(driver);
			dataSource.setJdbcUrl(databaseUrl);
			dataSource.setUser(databaseUser);
			dataSource.setPassword(databasePassword);

			// Test Configuration
			dataSource.setIdleConnectionTestPeriod(300);
			dataSource.setPreferredTestQuery("SELECT 1;");
			dataSource.setTestConnectionOnCheckout(true);
			dataSource.setMaxIdleTime(1800);
		} catch (PropertyVetoException | ClassNotFoundException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.POSTGRESQL);
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(false);

		HibernateJpaDialect jpaDialect = new HibernateJpaDialect();

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(PTD.class.getPackage().getName());
		factory.setDataSource(dataSource());
		factory.setJpaDialect(jpaDialect);
		factory.setBeanName("entityManagerFactory");

		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}

	@Bean
	public Scheduler startSchedulerDump() {
		Scheduler sched = null;
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			sched = sf.getScheduler(); 
			BackupBD.configureBackupBD(dumpCommand, redirectOutput);
			JobDetail job = JobBuilder.newJob(BackupBD.class).withIdentity("job1", "group1").build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule(backupTime)).build();
			sched.scheduleJob(job, trigger);
			
			sched.start();
		} catch (SchedulerException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}
		return sched;
	}
}
